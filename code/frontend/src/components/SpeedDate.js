import React from 'react';
import {bandi_styles} from "../styles/bandi_styles";
import Header from "./Header";
import SubHeader from "./SubHeader";

class SpeedDate extends React.Component {
    constructor(props) {
        super(props)

        this.state = {

        }
    }

    render() {
        return (
            <div className="bg-transparent" style={bandi_styles.discover_background}>
            <Header/>
            <SubHeader text={"Register for speed-dating events here!"}/>

            <h1>Speed-Dating Events:</h1>

            </div>
        )
    }

}

export default SpeedDate;
