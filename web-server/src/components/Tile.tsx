import "./Tile.css"
import React from "react";
import * as Globals from "../globals";
import Action from "../models/Action";
import Piece from "../models/Piece";

interface Props {
    x: number;
    y: number;
    color: string;
    piece: Piece | null;
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
 * Return the piece at the given coordinate of the board
 * @param x the x coordinate
 * @param y the y coordinate
 * @return the piece, or null if the piece at the given position is not present
 */
export function pieceAt(x: number, y: number): Piece | null {
    let tiles = document.getElementsByClassName("tile");
    let linearIndex = x + y * Globals.BOARD_SIZE;

    let tile = tiles.item(linearIndex);
    if (tile === null) {
        throw new Error(`Invalid tile index: ${linearIndex}`);
    }

    let image = tile.firstElementChild as HTMLImageElement;
    return image === null ? null : {
        imageUrl: new URL(image.src).pathname.substring(1),
        x: x,
        y: y,
        actions: JSON.parse(image.dataset.actions!)
    };
}

export default function Tile({x, y, color, piece, onDrop}: Props) {

    function dropPiece(event: React.DragEvent) {
        event.preventDefault();

        let droppedImage = document.getElementById(event.dataTransfer.getData("text")) as HTMLImageElement;

        let [imageUrl, stringX, stringY] = droppedImage.id.split(" ");

        console.log("Possible actions: ");
        console.log(droppedImage.dataset.actions!);

        x = Number(stringX);
        y = Number(stringY);

        let actions: Action[] = JSON.parse(droppedImage.dataset.actions!);
        let piece: Piece = {imageUrl: imageUrl, x: x, y: y, actions: actions};

        let target = event.target as HTMLElement
        if (target.classList.contains("tile")) {
            [x, y] = target.id.split(" ").map(Number);
        } else if (target.tagName === "IMG") {
            [, x, y] = target.id.split(" ").map(Number);
        } else {
            console.log("Piece is dropped on a wrong place.");
            return;
        }

        if (actions.find(action => action.x === x && action.y === y) === undefined) {
            console.log("The action is invalid.");
            return;
        }

        let action: Action = {
            piece: {
                icon: Globals.iconOf(piece.imageUrl),
                x: piece.x,
                y: piece.y
            },
            x: x,
            y: y
        }

        onDrop(action);
    }

    return (
        <div className={"tile " + color} id={`${x} ${y}`} onDragOver={allowDrop} onDrop={dropPiece}>
            {piece !== null && <img id={`${piece.imageUrl} ${piece.x} ${piece.y}`} className={"piece"}
                                    src={piece.imageUrl} draggable={Globals.isWhite(piece)}
                                             onDragStart={dragPiece} alt={piece.imageUrl}
                                             data-actions={JSON.stringify(piece.actions)}></img>}
        </div>
    )
}
