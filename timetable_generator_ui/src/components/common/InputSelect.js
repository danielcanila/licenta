import React, {Component} from 'react';

class InputSelect extends Component {

    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        return <input type="text" onChange={this.props.handleTeacherSelect} disabled={this.props.isDisabled}/>
    };
}

export default InputSelect;
