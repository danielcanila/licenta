import React, {Component} from 'react';

class Timeslot extends Component {

    constructor(props) {
        super(props);
        this.state = {
            slots: {
                0: "08:00",
                1: "10:00",
                2: "12:00",
                3: "14:00",
                4: "16:00",
                5: "18:00",
                6: "20:00"
            }
        };
    }

    render() {
        return <div>{this.state.slots[this.props.timeslot]}</div>
    };
}

export default Timeslot;
