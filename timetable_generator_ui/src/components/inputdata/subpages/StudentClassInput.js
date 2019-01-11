import React, {Component} from 'react';
import './studentClassInput.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {
    retrieveAllStudentClasses,
    saveClass,
    deleteClass,
    updateClass,
    addSubclass
} from '../../services/StudentClassService'

class StudentClassInput extends Component {

    constructor(props) {
        super(props);
        this.state = {
            classes: retrieveAllStudentClasses
        };
    }

    saveClass() {
        saveClass({name: 'new class'});
    }

    deleteClass() {
        deleteClass(123123123);
    }

    updateClass() {
        updateClass(123123123, {name: 'new class 2'});
    }

    addSubclass() {
        addSubclass(10, [5, 6])
    }

    render() {
        return (
            <div>Student class input</div>
        );
    };

}

export default StudentClassInput;
