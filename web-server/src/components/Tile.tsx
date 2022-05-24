import "./Tile.css"
import React from "react";
import * as Globals from "../globals";
import Action from "../models/Action";
import Piece from "../models/Piece";

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
    let linearIndex = x + y * Globals.BOARD_SIZE;

    let tile = tiles.item(linearIndex);
    if (tile === null) {
        throw new Error(`Invalid tile index: ${linearIndex}`);
    }

    let piece = tile.firstElementChild as HTMLImageElement;
    return piece !== null ? new URL(piece.src).pathname.substring(1) : null;
}

export default function Tile({color, piece, onDrop}: Props) {

    function dropPiece(event: React.DragEvent) {
        event.preventDefault();

        let droppedImage = document.getElementById(event.dataTransfer.getData("text")) as HTMLImageElement;

        let [imageUrl, x, y] = droppedImage.id.split(" ");
        if (imageUrl === null) {
            throw new Error("Cannot drop an empty piece to the board.");
        }

        let piece: Piece = {imageUrl: imageUrl, x: Number(x), y: Number(y)};

        let target = event.target as HTMLElement
        if (target.classList.contains("tile")) {
            [x, y] = target.id.split(" ");
        } else if (target.tagName === "IMG") {
            [, x, y] = target.id.split(" ");
        } else {
            throw Error("Piece is dropped on a wrong place.")
        }

        onDrop({piece: piece, x: Number(x), y: Number(y)});
    }

    return (
        <div className={"tile " + color} id={`${piece.x} ${piece.y}`} onDragOver={allowDrop} onDrop={dropPiece}>
            {piece.imageUrl !== null && <img id={`${piece.imageUrl} ${piece.x} ${piece.y}`} className={"piece"}
                                    src={piece.imageUrl} draggable={Globals.isWhite(Globals.iconOf(piece.imageUrl))}
                                             onDragStart={dragPiece}
                                             alt={piece.imageUrl}></img>}
        </div>
    )
}
