import React, { Component } from 'react';
import './App.css';
import ReactTable from "react-table";
import 'react-table/react-table.css'
import { render } from "react-dom";

class App extends Component {


 constructor(props) {
    super(props);

    this.state = {
        data:  null,
        dataTwo:[]
    };

    this.retrieveTimetable = this.retrieveTimetable.bind(this);
    this.displayTimetable = this.displayTimetable.bind(this);
  }

  render() {
    const columns = [
                      {
                        Header: "Name",
                        accessor: "name"
                      },
                      {
                        Header: "Age",
                        accessor: "numberOfStudents"
                      }

                    ]

    const data  = this.state.dataTwo;

   return (


      <div className="App">
           <button onClick={this.retrieveTimetable}>
                  Get timetable
            </button>
            <button onClick={this.displayTimetable}>
            Display timetable
            </button>
              <ReactTable
                data={data}
                columns={columns}
                defaultPageSize={5}
                className="-striped -highlight"
              />
              <br />
           </div>
    );
  }

  displayTimetable(){
  console.log(this.state.data)
  this.setState({ dataTwo: this.state.data.studentClassOutputs })
  }

  retrieveTimetable() {
     console.log("Hello world!");
       fetch('http://localhost:8090/timetable')
           .then(response => response.json())
           .then(data => this.setState({ data }));}
}


export default App;
