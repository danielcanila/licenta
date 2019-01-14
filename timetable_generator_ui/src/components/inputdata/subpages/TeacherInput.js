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
import CrudTableHeader from "../common/CRUDTable/CrudTableHeader";

class TeacherInput extends Component {

    constructor(props) {
        super(props);
        this.state = {
            teachers: [],
            showPopup: false
        };
    }

    componentDidMount() {
        retrieveAllTeachers()
            .then(response => {
                console.log('techers response: ', response);
                this.setState({teachers: response});
            })
            .catch(error => {
                console.error(error);
            });
    }


    // saveTeacher() {
    //     saveTeacher({name: 'new Room', capacity: 100});
    // }
    //
    // deleteTeacher() {
    //     deleteTeacher(123123123);
    // }
    //
    // updateTeacher() {
    //     updateTeacher(123123123, {name: 'new Room 2', capacity: 100});
    // }
    //
    // addLecture() {
    //     addLectures(123123123, [1, 2])
    // }
    //
    // removeLecture() {
    //     removeLectures(123123123, [1, 2])
    // }
    //
    // setUnavailabilityTimeslots() {
    //     setUnavailabilityTimeslots(123123123, [0, 1, 2])
    // }

    render() {
        let {teachers} = this.state;

        return (
            <div className="teacher-input">
                <CrudTableHeader
                    title="Manage Teachers"
                    columns={['Name', 'Lectures', 'Unavailability']}
                    addNewItem={() => this.setState({showPopup: true})}
                />

                {(!teachers || teachers.length === 0) &&
                <div className="no-items">
                    <span>No teachers to display</span>
                </div>
                }
            </div>
        );
    };
}

export default TeacherInput;
