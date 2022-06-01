import {useGameContext} from "../context/GameContext";
import "./DashBoard.css";
import robotProfilePicture from "../assets/robot_profile_picture.png"
import helpIcon from "../assets/help_icon.png";
import CircularProgress from "@mui/material/CircularProgress";
import Slider from "@mui/material/Slider";
import * as Globals from "../globals";
import {SyntheticEvent} from "react";

export default function Dashboard() {
    let {isThinking, intelligenceLevel} = useGameContext();

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
                    <CircularProgress size={"15px"} style={{"marginLeft": "10px"}}/>
                </div>}

            </div>
            <div className={"score-panel"}>
                <span className={"score"}>Score: 0</span>
                <div className={"hover-container"}>
                    <img className={"score-help"} src={helpIcon} alt={"helpIcon"}></img>
                    <span className={"hover-text"}>
                        {"A positive number means that Black's position is better. A negative means things look better for White."}
                    </span>
                </div>
            </div>
            <div className={"intelligence-slider"}>
                {"AI's Intelligence Level"}
                <Slider key={"key"} defaultValue={Globals.DEFAULT_INTELLIGENCE_LEVEL} min={0} max={4} step={1} valueLabelDisplay="auto" disabled={isThinking}
                        onChange={handleSliderChange}/>
            </div>
        </div>
    )
}