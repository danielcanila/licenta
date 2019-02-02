import React, {Component} from 'react';
import './teacherInput.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {
    retrieveAllTeachers,
    saveTeacher,
    deleteTeacher,
    updateTeacher,
    addLectures,
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
				lectures: [],
				unavailabilitySlots: []
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
                this.setState({teachers: response, teachersReady: true});
            })
            .catch(error => {
                console.error(error);
            });
    }

    getLectures() {
        retrieveAllLectures()
            .then(response => {
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
    
    deleteTeacher(id) {
        deleteTeacher(id).then(() => {
			this.setState({
				teachers: [...this.state.teachers.filter(t => t.id !== id)]
			})
		});
    }

    getTeacherLectures(teacher) {
        return teacher.lectures.map(l => l.name).join(', ');
    }

	// dayIndex = nr / 7
    getSlotDay(unavailabilityNumber) {
        return this.dayObject[Math.floor(unavailabilityNumber / 7)];
    }

	// timeIndex = nr % 7
    getSlotInterval(unavailabilityNumber) {
        return this.timeInterval[unavailabilityNumber % 7];
	}
	
	// SLOT Number = dayIndex * 7 + timeIndex
	reverseUnavailabilitySlotsToNumber({day, time}) {
		let dayIndex = Object.values(this.dayObject).findIndex(d => d === day),
			timeIndex = Object.values(this.timeInterval).findIndex(t => t === time);

		return dayIndex * 7 + timeIndex;
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
				...teacher,
				unavailabilitySlots: teacher.unavailabilitySlots.map(slot => ({
					day: this.getSlotDay(slot),
					time: this.getSlotInterval(slot)
				}))
            }
        });
	}
	
	async saveNewTeacher({lecturesIds, name, slotsNumbers}) {
		let teacherObject = await saveTeacher({name});
		teacherObject = await addLectures(teacherObject.id, lecturesIds);
		teacherObject = await setUnavailabilityTimeslots(teacherObject.id, slotsNumbers);

		this.setState({
			teachers: [...this.state.teachers, teacherObject]
		});
	}

	async editTeacherSave(id, {lecturesIds, name, slotsNumbers}) {
		let teacherObject = await updateTeacher(id, {name});
		teacherObject = await addLectures(id, lecturesIds);
		teacherObject = await setUnavailabilityTimeslots(id, slotsNumbers);

		let teachers = this.state.teachers.map(teacher => {
			if(teacher.id === id) {
				return teacherObject;
			}
			return teacher;
		});

		this.setState({
			teachers
		});
	}

    onSave = ({selectedLectures, name, unavailabilitySlots}) => {
		let slotsNumbers = unavailabilitySlots.map(s => this.reverseUnavailabilitySlotsToNumber({...s})),
			lecturesIds = selectedLectures.map(l => l.id),
			teacherId = this.state.editTeacher.id;

		this.state.onEdit ? this.editTeacherSave(teacherId, {lecturesIds, name, slotsNumbers}) : this.saveNewTeacher({lecturesIds, name, slotsNumbers});

        this.closePopupAndResetValues();
    };

    closePopupAndResetValues = () => {
        this.setState({
            editTeacher: {
                name: '',
				lectures: [],
				unavailabilitySlots: []
            },
			showPopup: false,
			onEdit: false
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
                                    onRemove={() => this.deleteTeacher(teacher.id)}
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
                    getTimeInterval={this.getTimeInterval}
                    getDayObject={this.getDayObject}
                />

            </div>
        );
    };
}

export default TeacherInput;
