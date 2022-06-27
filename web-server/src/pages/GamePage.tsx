import ChessBoard from "../components/ChessBoard";
import Dashboard from "../components/Dashboard";
import React, {useRef, useState} from "react";
import {GameContext} from "../context/GameContext";
import * as Globals from "../globals";
import "./GamePage.css";
import {ErrorBoundary, FallbackProps} from "react-error-boundary";
import Alert from "@mui/material/Alert";
import AlertTitle from "@mui/material/AlertTitle";
import CircularProgress from "@mui/material/CircularProgress";

function errorHandler({error}: FallbackProps) {
    return (
        <Alert severity="error">
            <AlertTitle>Error Occurred</AlertTitle>
            {error.message}
        </Alert>
    )
}

export default function GamePage() {

    let [isThinking, setIsThinking] = useState(false);
    let intelligenceLevel = useRef(Globals.DEFAULT_INTELLIGENCE_LEVEL);
    let promotingIcon = useRef(Globals.DEFAULT_PROMOTING_ICON);
    let [minimaxValue, setMinimaxValue] = useState(0);
    let timeLimit = useRef(null);

    return (
        <div className={"game-page"}>
            <ErrorBoundary FallbackComponent={errorHandler}>
                <React.Suspense fallback={<CircularProgress />}>
                    <GameContext.Provider value={{
                        isThinking, setIsThinking, intelligenceLevel, promotingIcon, minimaxValue, setMinimaxValue, timeLimit
                    }}>
                        <ChessBoard/>
                        <Dashboard/>
                    </GameContext.Provider>
                </React.Suspense>
            </ErrorBoundary>
        </div>
    )
}
