import React, {Component} from 'react';
import './configTimetable.css';
import {Button, FieldGroup, Table} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import Select from 'react-select';
import {retrieveAllStudentClasses} from '../services/StudentClassService';
import {retrieveAllLectures} from '../services/LectureService';
import {addAssignmentsToConfig, saveConfig} from '../services/ConfigService';
import Timeslot from "../view/common/StudentTable";

class ConfigTimetable extends Component {

    constructor(props) {
        super(props);

        this.state = {
            configId: null,
            studentClasses: [],
            lectures: [],
            teachers: [],

            selectedStudentClasses: null,
            selectedLecture: null,
            selectedTeacher: null,
            selectedNumberOfSessions: '1',
            selections: []
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleChangeStudentClasses = this.handleChangeStudentClasses.bind(this);
        this.handleChangeLecture = this.handleChangeLecture.bind(this);
        this.handleChangeTeacher = this.handleChangeTeacher.bind(this);
        this.addAssignation = this.addAssignation.bind(this);
        this.saveAndContinue = this.saveAndContinue.bind(this);
        this.handleChangeNumberOfSessions = this.handleChangeNumberOfSessions.bind(this);
        this.removeElement = this.removeElement.bind(this);
    }

    componentDidMount() {
        retrieveAllStudentClasses()
            .then(response => {
                let students = response.map(studentClass => {
                    return {
                        value: studentClass.id,
                        label: studentClass.name
                    }
                });
                this.setState({studentClasses: students});
            })
            .catch(error => {
                console.error(error);
            });

        retrieveAllLectures()
            .then(response => {
                let lectures = response.map(lecture => {
                    console.log(lecture);
                    return {
                        value: lecture.id,
                        label: lecture.name,
                        teachers: lecture.teachers.map(teacher => {
                            return {
                                value: teacher.id,
                                label: teacher.name
                            }
                        })
                    }
                });
                this.setState({lectures: lectures});
            })
    }

    handleChangeStudentClasses = (selectedStudent) => {
        this.setState({selectedStudentClasses: selectedStudent});
    };

    handleChangeLecture = (selectedLecture) => {
        this.setState(
            {
                selectedLecture: selectedLecture,
                teachers: selectedLecture.teachers
            }
        );
    };

    handleChangeTeacher = (selectedTeacher) => {
        this.setState({selectedTeacher: selectedTeacher});
    };

    handleChangeNumberOfSessions = (e) => {
        this.setState({selectedNumberOfSessions: e.target.value});
    };

    handleChange = (e) => {
        this.setState({configName: e.target.value});
    };

    addAssignation() {
        let selectedStudentClasses = this.state.selectedStudentClasses;
        let selectedLecture = this.state.selectedLecture;
        let selectedTeacher = this.state.selectedTeacher;
        let selectedNumberOfSessions = this.state.selectedNumberOfSessions;

        let item = {
            studentClasses: selectedStudentClasses,
            lecture: selectedLecture,
            teacher: selectedTeacher,
            numberOfSessions: selectedNumberOfSessions
        };

        this.setState({
            selections: [...this.state.selections, item],
            selectedStudentClasses: null,
            selectedLecture: null,
            selectedTeacher: null,
            selectedNumberOfSessions: ''
        });
    }

    saveAndContinue() {
        let entries = this.state.selections.map(selection => {
            return selection.studentClasses.map(studentClass => {
                return {
                    studentClassId: studentClass.value,
                    lectureId: selection.lecture.value,
                    teacherId: selection.teacher ? selection.teacher.value : null,
                    numberOfSessions: selection.numberOfSessions
                }
            })
        }).flat();

        saveConfig({name: this.state.configName})
            .then(result => {
                this.setState({configId: result.id})
                return result.id;
            })
            .then(id => {
                addAssignmentsToConfig(id, entries);
            });

        console.log(entries);
    }

    render() {
        const {selectedStudentClasses, studentClasses, selectedLecture, lectures, selectedTeacher, teachers} = this.state;

        return (
            <div className="timetable">
				<div className="select-area-content">
					<div className="config-name row">
						<span>Config name</span>
						<input className="select-field" type="text" value={this.state.configName} onChange={this.handleChange}/>
					</div>
					<div className="row">
						<span>Select student</span>
						<Select
							className="select-field"
							value={selectedStudentClasses}
							onChange={this.handleChangeStudentClasses}
							options={studentClasses}
							isMulti={true}
							isSearchable={true}
							closeMenuOnSelect={false}
						/>
					</div>
					<div className="row">
						<span>Select lecture</span>
						<Select
							className="select-field"
							value={selectedLecture}
							onChange={this.handleChangeLecture}
							options={lectures}
							isSearchable={true}
						/>
					</div>
					<div className="row">
						<span>Select teacher</span>
						<Select
							className="select-field"
							value={selectedTeacher}
							onChange={this.handleChangeTeacher}
							options={teachers}
							isDisabled={teachers.length === 0}
							isSearchable={true}
							placeholder={"Decide automatically..."}
						/>
					</div>
					<div className="row">
						<span>Number of sessions</span>
						<input type="number"
							min="1"
							value={this.state.selectedNumberOfSessions}
							className="select-field"
							onChange={this.handleChangeNumberOfSessions} />
					</div>
				</div>

				<Button className="generate-btn" bsStyle="primary" onClick={this.addAssignation}>Add assignation</Button>

				{this.state.selections && this.state.selections.length > 0 &&
					<Table bordered condensed hover responsive className="Table">
						<tbody>
						<tr className="MainHeader">
							<th>Students</th>
							<th>Lecture</th>
							<th>Teacher</th>
							<th>Number of sessions</th>
							<th>Remove</th>
						</tr>
						{
							this.state.selections.map((selection, index) => {
								return <tr key={index}>
									<th>{selection.studentClasses.map(s => s.label).join(", ")}</th>
									<th>{selection.lecture.label}</th>
									<th>{selection.teacher ? selection.teacher.label : 'Let the app decide.'}</th>
									<th>{selection.numberOfSessions}</th>
									<th><Button bsStyle="primary"
												onClick={() => this.removeElement(selection)}>Remove</Button></th>
								</tr>
							})
						}

						</tbody>
					</Table>
				}
				<div className="generate-btn-content">
                	<Button className="generate-btn" bsStyle="primary" onClick={this.saveAndContinue}>Generate timetable</Button>
				</div>
            </div>
        );
    };

    removeElement(selection) {
        this.setState({
            selections: this.state.selections.filter(function (sel) {
                return sel !== selection
            })
        });
        return undefined;
    }
}

export default ConfigTimetable;
