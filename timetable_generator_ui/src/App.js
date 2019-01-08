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
				dataTwo:[
					1,2,3,4
				]
		};

		this.retrieveTimetable = this.retrieveTimetable.bind(this);
		this.displayTimetable = this.displayTimetable.bind(this);
	}

	displayTimetable(){
		console.log(this.state.data)
		this.setState({ dataTwo: this.state.data.studentClassOutputs })
	}

	retrieveTimetable() {
		console.log("Hello world!");
		fetch('http://localhost:8090/timetable', {method: 'POST', body: {}})
		.then(response => response.json())
		.then(data => {
			console.log(data);
			this.setState({ data })
		});
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
				<button onClick={this.retrieveTimetable}>Get timetable</button>
				<button onClick={this.displayTimetable}>Display timetable</button>

				<ReactTable
					data={data}
					columns={columns}
					defaultPageSize={5}
					className="-striped -highlight"
				/>

				<br />

				{this.state.dataTwo.map((data, index) => (
					<div key={index}>{data}</div>
				))}

			</div>
		);
	};
}


export default App;
