import React, {Component} from 'react';
import {Table} from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import Day from './Day'
import Timeslot from './Timeslot'

class TeacherTable extends Component {

    createTable(reservations) {
        if (!reservations) {
            return <div></div>
        }
        return <Table bordered condensed hover responsive className="Table">
            <tbody>
            {this.createMainHeader()}
            {this.createTableContent(reservations)}
            </tbody>
        </Table>
    }

    createMainHeader() {
        return <tr className="MainHeader">
            <th>From</th>
            <th>To</th>
            <th>Lecture</th>
            <th>Student class</th>
            <th>Capacity</th>
        </tr>
    }

    createTableContent(reservations) {
        let table = [];
        for (let key of Object.keys(reservations)) {
            table.push(<tr className="SubHeader">
                <th colSpan={10}>{<Day day={key}/>}</th>
            </tr>);
            for (let reservation of reservations[key]) {
                table.push(<tr>
                    <th><Timeslot timeslot={reservation.slot}/></th>
                    <th><Timeslot timeslot={reservation.slot + 1}/></th>
                    <th>{reservation.lectureName}</th>
                    <th>{reservation.studentClassName}</th>
                    <th>{reservation.roomCapacity}</th>
                </tr>)
            }
        }
        return table
    }

    render() {
        return this.createTable(this.props.reservations)
    };
}

export default TeacherTable;
