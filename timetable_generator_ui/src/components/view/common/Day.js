import React, {Component} from 'react';

class Day extends Component {

    constructor(props) {
        super(props);
        this.days = {
            0: "Luni",
            1: "Marti",
            2: "Miercuri",
            3: "Joi",
            4: "Vineri"
        };
    }

    render() {
        return <div>{this.days[this.props.day]}</div>
    };
}

export default Day;
