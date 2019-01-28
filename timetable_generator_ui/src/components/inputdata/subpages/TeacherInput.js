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
import {retrieveAllLectures} from '../../services/LectureService';

import CrudTableHeader from "../common/CRUDTable/CrudTableHeader";
import CrudTableRow from "../common/CRUDTable/CrudTableRow";
import TeacherPopup from '../common/teacherPopup/TeacherPopup';

class TeacherInput extends Component {

    constructor(props) {
        super(props);

        this.state = {
            teachers: [],
            lectures: [],
            teachersReady: false,
            showPopup: false,
            onEdit: false,
            editTeacher: {
                name: '',
                lectures: []
            },
            lecturesReady: false
        };

        this.dayObject = this.getDayObject();
        this.timeInterval = this.getTimeInterval();
    }

    componentDidMount() {
        this.getTeachers();
        this.getLectures();
    }

    getTeachers() {
        retrieveAllTeachers()
            .then(response => {
                console.log('teachers: ', response);
                this.setState({teachers: response, teachersReady: true});
            })
            .catch(error => {
                console.error(error);
            });
    }

    getLectures() {
        retrieveAllLectures()
            .then(response => {
                console.log('lectures: ', response);
                this.setState({lectures: response, lecturesReady: true});
            })
            .catch(error => {
                console.error(error);
            });
    }

    getDayObject() {
        return {
            0: 'Monday',
            1: 'Tuesday',
            2: 'Wednesday',
            3: 'Thursday',
            4: 'Friday'
        };
    }

    getTimeInterval() {
        return {
            0: 'from 8:00 to 9:00',
            1: 'from 9:00 to 10:00',
            2: 'from 10:00 to 11:00',
            3: 'from 11:00 to 12:00',
            4: 'from 12:00 to 13:00',
            5: 'from 13:00 to 14:00',
            6: 'from 14:00 to 15:00'
        };
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

    getSlotDay(unavailabilityNumber) {
        return this.dayObject[Math.floor(unavailabilityNumber / 7)];
    }

    getSlotInterval(unavailabilityNumber) {
        return this.timeInterval[unavailabilityNumber % 7];
    }

    getUnavailabilitySlots(slots) {
        if(!slots || slots.length === 0) return '-';
        return slots.map(s => `${this.getSlotDay(s)} ${this.getSlotInterval(s)}`);
    }

    editTeacher(teacher) {
        this.setState({
            onEdit: true,
            showPopup: true,
            editTeacher: {
                ...teacher
            }
        });
    }

    onSave = (teacherLectures) => {
        console.log('on SAVE teacherLectures...', teacherLectures);

        this.closePopupAndResetValues();
    };

    closePopupAndResetValues = () => {
        this.setState({
            editTeacher: {
                name: '',
                lectures: []
            },
            showPopup: false
        });
    };

    render() {
        let {teachers, teachersReady, lecturesReady} = this.state,
            isReady = teachersReady && lecturesReady;

        return (
            <div className="teacher-input">
                <CrudTableHeader
                    title="Manage Teachers"
                    columns={['Name', 'Lectures', 'Unavailability']}
                    addNewItem={() => this.setState({showPopup: true})}
                />

                <div className="scrollable-items">
                    {teachers && teachers.length > 0 && isReady &&
                        teachers.map(teacher => {
                            let lectures = this.getTeacherLectures(teacher),
                                unavailabilityRows = this.getUnavailabilitySlots(teacher.unavailabilitySlots);

                            return (
                                <CrudTableRow
                                    key={teacher.id}
                                    columns={[teacher.name, lectures, unavailabilityRows]}
                                    toggleEdit={() => this.editTeacher(teacher)}
                                    // onRemove={() => this.onRemove(teacher.id)}
                                />
                            );
                        })
                    }
                </div>

                {(isReady && (!teachers || teachers.length === 0)) &&
                <div className="no-items">
                    <span>No teachers to display</span>
                </div>
                }

                <TeacherPopup
                    {...this.state.editTeacher}
                    allLectures={this.state.lectures}
                    showPopup={this.state.showPopup}
                    title={this.state.onEdit ? 'Update teacher details' : 'Add new teacher'}
                    onClose={this.closePopupAndResetValues}
                    onSave={this.onSave}
                />

            </div>
        );
    };
}

export default TeacherInput;
