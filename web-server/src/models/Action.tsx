import Piece from "./Piece";

export default interface Action {
    piece: Piece;
    x: number;
    y: number;
}
