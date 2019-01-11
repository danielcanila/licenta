import React, {Component} from 'react';
import './roomInput.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {retrieveAllRooms, saveRoom, deleteRoom, updateRoom} from '../../services/RoomService'

class RoomInput extends Component {

    constructor(props) {
        super(props);
        this.state = {
            rooms: retrieveAllRooms()
        }
    }

    saveRoom() {
        saveRoom({name: 'new Room', capacity: 100});
    }

    deleteRoom() {
        deleteRoom(123123123);
    }

    updateRoom() {
        updateRoom(123123123, {name: 'new Room 2', capacity: 100});
    }

    render() {
        return (
            <div>Room input</div>
        );
    };

}

export default RoomInput;
