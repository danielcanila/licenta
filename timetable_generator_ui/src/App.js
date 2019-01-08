import React, {Component} from 'react';
import './App.css';
import ReactTable from "react-table";
import 'react-table/react-table.css'
import {render} from "react-dom";

class App extends Component {

    constructor(props) {
        super(props);

        this.state = {
            data: null,
            students: []
        };

        this.retrieveTimetable = this.retrieveTimetable.bind(this);
        this.displayTimetable = this.displayTimetable.bind(this);
    }

    displayTimetable() {
        console.log(this.state.data);
        this.setState({students: this.state.data && this.state.data.studentClassOutputs});
    }

    retrieveTimetable() {
        console.log("Hello world!");
        fetch('http://localhost:8090/timetable')
            .then(response => response.json())
            .then(data => {
                console.log(data);
                this.setState({data})
            });
    }

    render() {

        return (
            <div className="App">
                <button onClick={this.retrieveTimetable}>Get timetable</button>
                <button onClick={this.displayTimetable}>Display timetable</button>

                <br/>

                {this.state.students && this.state.students.length > 0 &&
                this.state.students.map((studentClass, index) => (
                    <div key={index}>
                        <div>This is my name: {studentClass.name}</div>
                    </div>
                ))}

            </div>
        );
    };
}


export default App;
