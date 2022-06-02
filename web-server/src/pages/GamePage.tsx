import ChessBoard from "../components/ChessBoard";
import Dashboard from "../components/Dashboard";
import React, {useRef, useState} from "react";
import {GameContext} from "../context/GameContext";
import * as Globals from "../globals";
import "./GamePage.css";
import {duration} from "moment";

export default function GamePage() {

    let [isThinking, setIsThinking] = useState(false);
    let intelligenceLevel = useRef(Globals.DEFAULT_INTELLIGENCE_LEVEL);
    let promotingIcon = useRef(Globals.DEFAULT_PROMOTING_ICON);
    let [minimaxValue, setMinimaxValue] = useState(0);
    let timeLimit = useRef(duration(Globals.DEFAULT_TIME_LIMIT_IN_SECONDS, "seconds"));

    return (
        <div className={"game-page"}>
            <GameContext.Provider value={{
                isThinking, setIsThinking, intelligenceLevel, promotingIcon, minimaxValue, setMinimaxValue, timeLimit
            }}>
                <ChessBoard/>
                <Dashboard/>
            </GameContext.Provider>
        </div>
    )
}
