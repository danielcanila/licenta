import React, {Component} from 'react';
import './inputData.css';
import {Col, Nav, NavItem, Row, Tab} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import StudentClassInput from './subpages/StudentClassInput';
import LectureInput from './subpages/LectureInput';
import TeacherInput from './subpages/TeacherInput';

class InputData extends Component {

    render() {
        return (
            <Tab.Container defaultActiveKey="first" id="input-data-container">
                <Row className="clearfix">
                    <Col sm={2}>
                        <Nav bsStyle="pills" stacked>
                            <NavItem eventKey="first">Student classes</NavItem>
                            <NavItem eventKey="second">Lectures</NavItem>
                            <NavItem eventKey="third">Teachers</NavItem>
                        </Nav>
                    </Col>
                    <Col sm={10}>
                        <Tab.Content animation>
                            <Tab.Pane eventKey="first" mountOnEnter={true} unmountOnExit={true}>
                                <StudentClassInput />
                            </Tab.Pane>
                            <Tab.Pane eventKey="second" mountOnEnter={true} unmountOnExit={true}>
                                <LectureInput />
                            </Tab.Pane>
                            <Tab.Pane eventKey="third" mountOnEnter={true} unmountOnExit={true}>
                                <TeacherInput />
                            </Tab.Pane>
                        </Tab.Content>
                    </Col>
                </Row>
            </Tab.Container>
        );
    };

}

export default InputData;
