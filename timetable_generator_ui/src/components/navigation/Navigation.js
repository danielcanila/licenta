import React, {Component} from 'react';
import './navigation.css';
import {Nav, Navbar, NavItem} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import {LinkContainer} from 'react-router-bootstrap';

class Navigation extends Component {

    constructor(props) {
        super(props);
        this.state = {
            activePage: 1
        };
    }

    handleSelect(selectedKey) {
        this.setState({activePage: selectedKey});
    }

    render() {
        return (
            <Navbar>
                <Nav activeKey={this.state.activePage} onSelect={val => this.handleSelect(val)}>
                    <LinkContainer to="/home">
                        <NavItem eventKey="1">
                            Home
                        </NavItem>
                    </LinkContainer>
                    <LinkContainer to="/inputData">
                        <NavItem eventKey="2">
                            Input data
                        </NavItem>
                    </LinkContainer>
                    <LinkContainer to="/timetableGeneration">
                        <NavItem eventKey="3">
                            Timetable generation
                        </NavItem>
                    </LinkContainer>
                    <LinkContainer to="/viewTimetable">
                        <NavItem eventKey="4">
                            Timetable view
                        </NavItem>
                    </LinkContainer>
                </Nav>
            </Navbar>
        );
    };
}

export default Navigation;
