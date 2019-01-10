import React, {Component} from 'react';
import './ViewClassTimetable.css';
import axios from 'axios';
import {Table} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

class ViewClassTimetable extends Component {

    constructor(props) {
        super(props);
        this.state = {
            reservations: null,
            slots: {
                0: "08:00",
                1: "10:00",
                2: "12:00",
                3: "14:00",
                4: "16:00",
                5: "18:00",
                6: "20:00"
            },
            days: {
                0: "Luni",
                1: "Marti",
                2: "Miercuri",
                3: "Joi",
                4: "Vineri"
            }
        };
        this.retrieveTimetable = this.retrieveTimetable.bind(this);
        this.createTableContent = this.createTableContent.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
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
                console.log(data);
                this.setState({reservations: data})
            });

    }

    createTable() {
        return <Table  bordered condensed hover responsive className="Table">
            <tbody>
            {this.createMainHeader()}
            {this.createTableContent()}
            </tbody>
        </Table>;
    }

    createMainHeader() {
        return <tr className="MainHeader">
            <th>De la</th>
            <th>Pana la</th>
            <th>Disciplina</th>
            <th>Profesor</th>
            <th>Sala</th>
            <th>Capacitate</th>
        </tr>;
    }

    createTableContent() {
        let table = []
        for (let key of Object.keys(this.state.reservations)) {
            table.push(<tr className="SubHeader">
                <th colSpan={10}>{this.state.days[key]}</th>
            </tr>)
            for (let reservation of this.state.reservations[key]) {
                table.push(<tr>
                    <th>{this.state.slots[reservation.slot]}</th>
                    <th>{this.state.slots[reservation.slot + 1]}</th>
                    <th>{reservation.lectureName}</th>
                    <th>{reservation.teacherName}</th>
                    <th>{reservation.roomName}</th>
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

export default ViewClassTimetable;
