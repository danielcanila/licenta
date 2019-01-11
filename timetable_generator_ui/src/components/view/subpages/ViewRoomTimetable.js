import React, {Component} from 'react';
import './viewRoomTimetable.css';
import axios from 'axios';
import {Table} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import Day from '../../common/Day'
import Timeslot from '../../common/Timeslot'

class ViewRoomTimetable extends Component {

    constructor(props) {
        super(props);
        this.state = {
            reservations: null,
        };
        this.retrieveTimetable = this.retrieveTimetable.bind(this);
        this.createTableContent = this.createTableContent.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
    }

    retrieveTimetable(resultId, roomId) {
        let url = 'http://localhost:8090/timetable/' + resultId + '/room/' + roomId;
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

    createTable() {
        return <Table bordered condensed hover responsive className="Table">
            <tbody>
            {this.createMainHeader()}
            {this.createTableContent()}
            </tbody>
        </Table>
    }

    createMainHeader() {
        return <tr className="MainHeader">
            <th>From</th>
            <th>To</th>
            <th>Lecture</th>
            <th>Teacher</th>
            <th>Student class</th>
            <th>Capacity</th>
        </tr>
    }

    createTableContent() {
        let table = []
        for (let key of Object.keys(this.state.reservations)) {
            table.push(<tr className="SubHeader">
                <th colSpan={10}>{<Day day={key}/>}</th>
            </tr>)
            for (let reservation of this.state.reservations[key]) {
                table.push(<tr>
                    <th><Timeslot timeslot={reservation.slot}/></th>
                    <th><Timeslot timeslot={reservation.slot + 1}/></th>
                    <th>{reservation.lectureName}</th>
                    <th>{reservation.teacherName}</th>
                    <th>{reservation.studentClassName}</th>
                    <th>{reservation.roomCapacity}</th>
                </tr>)
            }
        }
        return table
    }

    handleInputChange(event) {
        if (event.target.value) {
            this.retrieveTimetable(352, event.target.value)
        }
    }

    render() {
        return (
            <div className="App">
                <div>
                    <input type="text" onChange={this.handleInputChange}/>
                </div>
                <div>
                    <br/>
                    {this.state.reservations && this.createTable()}
                </div>
            </div>
        );
    };


}

export default ViewRoomTimetable;
