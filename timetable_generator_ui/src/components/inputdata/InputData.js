import React, {Component} from 'react';
import './inputData.css';
import {Col, Nav, NavItem, Row, Tab} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

class InputData extends Component {

    TabDateIntrare() {
        return <Tab.Container defaultActiveKey="first">
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
                        <Tab.Pane eventKey="first">Add/Edit/Delete rooms</Tab.Pane>
                        <Tab.Pane eventKey="second">Add/Edit/Delete student classes</Tab.Pane>
                        <Tab.Pane eventKey="third">Add/Edit/Delete lectures</Tab.Pane>
                        <Tab.Pane eventKey="forth">Add/Edit/Delete teachers</Tab.Pane>
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

export default InputData;
