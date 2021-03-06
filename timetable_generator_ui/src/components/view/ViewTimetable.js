import React, {Component} from 'react';
import './viewTimetable.css';
import {Col, Nav, NavItem, Row, Tab} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import StudentTimetableView from "./subpages/StudentTimetableView";
import TeacherTimetableView from "./subpages/TeacherTimetableView";

class ViewTimetable extends Component {
    render() {
        return (
            <div id="timetable">
                <Tab.Container defaultActiveKey="first" id="timetable-view">
                    <Row className="clearfix">
                        <Col sm={2}>
                            <Nav bsStyle="pills" stacked>
                                <NavItem eventKey="first">Student timetable</NavItem>
                                <NavItem eventKey="second">Teacher timetable</NavItem>
                            </Nav>
                        </Col>
                        <Col sm={10}>
                            <Tab.Content animation>
                                <Tab.Pane eventKey="first"><StudentTimetableView/></Tab.Pane>
                                <Tab.Pane eventKey="second"><TeacherTimetableView/></Tab.Pane>
                            </Tab.Content>
                        </Col>
                    </Row>
                </Tab.Container>
            </div>
        );
    };
}

export default ViewTimetable;
