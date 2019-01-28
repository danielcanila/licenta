import React, {Component} from 'react';
import './studentClassesConfig.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Button} from "react-bootstrap";
import {
    retrieveLatestConfig,
    saveConfig
} from '../../services/ConfigService';

import {
    retrieveAllStudentClasses
} from '../../services/StudentClassService';

class StudentClassesConfig extends Component {

    constructor(props) {
        super(props);
        this.state = {
            studentClasses: []
        };
    }

    componentDidMount() {
        retrieveAllStudentClasses()
            .then(response => {
                let students = response.map(studentClass => {
                    return {
                        id: studentClass.id,
                        name: studentClass.name
                    }
                });
                this.setState({studentClasses: students});
                console.log(this.state.studentClasses);
            })
            .catch(error => {
                console.error(error);
            });
    }

    render() {
        return (
            <Button bsStyle="primary" onClick={this.props.enableNext}>Next</Button>
        );
    };

}

export default StudentClassesConfig;
