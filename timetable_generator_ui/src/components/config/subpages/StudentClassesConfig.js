import React, {Component} from 'react';
import './studentClassesConfig.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Button} from "react-bootstrap";

class StudentClassesConfig extends Component {

    render() {
        return (
            <Button bsStyle="primary" onClick={this.props.enableNext}>Next</Button>
        );
    };

}

export default StudentClassesConfig;
