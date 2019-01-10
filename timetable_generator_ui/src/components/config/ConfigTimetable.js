import React, {Component} from 'react';
import './configTimetable.css';
import {Col, Nav, NavItem, Row, Tab} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

class ConfigTimetable extends Component {

    TabConfigurareOrar() {
        return <Tab.Container defaultActiveKey="first">
            <Row className="clearfix">
                <Col sm={2}>
                    <Nav bsStyle="pills" stacked>
                        <NavItem eventKey="first">Rooms</NavItem>
                        <NavItem eventKey="second">Assign student classes</NavItem>
                        <NavItem eventKey="third">Assign teachers/lectures</NavItem>
                        <NavItem eventKey="forth">Generate timetable</NavItem>
                    </Nav>
                </Col>
                <Col sm={10}>
                    <Tab.Content animation>
                        <Tab.Pane eventKey="first">Adaugare/Editare/Stergere grupe</Tab.Pane>
                        <Tab.Pane eventKey="second">Adaugare/Editare/Stergere Materii</Tab.Pane>
                        <Tab.Pane eventKey="third">Adaugare/Editare/Stergere profesori</Tab.Pane>
                        <Tab.Pane eventKey="forth">Adaugare/Editare/Stergere profesori</Tab.Pane>
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
