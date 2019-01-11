import React, {Component} from 'react';
import {Button} from "react-bootstrap";
import './roomConfig.css';
import 'bootstrap/dist/css/bootstrap.min.css';

class RoomConfig extends Component {

    constructor(props) {
        super(props);
    }


    render() {
        return (
            <Button bsStyle="primary" onClick={this.props.enableNext}>Next</Button>
        );
    };

}

export default RoomConfig;
