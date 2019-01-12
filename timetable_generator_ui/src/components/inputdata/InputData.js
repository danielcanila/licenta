import React, {Component} from 'react';
import './inputData.css';
import {Col, Nav, NavItem, Row, Tab} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import RoomInput from './subpages/RoomInput';
import StudentClassInput from './subpages/StudentClassInput';
import LectureInput from './subpages/LectureInput';
import TeacherInput from './subpages/TeacherInput';

class InputData extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Tab.Container defaultActiveKey="first" id="input-data-container">
                <Row className="clearfix">
                    <Col sm={2}>
                        <Nav bsStyle="pills" stacked>
                            <NavItem eventKey="first">Rooms</NavItem>
                            <NavItem eventKey="second">Student classes</NavItem>
                            <NavItem eventKey="third">Lectures</NavItem>
                            <NavItem eventKey="forth">Teachers</NavItem>
                        </Nav>
                    </Col>
                    <Col sm={10}>
                        <Tab.Content animation>
                            <Tab.Pane eventKey="first"><RoomInput/></Tab.Pane>
                            <Tab.Pane eventKey="second"><StudentClassInput/></Tab.Pane>
                            <Tab.Pane eventKey="third"><LectureInput/></Tab.Pane>
                            <Tab.Pane eventKey="forth"><TeacherInput/></Tab.Pane>
                        </Tab.Content>
                    </Col>
                </Row>
            </Tab.Container>
        );
    };

}

export default InputData;
