import "./ChessBoard.css";
import * as Globals from "../globals";
import React, {useEffect, useRef, useState} from "react";
import Tile, {pieceAt} from "./Tile";
import Piece from "../models/Piece";
import Action from "../models/Action";
import {useGameContext} from "../context/GameContext";
import useAsyncError from "../context/AsyncErrorContext";

/**
 * Get the pieces of the given board
 * @param board the board
 * @return the pieces
 */
async function piecesOf(board: string): Promise<Piece[][]> {
    let response = await fetch(encodeURI(`${Globals.AI_SERVER_HOST}api/actions?board=${board}`));
    if (!response.ok) {
        throw Error(response.statusText);
    }

    let pieces: Piece[][] = [...Array(Globals.BOARD_SIZE)].map(() => Array(Globals.BOARD_SIZE).fill(null));
    let lines = board.split("\n");

    for (let i = 0; i < Globals.BOARD_SIZE; i++) {
        for (let j = 0; j < Globals.BOARD_SIZE; j++) {
            let icon = lines[i][j];
            if (icon !== Globals.PIECE_ICON.none) {
                pieces[i][j] = {imageUrl: Globals.imageUrlOf(icon), x: j, y: i, actions: []}
            }
        }
    }

    let actions: Action[] = await response.json();
    console.log(`Response from ${response.url}: `);
    console.log(actions);

    for (let action of actions) {
        let piece: Piece = pieces[action.piece.y][action.piece.x];
        piece.actions.push(action);
    }

    return pieces;
}

/**
 * Get the board of the given pieces
 * @param pieces the pieces
 * @return the board
 */
function boardOf(pieces: Piece[][]): string {
    let lines: string[] = [];

    for (let row of pieces) {
        let line: string[] = []
        for (let piece of row) {
            let icon = piece !== null ? Globals.iconOf(piece.imageUrl) : Globals.PIECE_ICON.none;
            line.push(icon)
        }
        lines.push(line.join("") + "\n");
    }

    return lines.join("");
}

/**
 * Get the pieces on the current chess board
 * @return the current pieces
 */
function getPieces(): Piece[][] {
    let pieces: Piece[][] = [...Array(Globals.BOARD_SIZE)].map(() => Array(Globals.BOARD_SIZE).fill(null));
    for (let i = 0; i < Globals.BOARD_SIZE; i++) {
        for (let j = 0; j < Globals.BOARD_SIZE; j++) {
            let piece = pieceAt(j, i);
            if (piece !== null) {
                pieces[i][j] = piece;
            }
        }
    }
    return pieces;
}

export default function ChessBoard() {

    let boardElement = useRef<HTMLDivElement>(null);
    let [tiles, setTiles] = useState<JSX.Element[]>([]);
    let {setIsThinking, promotingIcon, intelligenceLevel, setMinimaxValue, timeLimit} = useGameContext();
    let throwError = useAsyncError();

    /**
     * Get the pieces of the initial board
     * @return the initial pieces
     */
    async function getInitialPieces(): Promise<Piece[][]> {
        let response = await fetch(`${Globals.AI_SERVER_HOST}api/initial-board`);

        if (!response.ok) {
            throw Error(response.statusText);
        }

        let board = await response.text();
        console.log(`Response from ${response.url}`);
        console.log(board);
        return piecesOf(board);
    }

    /**
     * Apply the given action to the current board
     * @param action the action
     */
    async function apply(action: Action) {
        interface Result {
            board: string,
            winner?: string | null
        }

        /**
         * Check if the game is over
         * @param result true if it is, false otherwise
         */
        function isGameOver(result: Result) {
            return result.winner !== undefined;
        }

        function handleGameTermination(result: Result) {
            if (!isGameOver(result)) {
                return;
            }

            setTimeout(() => {
                let message;
                if (result.winner === "black") {
                    message = "You Lost!";
                } else if (result.winner === "white") {
                    message = "You Won!";
                } else {
                    message = "Draw!";
                }

                alert(message);
                getInitialPieces()
                    .then(setBoard)
                    .catch(throwError)
                    .finally(() => {
                        setIsThinking(false);
                        boardElement.current!.removeAttribute("onmousedown");
                    });
            }, 0);
        }

        setIsThinking(true);
        boardElement.current!.setAttribute("onmousedown", "return false");

        let data = {
            "board": boardOf(getPieces()),
            "action": action,
            "promotingIcon": promotingIcon.current
        }

        let resultResponse;
        try {
            resultResponse = await fetch(`${Globals.AI_SERVER_HOST}api/result`, {
                "method": "POST",
                "headers": {
                    "Content-Type": "application/json"
                },
                "body": JSON.stringify(data)
            });
        } catch (e: any) {
            throwError(new Error(e + ". Make sure if the AI server is running."));
            throw e;
        }

        if (!resultResponse.ok) {
            throw Error(resultResponse.statusText);
        }

        let result: Result = await resultResponse.json();
        console.log(`Response from ${resultResponse.url}: `)
        console.log(result)
        piecesOf(result.board).then(setBoard);

        if (isGameOver(result)) {
            handleGameTermination(result);
            return;
        }

        let url = `${Globals.AI_SERVER_HOST}api/decision?board=${result.board}&intelligenceLevel=${intelligenceLevel.current}`;
        if (timeLimit.current !== null) {
            url += `&timeLimit=${timeLimit.current}`;
        }

        let decisionResponse = await fetch(encodeURI(url));
        if (!decisionResponse.ok) {
            throw Error(decisionResponse.statusText);
        }

        interface BotDecision {
            timeTaken: string;
            minimaxValue: number;
            actionTaken: {
                piece: {
                    icon: string,
                    x: number,
                    y: number
                }
                x: number,
                y: number
            }
            result: Result,
            numNodesExpanded: number
        }

        let decision: BotDecision = await decisionResponse.json();
        result = decision.result
        console.log(`Response from ${decisionResponse.url}: `);
        console.log(decision);

        piecesOf(result.board)
            .then(setBoard)
            .then(() => handleGameTermination(result))
            .finally(() => {
                boardElement.current!.removeAttribute("onmousedown");
                setIsThinking(false);
            });
    }

    /**
     * Set the board from the given pieces
     * @param pieces the pieces
     */
    function setBoard(pieces: Piece[][]) {
        fetch(encodeURI(`${Globals.AI_SERVER_HOST}/api/evaluation?board=${boardOf(pieces)}`))
            .then(response => response.text())
            .then(value => setMinimaxValue(-Number(Number(value).toFixed(2))));
        let arr: JSX.Element[] = [];
        for (let i = 0; i < Globals.BOARD_SIZE; i++) {
            for (let j = 0; j < Globals.BOARD_SIZE; j++) {
                let isEven = (i+j) % 2 === 0;
                let tileColor = isEven ? "white" : "black";
                arr.push(<Tile key={`${i} ${j}`} x={j} y={i} color={tileColor} piece={pieces[i][j]} onDrop={apply}/>);
            }
        }
        setTiles(arr);
    }

    useEffect(() => {
        console.log("Getting the initial chess board...")
        getInitialPieces().then(setBoard).catch(() => throwError(new Error("Failed to connect to the AI server.")));
    }, []); // eslint-disable-line react-hooks/exhaustive-deps

    return (
        <div className="chess-board" ref={boardElement}>
            {tiles}
        </div>
    )
}
