import React, {Component} from 'react';
import './viewTimetable.css';
import {Col, Nav, NavItem, Row, Tab} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import StudentTimetableView from "./subpages/StudentTimetableView";
import TeacherTimetableView from "./subpages/TeacherTimetableView";
import RoomTimetableView from "./subpages/RoomTimetableView";

class ViewTimetable extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Tab.Container defaultActiveKey="first" id="timetable-view">
                <Row className="clearfix">
                    <Col sm={2}>
                        <Nav bsStyle="pills" stacked>
                            <NavItem eventKey="first">Student timetable</NavItem>
                            <NavItem eventKey="second">Teacher timetable</NavItem>
                            <NavItem eventKey="third">Room timetable</NavItem>
                        </Nav>
                    </Col>
                    <Col sm={10}>
                        <Tab.Content animation>
                            <Tab.Pane eventKey="first"><StudentTimetableView/></Tab.Pane>
                            <Tab.Pane eventKey="second"><TeacherTimetableView/></Tab.Pane>
                            <Tab.Pane eventKey="third"><RoomTimetableView/></Tab.Pane>
                        </Tab.Content>
                    </Col>
                </Row>
            </Tab.Container>
        );
    };
}

export default ViewTimetable;
