import React, {Component} from 'react';
import './viewTeacherTimetable.css';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import SelectTimetableDropdown from '../../common/SelectTimetableDropdown'
import TeacherTable from '../../common/TeacherTable'

class ViewTeacherTimetable extends Component {

    constructor(props) {
        super(props);
        this.state = {
            reservations: null,
            timetables: null,
            teachers: null,
            selectedTimetable: null,
            selectedTeacher: null
        };

        this.retrieveAllTimetables();
        this.retrieveTimetable = this.retrieveTimetable.bind(this);
        this.handleTeacherSelect = this.handleTeacherSelect.bind(this);
        this.retrieveAllTimetables = this.retrieveAllTimetables.bind(this);
        this.handleTimetableSelect = this.handleTimetableSelect.bind(this);
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
                console.log(data);
                this.setState({reservations: data})
            });
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
                console.log(data);
                this.setState({timetables: data})
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
                console.log(data);
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
            selectedTeacher: key
        });
        this.retrieveTimetable(this.state.selectedTimetable.id, key.id)
    }

    render() {
        return (
            <div className="App">
                <SelectTimetableDropdown selectItems={this.state.timetables}
                                         handleOnChange={this.handleTimetableSelect}
                                         selectedItem={this.state.selectedTimetable}
                                         elementName={"timetable"}/>

                <SelectTimetableDropdown selectItems={this.state.teachers}
                                         handleOnChange={this.handleTeacherSelect}
                                         selectedItem={this.state.selectedTeacher}
                                         elementName={"teacher"}/>

                <TeacherTable reservations={this.state.reservations}/>
            </div>
        )

    };
}

export default ViewTeacherTimetable;
