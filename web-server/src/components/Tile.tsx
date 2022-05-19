import "./Tile.css"
import React from "react";
import * as Constants from "../constants";
import {Action} from "../models/Action";
import {Piece} from "../models/Piece";


interface Props {
    color: string;
    piece: Piece;
    onDrop: (action: Action) => void;
}

function allowDrop(event: React.DragEvent) {
    event.preventDefault();
}

function dragPiece(event: React.DragEvent) {
    let piece = event.target as HTMLImageElement;
    event.dataTransfer.setData("text", piece.id);
}

/**
 * Return the image url at the given coordinate of the board
 * @param x the x coordinate
 * @param y the y coordinate
 * @return the image url
 */
export function imageUrlAt(x: number, y: number): string | null {
    let tiles = document.getElementsByClassName("tile");
    let linear_index = x + y * Constants.BOARD_SIZE;

    let tile = tiles.item(linear_index);
    if (!tile) {
        throw new Error(`Invalid tile index: ${linear_index}`);
    }

    let piece = tile.firstElementChild as HTMLImageElement;
    return piece !== null ? new URL(piece.src).pathname.substring(1) : null;
}

export default function Tile({color, piece, onDrop}: Props) {

    function dropPiece(event: React.DragEvent) {
        let tile = event.target as HTMLDivElement;
        let image = document.getElementById(event.dataTransfer.getData("text")) as HTMLImageElement;

        let [imageUrl, x, y] = image.id.split(" ");
        let piece: Piece = {imageUrl: imageUrl, x: Number(x), y: Number(y)};

        [x, y] = tile.id.split(" ");
        onDrop({piece, x: Number(x), y: Number(y)});
    }

    return (
        <div className={"tile " + color} id={`${piece.x} ${piece.y}`} onDragOver={allowDrop} onDrop={dropPiece}>
            {piece.imageUrl && <img id={`${piece.imageUrl} ${piece.x} ${piece.y}`} className={"piece"} src={piece.imageUrl} draggable={true} onDragStart={dragPiece} alt={piece.imageUrl}></img>}
        </div>
    )
}
