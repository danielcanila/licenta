import React, {Component} from 'react';
import './viewTimetable.css';
import {Col, Nav, NavItem, Row, Tab} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import ViewClassTimetable from "./subpages/ViewClassTimetable";

class ViewTimetable extends Component {

    TabDateIntrare() {
        return <Tab.Container defaultActiveKey="first">
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
                        <Tab.Pane eventKey="first"><ViewClassTimetable/></Tab.Pane>
                        <Tab.Pane eventKey="second">Teacher timetable</Tab.Pane>
                        <Tab.Pane eventKey="third">Room timetable</Tab.Pane>
                    </Tab.Content>
                </Col>
            </Row>
        </Tab.Container>;
    }

    render() {
        return (
            this.TabDateIntrare()
        );
    };

}

export default ViewTimetable;
