import React, {Component} from 'react';
import './lectureInput.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {
    retrieveAllLectures,
    saveLecture,
    deleteLecture,
    updateLecture,
    addTeacherToLecture,
    removeTeacherToLecture
} from '../../services/LectureService'

class LectureInput extends Component {

    constructor(props) {
        super(props);
        this.state = {
            lectures: retrieveAllLectures()
        }
    }

    saveLecture() {
        saveLecture({name: 'new lecture'});
    }

    deleteLecture() {
        deleteLecture(123123123);
    }

    updateLecture() {
        updateLecture(123123123, {name: 'new lecture 2'});
    }

    addTeacherToLecture() {
        addTeacherToLecture(123123123, [123123123,123131,31232]);
    }

    removeTeacherToLecture() {
        removeTeacherToLecture(123123123, [123123123,2131231,13122]);
    }

    render() {
        return (
            <div>Lecture input</div>
        );
    };

}

export default LectureInput;
