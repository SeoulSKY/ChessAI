import "./Board.css";
import env from "react-dotenv";
import {useState} from "react";
import Tile from "./Tile";

const BOARD_SIZE = 8;

interface Piece {
    imageUrl?: string;
    x: number;
    y: number;
}

/**
 * Covert the given icon to the image url
 * @param icon the icon
 * @return the url or undefined if the given icon is invalid
 */
function imageUrlOf(icon: string): string | undefined {
    switch (icon) {
        case "♝": return "assets/black_bishop.png";
        case "♗": return "assets/white_bishop.png";
        case "♚": return "assets/black_king.png";
        case "♔": return "assets/white_king.png";
        case "♞": return "assets/black_knight.png";
        case "♘": return "assets/white_knight.png";
        case "♟": return "assets/black_pawn.png";
        case "♙": return "assets/white_pawn.png";
        case "♛": return "assets/black_queen.png";
        case "♕": return "assets/white_queen.png";
        case "♜": return "assets/black_rook.png";
        case "♖": return "assets/white_rook.png";
        default: return undefined;
    }
}

/**
 * Get the pieces of the initial board
 */
async function getInitialPieces(): Promise<Piece[][]> {
    let response = await fetch(`${env.AI_SERVER_HOST}api/initial-board`);
    if (!response.ok) {
        throw new Error(response.statusText);
    }

    let board = await response.text();
    let lines = board.split("\n");
    let pieces: Piece[][] = []

    for (let i = 0; i < BOARD_SIZE; i++) {
        let row: Piece[] = []
        for (let j = 0; j < BOARD_SIZE; j++) {
            let icon = lines[i][j];
            row.push({imageUrl: imageUrlOf(icon), x: j, y: i});
        }
        pieces.push(row);
    }
    return pieces;
}

export default function Board() {

    let [tiles, setTiles] = useState<JSX.Element[]>([]);

    /**
     * Draw the board from the given pieces
     * @param pieces the pieces
     */
    function drawBoard(pieces: Piece[][]) {
        let arr: JSX.Element[] = [];
        for (let i = 0; i < BOARD_SIZE; i++) {
            for (let j = 0; j < BOARD_SIZE; j++) {
                let isEven = (i+j) % 2 === 0;
                let tileColor = isEven ? "white" : "black";
                arr.push(<Tile key={`${j}${i}`} color={tileColor} imageUrl={pieces[i][j].imageUrl}/>);
            }
        }
        setTiles(arr);
    }

    getInitialPieces().then(drawBoard)

    return (
        <div className="board">
            {tiles}
        </div>
    )
}
