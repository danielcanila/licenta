import React, {Component} from 'react';
import {Table} from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import Day from './Day'
import Timeslot from './Timeslot'

class StudentTable extends Component {

    createTableContent(reservations) {
        let table = []
        for (let key of Object.keys(reservations)) {
            table.push(<tr className="SubHeader">
                <th colSpan={10}>{<Day day={key}/>}</th>
            </tr>)
            for (let reservation of reservations[key]) {
                table.push(<tr>
                    <th><Timeslot timeslot={reservation.slot}/></th>
                    <th><Timeslot timeslot={reservation.slot + 1}/></th>
                    <th>{reservation.lectureName}</th>
                    <th>{reservation.teacherName}</th>
                    <th>{reservation.roomCapacity}</th>
                </tr>)
            }
        }
        return table;
    }

    render() {
        let {reservations} = this.props;
        if(!reservations) return null;

        return (
            <div className="student-table">
                <Table bordered condensed hover responsive className="Table">
                    <tbody>
                        <tr className="MainHeader">
                            <th>From</th>
                            <th>To</th>
                            <th>Lecture</th>
                            <th>Teacher</th>
                            <th>Capacity</th>
                        </tr>
                    {this.createTableContent(reservations)}
                    </tbody>
                </Table>
            </div>
        );
    };
}

export default StudentTable;
