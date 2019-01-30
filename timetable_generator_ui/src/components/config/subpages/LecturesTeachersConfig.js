import React, {Component} from 'react';
import './lecturesTeachersConfig.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Button} from "react-bootstrap";
import Select from 'react-select';
import {retrieveAllStudentClasses} from '../../services/StudentClassService';
import {retrieveAllLectures} from '../../services/LectureService';
import {addAssignmentsToConfig} from '../../services/ConfigService';

class LecturesTeachersConfig extends Component {

    constructor(props) {
        super(props);

        this.state = {
            studentClasses: [],
            lectures: [],
            teachers: [],

            selectedStudentClasses: null,
            selectedLecture: null,
            selectedTeacher: null,
            selectedNumberOfSessions: '',
            selections: []
        };

        this.handleChangeStudentClasses = this.handleChangeStudentClasses.bind(this);
        this.handleChangeLecture = this.handleChangeLecture.bind(this);
        this.handleChangeTeacher = this.handleChangeTeacher.bind(this);
        this.addAssignation = this.addAssignation.bind(this);
        this.saveAndContinue = this.saveAndContinue.bind(this);
        this.handleChangeNumberOfSessions = this.handleChangeNumberOfSessions.bind(this);
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

        console.log(entries);

        addAssignmentsToConfig(this.props.configId,entries);
        this.props.enableNext();
    }

    render() {
        const {selectedStudentClasses, studentClasses, selectedLecture, lectures, selectedTeacher, teachers} = this.state;

        return (
            <div>
                <div className={"selectArea"}>
                    <label>Select student</label>
                    <Select
                        value={selectedStudentClasses}
                        onChange={this.handleChangeStudentClasses}
                        options={studentClasses}
                        isMulti={true}
                        isSearchable={true}
                        closeMenuOnSelect={false}
                    />
                </div>
                <div className={"selectArea"}>
                    <label>Select lecture</label>
                    <Select
                        value={selectedLecture}
                        onChange={this.handleChangeLecture}
                        options={lectures}
                        isSearchable={true}
                    />
                </div>
                <div className={"selectArea"}>
                    <label>Select teacher</label>
                    <Select
                        value={selectedTeacher}
                        onChange={this.handleChangeTeacher}
                        options={teachers}
                        isDisabled={teachers.length === 0}
                        isSearchable={true}
                        placeholder={"Decide automatically..."}
                    />
                </div>
                <div>
                    Number of sessions
                    <input type="number" value={this.state.selectedNumberOfSessions}
                           onChange={this.handleChangeNumberOfSessions}/>
                </div>
                <div>
                    <Button bsStyle="primary" onClick={this.addAssignation}>Add assignation</Button>
                </div>
                <Button bsStyle="primary" onClick={this.saveAndContinue}>Next</Button>
            </div>
        );
    };
}

export default LecturesTeachersConfig;
