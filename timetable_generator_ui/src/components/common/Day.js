import React, {Component} from 'react';

class Day extends Component {

    constructor(props) {
        super(props);
        this.state = {
            days: {
                0: "Luni",
                1: "Marti",
                2: "Miercuri",
                3: "Joi",
                4: "Vineri"
            }
        };
    }

    render() {
        return <div>{this.state.days[this.props.day]}</div>
    };
}

export default Day;
