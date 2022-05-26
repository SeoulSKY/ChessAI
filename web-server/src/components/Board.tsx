import "./Board.css";
import * as Globals from "../globals";
import React, {useEffect, useState} from "react";
import Tile, {imageUrlAt} from "./Tile";
import Piece from "../models/Piece";
import Action from "../models/Action";

/**
 * Get the pieces of the given board
 * @param board the board
 * @return the pieces
 */
async function piecesOf(board: string): Promise<Piece[][]> {
    let response = await fetch(encodeURI(`${Globals.AI_SERVER_HOST}/api/actions?board=${board}`));
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
            let imageUrl = imageUrlAt(j, i);
            if (imageUrl !== null) {
                pieces[i][j] = {imageUrl: imageUrl, x: j, y: i, actions: []};
            }
        }
    }
    return pieces;
}

/**
 * Get the pieces of the initial board
 * @return the initial pieces
 */
async function getInitialPieces(): Promise<Piece[][]> {
    let response = await fetch(`${Globals.AI_SERVER_HOST}api/initial-board`);
    if (!response.ok) {
        throw new Error(response.statusText);
    }

    let board = await response.text();
    console.log(`Response from ${response.url}`);
    console.log(board);
    return piecesOf(board);
}

export default function Board() {

    let [tiles, setTiles] = useState<JSX.Element[]>([]);

    /**
     * Apply the given action to the current board
     * @param action the action
     */
    async function apply(action: Action) {
        let data = {
            "board": boardOf(getPieces()),
            "action": action
        }

        let response = await fetch(`${Globals.AI_SERVER_HOST}api/result`, {
            "method": "POST",
            "headers": {
                "Content-Type": "application/json"
            },
            "body": JSON.stringify(data)
        });

        if (!response.ok) {
            throw Error(response.statusText);
        }

        let board = await response.text();
        console.log(`Response from ${response.url}: `)
        console.log(board)
        piecesOf(board).then(setBoard);
    }

    /**
     * Set the board from the given pieces
     * @param pieces the pieces
     */
    function setBoard(pieces: Piece[][]) {
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
        console.log("Getting the initial board...")
        getInitialPieces().then(setBoard);
    }, []); // eslint-disable-line react-hooks/exhaustive-deps

    return (
        <div className="board" id={"board"}>
            {tiles}
        </div>
    )
}
