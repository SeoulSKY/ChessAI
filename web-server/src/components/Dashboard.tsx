import {useGameContext} from "../context/GameContext";
import "./DashBoard.css";
import robotProfilePicture from "../assets/robot_profile_picture.png";
import helpIcon from "../assets/help_icon.png";
import CircularProgress from "@mui/material/CircularProgress";
import Slider from "@mui/material/Slider";
import * as Globals from "../globals";
import {FormEvent, SyntheticEvent, useRef, useState} from "react";

import {LocalizationProvider, TimePicker} from "@mui/x-date-pickers";
import {AdapterMoment} from "@mui/x-date-pickers/AdapterMoment";
import TextField from "@mui/material/TextField";
import Switch from "@mui/material/Switch";
import {duration, Duration} from "moment";


export default function Dashboard() {
    const TIME_LIMIT_LABEL = "AI's Time Limit";

    let {isThinking, intelligenceLevel, minimaxValue, timeLimit, promotingIcon} = useGameContext();
    let [timeInput, setTimeInput] = useState<Duration | null>(null);
    let [timeLabel, setTimeLabel] = useState(TIME_LIMIT_LABEL);
    let [isTimeDisabled, setIsTimeDisabled] = useState(true);

    let timePicker = useRef<HTMLInputElement>(null);

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
                <LocalizationProvider dateAdapter={AdapterMoment}>
                    <TimePicker
                        inputRef={timePicker}
                        disabled={isTimeDisabled || isThinking}
                        views={['minutes', 'seconds']}
                        inputFormat="mm:ss"
                        mask="__:__"
                        label={timeLabel}
                        value={timeInput}
                        minTime={duration(Globals.MIN_TIME_LIMIT_IN_SECONDS, "seconds")}
                        renderInput={(params) => <TextField {...params} />}
                        onChange={(newValue: Duration | null) => {
                            setTimeInput(newValue);
                            if (newValue === null || !newValue.isValid()) {
                                return;
                            }

                            let [minutes, seconds] = timePicker.current!.value.split(":").map(Number);
                            timeLimit.current = duration({minutes, seconds}).asSeconds();
                        }}
                        onError={(error) => {
                            if (error === "invalidDate") {
                                setTimeLabel("Time format is invalid");
                            } else if (error === "minTime") {
                                setTimeLabel(`Time limit must be at least ${Globals.MIN_TIME_LIMIT_IN_SECONDS} seconds`);
                            } else {
                                setTimeLabel(TIME_LIMIT_LABEL);
                            }
                        }}
                    />
                    <Switch className={"time-limit-switch"} disabled={isThinking} onClick={(event) => {
                        let toggleSwitch = event.target as HTMLInputElement;
                        setIsTimeDisabled(!toggleSwitch.checked);

                        let [minutes, seconds] = timePicker.current!.value.split(":").map(Number);

                        if (!toggleSwitch.checked || isNaN(minutes) || isNaN(seconds) ||  minutes > 59 || seconds > 59) {
                            timeLimit.current = null;
                        } else {
                            timeLimit.current = minutes*60 + seconds;
                        }
                    }}/>
                </LocalizationProvider>
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