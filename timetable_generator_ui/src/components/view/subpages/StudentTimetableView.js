import React, {Component} from 'react';
import './studentTimetableView.css';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import StudentTable from "../common/StudentTable";
import SelectDropdown from '../common/CustomDropdown'

class StudentTimetableView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            reservations: null,
            timetables: null,
            selectedTimetable: null,
            classes: null,
            selectedClass: null,
        };
        this.retrieveAllTimetables();
        this.retrieveTimetable = this.retrieveTimetable.bind(this);
        this.retrieveAllClases = this.retrieveAllClases.bind(this);
        this.handleTimetableSelect = this.handleTimetableSelect.bind(this);
        this.handleClassSelect = this.handleClassSelect.bind(this);
    }

    retrieveTimetable(resultId, classId) {
        let url = 'http://localhost:8090/timetable/' + resultId + '/class/' + classId;
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

    retrieveAllClases(resultId) {
        let url = 'http://localhost:8090/timetable/' + resultId + '/class';
        let config = {
            headers: {
                'Access-Control-Allow-Origin': '*',
            }
        };
        axios.get(url, config)
            .then(response => response.data)
            .then(data => {
                this.setState({classes: data})
            });
    }

    handleTimetableSelect(key) {
        this.setState({
                selectedTimetable: key
            }
        );
        this.retrieveAllClases(key.id);
    }

    handleClassSelect(key) {
        this.setState({
            selectedClass: {...key}
        });
        this.retrieveTimetable(this.state.selectedTimetable.id, key.id)
    }


    render() {
        return (
            <div className="App">
                <SelectDropdown key={'01'}
                                selectItems={this.state.timetables}
                                handleOnChange={this.handleTimetableSelect}
                                selectedItem={this.state.selectedTimetable}
                                defaultName={"timetable"}/>

                <SelectDropdown key={'02'}
                                selectItems={this.state.classes}
                                handleOnChange={this.handleClassSelect}
                                selectedItem={this.state.selectedClass}
                                defaultName={"class"}/>

                <StudentTable reservations={this.state.reservations}/>
            </div>
        );
    };


}

export default StudentTimetableView;
