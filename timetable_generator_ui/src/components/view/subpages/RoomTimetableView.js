import React, {Component} from 'react';
import './roomTimetableView.css';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import SelectDropdown from '../common/CustomDropdown'
import RoomTable from "../common/RoomTable";

class RoomTimetableView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            reservations: null,
            timetables: null,
            selectedTimetable: null,
            rooms: null,
            selectedRoom: null,
        };
        this.retrieveAllTimetables();
        this.retrieveTimetable = this.retrieveTimetable.bind(this);
        this.retrieveAllTimetables = this.retrieveAllTimetables.bind(this);
        this.handleTimetableSelect = this.handleTimetableSelect.bind(this);
        this.handleRoomSelect = this.handleRoomSelect.bind(this);
    }

    retrieveTimetable(resultId, roomId) {
        let url = 'http://localhost:8090/timetable/' + resultId + '/room/' + roomId;
        let config = {
            headers: {
                'Access-Control-Allow-Origin': '*',
            }
        };
        axios.get(url, config)
            .then(response => response.data)
            .then(data => {
                this.setState({reservations: data})
            });

    }

    retrieveAllTimetables() {
        let url = 'http://localhost:8090/timetable';
        let config = {
            headers: {
                'Access-Control-Allow-Origin': '*',
            }
        };
        axios.get(url, config)
            .then(response => response.data)
            .then(data => {
                this.setState({timetables: data})
            });
    }

    retrieveAllRooms(resultId) {
        let url = 'http://localhost:8090/timetable/' + resultId + '/room';
        let config = {
            headers: {
                'Access-Control-Allow-Origin': '*',
            }
        };
        axios.get(url, config)
            .then(response => response.data)
            .then(data => {
                this.setState({rooms: data})
            });
    }

    handleTimetableSelect(key) {
        this.setState({
                selectedTimetable: key
            }
        );
        this.retrieveAllRooms(key.id);
    }

    handleRoomSelect(key) {
        this.setState({
            selectedRoom: {...key}
        });
        this.retrieveTimetable(this.state.selectedTimetable.id, key.id)
    }

    render() {
        return (
            <div className="Room">
                <SelectDropdown selectItems={this.state.timetables}
                                handleOnChange={this.handleTimetableSelect}
                                selectedItem={this.state.selectedTimetable}
                                defaultName={"timetable"}/>
                <SelectDropdown selectItems={this.state.rooms}
                                handleOnChange={this.handleRoomSelect}
                                selectedItem={this.state.selectedRoom}
                                defaultName={"room"}/>

                <RoomTable reservations={this.state.reservations}/>
            </div>
        );
    };


}

export default RoomTimetableView;
