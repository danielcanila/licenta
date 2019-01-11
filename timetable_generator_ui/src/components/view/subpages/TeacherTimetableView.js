import React, {Component} from 'react';
import './teacherTimetableView.css';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import SelectDropdown from '../common/CustomDropdown'
import TeacherTable from '../common/TeacherTable'

class TeacherTimetableView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            timetables: null,
            selectedTimetable: null,
            teachers: null,
            selectedTeacher: null,
            reservations: null
        };

        this.retrieveAllTimetables();
        this.retrieveAllTimetables = this.retrieveAllTimetables.bind(this);
        this.retrieveTimetable = this.retrieveTimetable.bind(this);
        this.handleTimetableSelect = this.handleTimetableSelect.bind(this);
        this.handleTeacherSelect = this.handleTeacherSelect.bind(this);
    }

    retrieveAllTimetables() {
        let url = 'http://localhost:8090/timetable';
        let config = {
            headers: {
                'Access-Control-Allow-Origin': '*',
            }
        };
        axios.get(url, config)
            .then(response => response.data)
            .then(data => {
                this.setState({timetables: data})
            });
    }

    retrieveTimetable(resultId, teacherId) {
        let url = 'http://localhost:8090/timetable/' + resultId + '/teacher/' + teacherId;
        let config = {
            headers: {
                'Access-Control-Allow-Origin': '*',
            }
        };
        axios.get(url, config)
            .then(response => response.data)
            .then(data => {
                this.setState({reservations: data})
            });
    }

    retrieveAllTeachers(resultId) {
        let url = 'http://localhost:8090/timetable/' + resultId + '/teacher';
        let config = {
            headers: {
                'Access-Control-Allow-Origin': '*',
            }
        };
        axios.get(url, config)
            .then(response => response.data)
            .then(data => {
                this.setState({teachers: data})
            });
    }

    handleTimetableSelect(key) {
        this.setState({
                selectedTimetable: key
            }
        );
        this.retrieveAllTeachers(key.id);
    }

    handleTeacherSelect(key) {
        this.setState({
            selectedTeacher: {...key}
        });
        this.retrieveTimetable(this.state.selectedTimetable.id, key.id)
    }

    render() {
        return (
            <div className="Teacher">
                <SelectDropdown selectItems={this.state.timetables}
                                handleOnChange={this.handleTimetableSelect}
                                selectedItem={this.state.selectedTimetable}
                                defaultName={"timetable"}/>

                <SelectDropdown selectItems={this.state.teachers}
                                handleOnChange={this.handleTeacherSelect}
                                selectedItem={this.state.selectedTeacher}
                                defaultName={"teacher"}/>

                <TeacherTable reservations={this.state.reservations}/>
            </div>
        )

    };
}

export default TeacherTimetableView;
