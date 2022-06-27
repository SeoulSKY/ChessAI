import {useGameContext} from "../context/GameContext";
import "./DashBoard.css";
import robotProfilePicture from "../assets/robot_profile_picture.png";
import helpIcon from "../assets/help_icon.png";
import CircularProgress from "@mui/material/CircularProgress";
import Slider from "@mui/material/Slider";
import * as Globals from "../globals";
import {FormEvent, SyntheticEvent, useRef, useState} from "react";

import TextField from "@mui/material/TextField";
import Switch from "@mui/material/Switch";


export default function Dashboard() {

    let {isThinking, intelligenceLevel, minimaxValue, timeLimit, promotingIcon} = useGameContext();
    let [isTimeDisabled, setIsTimeDisabled] = useState(true);
    let [timeError, setTimeError] = useState<string | null>(null);

    let timePicker = useRef<HTMLInputElement>(null);

    function handleTimeLimit(seconds: number) {
        if (seconds < Globals.MIN_TIME_LIMIT_IN_SECONDS) {
            setTimeError(`Must be at least ${Globals.MIN_TIME_LIMIT_IN_SECONDS} seconds`);
            timeLimit.current = null;
        } else {
            setTimeError(null);
            timeLimit.current = seconds;
        }
    }

    function handleSliderChange(_: SyntheticEvent | Event, val: number | number[]) {
        intelligenceLevel.current = Array.isArray(val) ? val[0]: val;
    }

    function selectPromotingPiece(event: FormEvent<HTMLFormElement>) {
        let target = event.target as HTMLInputElement;
        promotingIcon.current = target.value;
    }

    return (
        <div className={"dashboard"}>
            <div className={"robot-profile"}>
                <img className={"profile-picture"} src={robotProfilePicture} alt={"robot profile"} draggable={false}/>
                <p className={"robot-name"}>Chess AI</p>

                {isThinking && <div className={"robot-message"}>
                    {"Thinking..."}
                    <CircularProgress className={"circular-progress"} size={"15px"}/>
                </div>}

            </div>
            <div className={"score-panel"}>
                <span className={"score"}>Score: {minimaxValue}</span>
                <div className={"hover-container"}>
                    <img className={"score-help"} src={helpIcon} alt={"helpIcon"} draggable={false}/>
                    <span className={"hover-text"}>
                        {"A positive number means that White's position is better. A negative means things look better for Black."}
                    </span>
                </div>
            </div>
            <div className={"intelligence-slider"}>
                {"AI's Intelligence Level"}
                <Slider defaultValue={Globals.DEFAULT_INTELLIGENCE_LEVEL}
                        min={0} max={4} step={1} valueLabelDisplay="auto" disabled={isThinking}
                        onChange={handleSliderChange}/>
            </div>
            <div className={"time-limit-panel"}>
                <TextField
                    className={"time-limit-input"}
                    inputRef={timePicker}
                    label={"AI's Time Limit"}
                    error={timeError !== null}
                    disabled={isTimeDisabled || isThinking}
                    type="time"
                    InputLabelProps={{
                        shrink: true,
                    }}
                    helperText={timeError === null ? "mm:ss" : timeError}
                    defaultValue={"00:00"}

                    onChange={(event) => {
                        if (event.target.value === "") {
                            event.target.value = "00:00";
                        }

                        let [minutes, seconds] = event.target.value.split(":").map(Number);
                        handleTimeLimit(minutes*60 + seconds);
                    }}
                />
                <Switch className={"time-limit-switch"} disabled={isThinking} onClick={(event) => {
                    if (!isTimeDisabled) {
                        setTimeError(null);
                        timeLimit.current = null;
                    } else {
                        let [minutes, seconds] = timePicker.current!.value.split(":").map(Number);
                        handleTimeLimit(minutes*60 + seconds);
                    }
                    let toggleSwitch = event.target as HTMLInputElement;
                    setIsTimeDisabled(!toggleSwitch.checked);

                }}/>
            </div>
            <div className={"promoting-piece-panel"}>
                <label className={"promoting-label"}>{"Piece to Promote"}</label>
                <form className={"radio-group"} onChange={selectPromotingPiece}>
                    <label>
                        <input type="radio" name="promoting-piece" value={Globals.PIECE_ICON.whiteQueen}
                               defaultChecked={Globals.isDefaultPromotingIcon(Globals.PIECE_ICON.whiteQueen)}
                               disabled={isThinking}/>
                        <img className={"queen"} src={Globals.PIECE_URL.whiteQueen} alt={"queen"} draggable={false}/>
                    </label>
                    <label>
                        <input type="radio" name="promoting-piece" value={Globals.PIECE_ICON.whiteRook}
                               defaultChecked={Globals.isDefaultPromotingIcon(Globals.PIECE_ICON.whiteRook)}
                               disabled={isThinking}/>
                        <img className={"rook"} src={Globals.PIECE_URL.whiteRook} alt={"rook"} draggable={false}/>
                    </label>
                    <label>
                        <input type="radio" name="promoting-piece" value={Globals.PIECE_ICON.whiteKnight}
                               defaultChecked={Globals.isDefaultPromotingIcon(Globals.PIECE_ICON.whiteKnight)}
                               disabled={isThinking} />
                        <img className={"knight"} src={Globals.PIECE_URL.whiteKnight} alt={"knight"} draggable={false}/>
                    </label>
                    <label>
                        <input type="radio" name="promoting-piece" value={Globals.PIECE_ICON.whiteBishop}
                               defaultChecked={Globals.isDefaultPromotingIcon(Globals.PIECE_ICON.whiteBishop)}
                               disabled={isThinking}/>
                        <img className={"bishop"} src={Globals.PIECE_URL.whiteBishop} alt={"bishop"} draggable={false}/>
                    </label>
                </form>
            </div>
        </div>
    )
}