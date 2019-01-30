import React, {Component} from 'react';
import './studentClassesConfig.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Button} from "react-bootstrap";
import {
    retrieveLatestConfig,
    saveConfig,
    addStudentsToConfig
} from '../../services/ConfigService';

import {
    retrieveAllStudentClasses
} from '../../services/StudentClassService';
import CheckboxList from "../../commons/CheckboxList/CheckboxList";

class StudentClassesConfig extends Component {

    constructor(props) {
        super(props);
        this.state = {
            studentClasses: [],
            selectedClasses: [],
        };

        this.addStudentsClassesToConfig = this.addStudentsClassesToConfig.bind(this);
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
            })
            .catch(error => {
                console.error(error);
            });
    }

    onInit = (api) => {
        this.getSelectedStudents = api.getSelectedItems;
    };

    addStudentsClassesToConfig() {
        let selectedItems = this.getSelectedStudents().map(element => {
            return element.id;
        });

        addStudentsToConfig(this.props.configId, selectedItems);
        this.props.enableNext();
    }

    render() {
        let {studentClasses, selectedClasses} = this.state;
        if (studentClasses.length === 0) {
            return <div>Waiting for data...</div>
        }
        return (
            <div>
                <CheckboxList
                    onInit={this.onInit}
                    items={studentClasses}
                    selectedItems={selectedClasses}
                />

                <Button bsStyle="primary" onClick={this.addStudentsClassesToConfig}>Next</Button>
            </div>
        );


    };

}

export default StudentClassesConfig;
