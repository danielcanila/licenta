import React, {Component} from 'react';

class Timeslot extends Component {

    constructor(props) {
        super(props);
        this.slots = {
            0: "08:00",
            1: "09:00",
            2: "10:00",
            3: "11:00",
            4: "12:00",
            5: "13:00",
            6: "14:00"
        };
    }

    render() {
        return <div>{this.slots[this.props.timeslot]}</div>
    };
}

export default Timeslot;
