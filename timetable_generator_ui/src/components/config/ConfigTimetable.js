import React, {Component} from 'react';
import './configTimetable.css';
import {Col, Nav, NavItem, Row, Tab} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import CreateConfig from './subpages/CreateConfig'
import StudentClassesConfig from './subpages/StudentClassesConfig'
import LecturesTeachersConfig from './subpages/LecturesTeachersConfig'
import GenerateTimetable from './subpages/GenerateTimetable'
import {
    retrieveLatestConfig,
    saveConfig
} from '../services/ConfigService';


class ConfigTimetable extends Component {


    constructor(props) {
        super(props);
        this.state = {
            activateTab: "first",
            studentConfigDisabled: true,
            lectureTeacherDisabled: true,
            generateTimetableDisabled: true,
            currentConfigId: null
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
        retrieveLatestConfig()
            .then(response => {
                this.setState({
                    currentConfigId: response.id
                });
            });
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
        return <Tab.Container id="tc-config-timetable" activeKey={this.state.activateTab} onSelect={() => {
        }}>
            <Row className="clearfix">
                <Col sm={2}>
                    <Nav bsStyle="pills" stacked>
                        <NavItem onClick={this.navigationStepOne} eventKey="first">Create config</NavItem>
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
                            <CreateConfig enableNext={this.navigationStepTwo}/>
                        </Tab.Pane>
                        <Tab.Pane eventKey="second">
                            <StudentClassesConfig enableNext={this.navigationStepThird}
                                                  configId={this.state.currentConfigId}/>
                        </Tab.Pane>
                        <Tab.Pane eventKey="third">
                            <LecturesTeachersConfig enableNext={this.navigationStepForth}
                                                    configId={this.state.currentConfigId}/>
                        </Tab.Pane>
                        <Tab.Pane eventKey="forth">
                            <GenerateTimetable enableNext={this.navigationStepForth}
                                               configId={this.state.currentConfigId}/>
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
