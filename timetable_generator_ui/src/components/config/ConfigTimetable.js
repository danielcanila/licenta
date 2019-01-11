import React, {Component} from 'react';
import './configTimetable.css';
import {Col, Nav, NavItem, Row, Tab} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import RoomConfig from './subpages/RoomConfig'
import StudentClassesConfig from './subpages/StudentClassesConfig'
import LecturesTeachersConfig from './subpages/LecturesTeachersConfig'
import GenerateTimetable from './subpages/GenerateTimetable'

class ConfigTimetable extends Component {


    constructor(props) {
        super(props);
        this.state = {
            activateTab: "first",
            studentConfigDisabled: true,
            lectureTeacherDisabled: true,
            generateTimetableDisabled: true
        };

        this.navigationStepOne = this.navigationStepOne.bind(this);
        this.navigationStepTwo = this.navigationStepTwo.bind(this);
        this.navigationStepThird = this.navigationStepThird.bind(this);
        this.navigationStepForth = this.navigationStepForth.bind(this);
    }

    navigationStepOne() {
        this.setState({
            activateTab: "first",
            studentConfigDisabled: true,
            lectureTeacherDisabled: true,
            generateTimetableDisabled: true
        })
    }

    navigationStepTwo() {
        this.setState({
            activateTab: "second",
            studentConfigDisabled: false,
            lectureTeacherDisabled: true,
            generateTimetableDisabled: true
        })
    }

    navigationStepThird() {
        this.setState({
            activateTab: "third",
            studentConfigDisabled: false,
            lectureTeacherDisabled: false,
            generateTimetableDisabled: true
        })
    }

    navigationStepForth() {
        this.setState({
            activateTab: "forth",
            studentConfigDisabled: false,
            lectureTeacherDisabled: false,
            generateTimetableDisabled: false
        })
    }

    TabConfigurareOrar() {
        return <Tab.Container activeKey={this.state.activateTab}>
            <Row className="clearfix">
                <Col sm={2}>
                    <Nav bsStyle="pills" stacked>
                        <NavItem onClick={this.navigationStepOne} eventKey="first">Rooms</NavItem>
                        <NavItem onClick={this.navigationStepTwo} disabled={this.state.studentConfigDisabled}
                                 eventKey="second">Student classes</NavItem>
                        <NavItem onClick={this.navigationStepThird} disabled={this.state.lectureTeacherDisabled}
                                 eventKey="third">Lectures /
                            Teachers</NavItem>
                        <NavItem onClick={this.navigationStepForth} disabled={this.state.generateTimetableDisabled}
                                 eventKey="forth">Generate
                            timetable</NavItem>
                    </Nav>
                </Col>
                <Col sm={10}>
                    <Tab.Content animation>
                        <Tab.Pane eventKey="first">
                            <RoomConfig enableNext={this.navigationStepTwo}/>
                        </Tab.Pane>
                        <Tab.Pane eventKey="second">
                            <StudentClassesConfig enableNext={this.navigationStepThird}/>
                        </Tab.Pane>
                        <Tab.Pane eventKey="third">
                            <LecturesTeachersConfig enableNext={this.navigationStepForth}/>
                        </Tab.Pane>
                        <Tab.Pane eventKey="forth">
                            <GenerateTimetable enableNext={this.navigationStepForth}/>
                        </Tab.Pane>
                    </Tab.Content>
                </Col>
            </Row>
        </Tab.Container>;
    }

    render() {
        return (
            this.TabConfigurareOrar()
        );
    };

}

export default ConfigTimetable;
