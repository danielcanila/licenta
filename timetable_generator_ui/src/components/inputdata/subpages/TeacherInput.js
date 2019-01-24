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
import {retrieveAllLectures} from "../../services/LectureService";

import CrudTableHeader from "../common/CRUDTable/CrudTableHeader";
import CrudTableRow from "../common/CRUDTable/CrudTableRow";

class TeacherInput extends Component {

    constructor(props) {
        super(props);
        this.state = {
            teachers: [],
            teachersReady: false,
            showPopup: false
        };
    }

    componentDidMount() {
        this.getTeachers();
    }

    getTeachers() {
        retrieveAllTeachers()
            .then(response => {
                this.setState({teachers: response, teachersReady: true});
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

    getTeacherLectures(teacher) {
        return teacher.lectures.map(l => l.name).join(', ');
    }

    getUnavailabilitySlots(unavailabilitySlots) {
        if(!unavailabilitySlots || unavailabilitySlots.length === 0) return '-';
    }

    render() {
        let {teachers, teachersReady} = this.state;

        return (
            <div className="teacher-input">
                <CrudTableHeader
                    title="Manage Teachers"
                    columns={['Name', 'Lectures', 'Unavailability']}
                    addNewItem={() => this.setState({showPopup: true})}
                />

                <div className="scrollable-items">
                    {teachers && teachersReady &&
                        teachers.map(teacher => {
                            let lectures = this.getTeacherLectures(teacher),
                                unavailability = this.getUnavailabilitySlots(teacher.unavailabilitySlots);
                            return (
                                <CrudTableRow
                                    key={teacher.id}
                                    columns={[teacher.name, lectures, unavailability]}
                                    // editable={teacher.editable}
                                    // toggleEdit={() => this.toggleEdit(teacher.id, teacher)}
                                    // updateRow={() => this.updateClassRow(teacher.id, teacher)}
                                    // onRemove={() => this.onRemove(teacher.id)}
                                />
                            );
                        })
                    }
                </div>

                {(teachersReady && (!teachers || teachers.length === 0)) &&
                <div className="no-items">
                    <span>No teachers to display</span>
                </div>
                }

            </div>
        );
    };
}

export default TeacherInput;
