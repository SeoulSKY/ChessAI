import "./Board.css";
import * as Constants from "../constants";
import React, {useState} from "react";
import Tile, {imageUrlAt} from "./Tile";
import {Piece} from "../models/Piece";
import {Action} from "../models/Action";

/**
 * Covert the given icon to the image url
 * @param icon the icon
 * @return the url or undefined if the given icon is invalid
 */
function imageUrlOf(icon: string): string | null {
    switch (icon) {
        case "♝": return Constants.PIECE_URL.blackBishop;
        case "♗": return Constants.PIECE_URL.whiteBishop;
        case "♚": return Constants.PIECE_URL.blackKing;
        case "♔": return Constants.PIECE_URL.whiteKing;
        case "♞": return Constants.PIECE_URL.blackKnight;
        case "♘": return Constants.PIECE_URL.whiteKnight;
        case "♟": return Constants.PIECE_URL.blackPawn;
        case "♙": return Constants.PIECE_URL.whitePawn;
        case "♛": return Constants.PIECE_URL.blackQueen
        case "♕": return Constants.PIECE_URL.whiteQueen;
        case "♜": return Constants.PIECE_URL.blackRook;
        case "♖": return Constants.PIECE_URL.whiteRook;
        default: return null;
    }
}

/**
 * Covert the given image url to the icon
 * @param imageUrl the image url
 * @return the icon
 */
function iconOf(imageUrl: string): string {
    switch (imageUrl) {
        case Constants.PIECE_URL.blackBishop: return "♝";
        case Constants.PIECE_URL.whiteBishop: return "♗";
        case Constants.PIECE_URL.blackKing: return "♚";
        case Constants.PIECE_URL.whiteKing: return "♔";
        case Constants.PIECE_URL.blackKnight: return "♞";
        case Constants.PIECE_URL.whiteKnight: return "♘";
        case Constants.PIECE_URL.blackPawn: return "♟";
        case Constants.PIECE_URL.whitePawn: return "♙";
        case Constants.PIECE_URL.blackQueen: return "♛";
        case Constants.PIECE_URL.whiteQueen: return "♕";
        case Constants.PIECE_URL.blackRook: return "♜";
        case Constants.PIECE_URL.whiteRook: return "♖";
        default: throw new Error(`Invalid imageUrl: ${imageUrl}`);
    }
}

/**
 * Get the pieces of the given board
 * @param board the board
 * @return the pieces
 */
function piecesOf(board: string): Piece[][] {
    let lines = board.split("\n");
    let pieces: Piece[][] = []

    for (let i = 0; i < Constants.BOARD_SIZE; i++) {
        let row: Piece[] = []
        for (let j = 0; j < Constants.BOARD_SIZE; j++) {
            let icon = lines[i][j];
            row.push({imageUrl: imageUrlOf(icon), x: j, y: i});
        }
        pieces.push(row);
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
            let icon = piece.imageUrl !== null ? iconOf(piece.imageUrl) : "□";
            line.push(icon)
        }
        lines.push(line.join("") + "\n");
    }
    return lines.join("");
}

/**
 * Read the pieces on the current chess board
 * @return the current pieces
 */
function readPieces(): Piece[][] {
    let pieces: Piece[][] = []
    for (let i = 0; i < Constants.BOARD_SIZE; i++) {
        let row: Piece[] = []
        for (let j = 0; j < Constants.BOARD_SIZE; j++) {
            row.push({imageUrl: imageUrlAt(j, i), x: j, y: i});
        }
        pieces.push(row);
    }
    return pieces;
}

/**
 * Get the pieces of the initial board
 * @return the initial pieces
 */
async function getInitialPieces(): Promise<Piece[][]> {
    let response = await fetch(`${Constants.AI_SERVER_HOST}api/initial-board`);
    if (!response.ok) {
        throw new Error(response.statusText);
    }

    let board = await response.text();
    return piecesOf(board);
}

export default function Board() {

    let [tiles, setTiles] = useState<JSX.Element[]>([]);

    function getResultPieces(action: Action) {
        let board = boardOf(readPieces());
        console.log(board);
        console.log(action);
    }

    /**
     * Draw the board from the given pieces
     * @param pieces the pieces
     */
    function drawBoard(pieces: Piece[][]) {
        let arr: JSX.Element[] = [];
        for (let i = 0; i < Constants.BOARD_SIZE; i++) {
            for (let j = 0; j < Constants.BOARD_SIZE; j++) {
                let isEven = (i+j) % 2 === 0;
                let tileColor = isEven ? "white" : "black";
                arr.push(<Tile key={`${j} ${i}`} color={tileColor} piece={pieces[i][j]} onDrop={getResultPieces}/>);
            }
        }
        setTiles(arr);
    }

    getInitialPieces().then(drawBoard)

    return (
        <div className="board" id={"board"}>
            {tiles}
        </div>
    )
}
