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
    let draggingPieceImage = event.target as HTMLImageElement;
    event.dataTransfer.setData("text", draggingPieceImage.id);

    let actions: Action[] = JSON.parse(draggingPieceImage.dataset.actions!);
    for (let action of actions) {
        let tile = tileAt(action.x, action.y);
        if (tile.hasChildNodes()) {
            let anotherPieceImage = tile.getElementsByTagName("img").item(0)!;
            anotherPieceImage.classList.add("attack-hint");
        } else {
            let actionHint = document.createElement("div") as HTMLDivElement;
            actionHint.classList.add("action-hint");
            actionHint.setAttribute("data-x", action.x.toString());
            actionHint.setAttribute("data-y", action.y.toString());
            tile.appendChild(actionHint);
        }
    }
}

/**
 * Return the tile at the given coordinate of the board
 * @param x the x coordinate
 * @param y the y coordinate
 * @return the tile
 */
function tileAt(x: number, y: number): HTMLDivElement {
    let tiles = document.getElementsByClassName("tile");
    let linearIndex = x + y * Globals.BOARD_SIZE;

    let tile = tiles.item(linearIndex) as HTMLDivElement;
    if (tile === null) {
        throw new Error(`Invalid tile index: ${linearIndex}`);
    }
    return tile;
}

/**
 * Return the piece at the given coordinate of the board
 * @param x the x coordinate
 * @param y the y coordinate
 * @return the piece, or null if the piece at the given position is not present
 */
export function pieceAt(x: number, y: number): Piece | null {
    let pieceImage = tileAt(x, y).getElementsByTagName("img").item(0) as HTMLImageElement;
    return pieceImage === null ? null : {
        imageUrl: new URL(pieceImage.src).pathname.substring(1),
        x: x,
        y: y,
        actions: JSON.parse(pieceImage.dataset.actions!)
    };
}

export default function Tile({x, y, color, piece, onDrop}: Props) {

    function dropPiece(event: React.DragEvent) {
        event.preventDefault();

        Array.from(document.getElementsByClassName("action-hint"))
            .forEach(actionHint => actionHint.remove());

        Array.from(document.getElementsByClassName("attack-hint"))
            .forEach(image => image.classList.remove("attack-hint"));

        let target = event.target as HTMLElement
        const possibleClassNames = ["tile", "piece", "action-hint"];
        if (!possibleClassNames.some(className => target.classList.contains(className))) {
            console.log("Piece is dropped on a wrong place.");
            return;
        }

        let newX = Number(target.dataset.x!);
        let newY = Number(target.dataset.y!);

        let droppedPieceImage = document.getElementById(event.dataTransfer.getData("text")) as HTMLImageElement;
        let [imageUrl, prevX, prevY] = droppedPieceImage.id.split(" ");
        let actions: Action[] = JSON.parse(droppedPieceImage.dataset.actions!);

        if (!actions.some(action => action.x === newX && action.y === newY)) {
            console.log("The action is invalid.");
            return;
        }

        let piece: Piece = {imageUrl: imageUrl, x: Number(prevX), y: Number(prevY), actions: actions};
        let action: Action = {
            piece: {
                icon: Globals.iconOf(piece.imageUrl),
                x: piece.x,
                y: piece.y
            },
            x: newX,
            y: newY
        }

        onDrop(action);
    }

    return (
        <div className={"tile " + color} onDragOver={allowDrop} onDrop={dropPiece} data-x={x} data-y={y}>
            {piece !== null && <img id={`${piece.imageUrl} ${piece.x} ${piece.y}`} className={"piece"}
                                    src={piece.imageUrl} draggable={Globals.isWhite(piece)}
                                    onDragStart={dragPiece} alt={piece.imageUrl}
                                    data-actions={JSON.stringify(piece.actions)}
                                    data-x={piece.x}
                                    data-y={piece.y}>
            </img>}
        </div>
    )
}
