import React, {Component} from 'react';
import './roomInput.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {retrieveAllRooms, saveRoom, deleteRoom, updateRoom} from '../../services/RoomService'
import {Button, Glyphicon} from "react-bootstrap";

import CrudTable from '../common/CrudTable';

class RoomInput extends Component {

    constructor(props) {
        super(props);
        this.state = {
            rooms: [],

        }
    }

    componentDidMount() {
        retrieveAllRooms()
            .then(response => {
                let rooms = response.map(room => ({
                    id: room.id,
                    name: room.name,
                    capacity: room.capacity,
                    editable: false,
                    editableName: room.name,
                    editableCapacity: room.capacity
                }));
                this.setState({rooms});
            })
            .catch(error => {
                console.error(error);
            });
    }

    // saveRoom() {
    //     saveRoom({name: 'new Room', capacity: 100});
    // }
    //
    // deleteRoom() {
    //     deleteRoom(123123123);
    // }
    //
    // updateRoom() {
    //     updateRoom(123123123, {name: 'new Room 2', capacity: 100});
    // }

    updateRoomObject(id, newProperties) {
        return this.state.rooms.map(room => {
            if (room.id == id) {
                return Object.assign({}, room, newProperties)
            }
            return room;
        });
    }

    toggleEdit(id, room) {
        this.setState({rooms: this.updateRoomObject(id, {editable: !room.editable})});
    }

    onNameChange(e, id) {
        this.setState({rooms: this.updateRoomObject(id, {editableName: e.target.value})});
    }

    onCapacityChange(e, id) {
        this.setState({rooms: this.updateRoomObject(id, {editableCapacity: e.target.value})});
    }

    updateRoomRow(id, room) {
        this.setState({
            rooms: this.updateRoomObject(id, {
                name: room.editableName,
                capacity: room.editableCapacity,
                editable: false
            })
        });
    }

    onRemove(id) {
        let rooms = this.state.rooms.filter(room => room.id !== id);
        this.setState({rooms});
    }

    render() {
        let {rooms} = this.state;
        return (
            <div className="room-input">
                <h5>Manage rooms</h5>

                <div className="row table-header">
                    <div className="column">Name</div>
                    <div className="column">Capacity</div>
                </div>

                <div className="scrollable-items">
                    {rooms && rooms.map(room => (
                        <CrudTable
                            key={room.id}
                            columns={[room.name, room.capacity]}
                            editable={room.editable}
                            toggleEdit={() => this.toggleEdit(room.id, room)}
                            updateRoomRow={() => this.updateRoomRow(room.id, room)}
                            onRemove={() => this.onRemove(room.id)}>

                            <input type="text" value={room.editableName}
                                   onChange={(e) => this.onNameChange(e, room.id)} />
                            <select
                                value={room.editableCapacity}
                                onChange={(e) => this.onCapacityChange(e, room.id)}>
                                <option key="30" value='30'>30</option>
                                <option key="100" value='100'>100</option>
                                <option key="200" value='200'>200</option>
                            </select>
                        </CrudTable>
                    ))}
                </div>
            </div>
        );
    };

}

export default RoomInput;
