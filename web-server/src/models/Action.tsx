import {Piece} from "./Piece";

export interface Action {
    piece: Piece;
    x: number;
    y: number;
}
