import "./Tile.css"

interface TileProps {
    color: string
    imageUrl?: string
}

export default function Tile({color, imageUrl}: TileProps) {
    return (
        <div className={"tile " + color}>
            {imageUrl && <div style={{backgroundImage: `url(${imageUrl})`}} className={"piece"}></div>}
        </div>
    )
}
