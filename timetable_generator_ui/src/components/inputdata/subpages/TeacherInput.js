import React, {Component} from 'react';
import './teacherInput.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {
    retrieveAllTeachers,
    saveTeacher,
    deleteTeacher,
    updateTeacher,
    addLectures,
    removeLectures,
    setUnavailabilityTimeslots
} from '../../services/TeacherService';

class TeacherInput extends Component {

    constructor(props) {
        super(props);
        this.state = {
            teachers: retrieveAllTeachers()
        };
    }

    render() {
        return (
            <div>Teacher class input</div>
        );
    };


    saveTeacher() {
        saveTeacher({name: 'new Room', capacity: 100});
    }

    deleteTeacher() {
        deleteTeacher(123123123);
    }

    updateTeacher() {
        updateTeacher(123123123, {name: 'new Room 2', capacity: 100});
    }

    addLecture() {
        addLectures(123123123, [1, 2])
    }

    removeLecture() {
        removeLectures(123123123, [1, 2])
    }

    setUnavailabilityTimeslots() {
        setUnavailabilityTimeslots(123123123, [0, 1, 2])
    }

}

export default TeacherInput;
