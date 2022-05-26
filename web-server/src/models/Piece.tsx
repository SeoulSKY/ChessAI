import Action from "./Action";

export default interface Piece {
    imageUrl: string;
    x: number;
    y: number;
    actions: Action[];
}
