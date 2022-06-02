import {useGameContext} from "../context/GameContext";
import "./DashBoard.css";
import robotProfilePicture from "../assets/robot_profile_picture.png";
import helpIcon from "../assets/help_icon.png";
import CircularProgress from "@mui/material/CircularProgress";
import Slider from "@mui/material/Slider";
import * as Globals from "../globals";
import {SyntheticEvent, useState} from "react";

import {LocalizationProvider} from "@mui/x-date-pickers";
import {TimePicker} from "@mui/x-date-pickers";
import {AdapterMoment} from "@mui/x-date-pickers/AdapterMoment";
import TextField from "@mui/material/TextField";
import {duration, Duration} from "moment";
import Switch from "@mui/material/Switch";


export default function Dashboard() {
    const TIME_LIMIT_LABEL = "AI's Time Limit";

    let {isThinking, intelligenceLevel, minimaxValue, timeLimit} = useGameContext();
    let [timeInput, setTimeInput] = useState<Duration | null>(null);
    let [timeLabel, setTimeLabel] = useState(TIME_LIMIT_LABEL);
    let [isTimeDisabled, setIsTimeDisabled] = useState(true);

    function handleSliderChange(_: SyntheticEvent | Event, val: number | number[]) {
        intelligenceLevel.current = Array.isArray(val) ? val[0]: val;
    }

    return (
        <div className={"dashboard"}>
            <div className={"robot-profile"}>
                <img className={"profile-picture"} src={robotProfilePicture} alt={"robot profile"}></img>
                <p className={"robot-name"}>Chess AI</p>

                {isThinking && <div className={"robot-message"}>
                    {"Thinking..."}
                    <CircularProgress className={"circular-progress"} size={"15px"}/>
                </div>}

            </div>
            <div className={"score-panel"}>
                <span className={"score"}>Score: {minimaxValue}</span>
                <div className={"hover-container"}>
                    <img className={"score-help"} src={helpIcon} alt={"helpIcon"}></img>
                    <span className={"hover-text"}>
                        {"A positive number means that Black's position is better. A negative means things look better for White."}
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
                        disabled={isTimeDisabled}
                        views={['minutes', 'seconds']}
                        inputFormat="mm:ss"
                        mask="__:__"
                        label={timeLabel}
                        value={timeInput}
                        minTime={duration(Globals.MIN_TIME_LIMIT_IN_SECONDS, "seconds")}
                        renderInput={(params) => <TextField {...params} />}
                        onChange={(newValue: Duration | null) => setTimeInput(newValue)}
                        onError={(error) => {
                            if (error === "invalidDate") {
                                setTimeLabel("Time format is invalid");
                            } else if (error === "minTime") {
                                setTimeLabel(`Time limit must be at least ${Globals.MIN_TIME_LIMIT_IN_SECONDS} seconds`);
                            } else {
                                setTimeLabel(TIME_LIMIT_LABEL);
                            }
                        }}
                        onAccept={(newValue: Duration | null) => timeLimit.current = newValue!}
                    />
                    <Switch className={"time-limit-switch"} onChange={(event) => {
                        let toggleSwitch = event.target as HTMLInputElement;
                        setIsTimeDisabled(!toggleSwitch.checked);
                        if (!toggleSwitch.checked) {
                            timeLimit.current = null;
                        }
                    }}/>
                </LocalizationProvider>
            </div>
        </div>
    )
}