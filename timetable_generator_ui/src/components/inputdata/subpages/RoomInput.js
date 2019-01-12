import React, {Component} from 'react';
import './roomInput.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {deleteRoom, retrieveAllRooms, saveRoom, updateRoom} from '../../services/RoomService'

import CrudTableRow from '../common/CRUDTable/CrudTableRow';
import CrudTableHeader from '../common/CRUDTable/CrudTableHeader';
import Popup from '../common/popup/Popup';

class RoomInput extends Component {

    constructor(props) {
        super(props);
        this.state = {
            rooms: [],
            showPopup: false,
            popupState: {
                capacity: 30,
                name: ''
            }
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
                    newName: room.name,
                    newCapacity: room.capacity
                }));
                this.setState({rooms});
            })
            .catch(error => {
                console.error(error);
            });
    }

    updateRoomObject(id, newProperties) {
        return this.state.rooms.map(room => {
            if (room.id === id) {
                return Object.assign({}, room, newProperties)
            }
            return room;
        });
    }

    toggleEdit(id, room) {
        this.setState({rooms: this.updateRoomObject(id, {editable: !room.editable})});
    }

    onNameChange(e, id) {
        this.setState({rooms: this.updateRoomObject(id, {newName: e.target.value})});
    }

    onCapacityChange(e, id) {
        this.setState({rooms: this.updateRoomObject(id, {newCapacity: e.target.value})});
    }

    updateRoomRow(id, room) {
        let {newName, newCapacity} = room;

        updateRoom(id, {name: newName, capacity: newCapacity})
            .then(() => {
                this.setState({
                    rooms: this.updateRoomObject(id, {
                        name: room.newName,
                        capacity: room.newCapacity,
                        editable: false
                    })
                });
            });
    }

    onRemove(id) {
        deleteRoom(id).then(() => {
            let rooms = this.state.rooms.filter(room => room.id !== id);
            this.setState({rooms});
        });
    }

    onModalSave() {
        let {name, capacity} = this.state.popupState;
        saveRoom({name, capacity}).then(response => {
            this.setState({
                rooms: [...this.state.rooms, response],
                showPopup: false,
                popupState: {
                    capacity: 30,
                    name: ''
                }
            });
        });
    }

    render() {
        let {rooms, popupState} = this.state;

        return (
            <div className="room-input">

                <CrudTableHeader
                    title="Manage rooms"
                    columns={['Name', 'Capacity']}
                    addNewItem={() => this.setState({showPopup: true})}
                />

                <div className="scrollable-items">
                    {rooms && rooms.map(room => (
                        <CrudTableRow
                            key={room.id}
                            columns={[room.name, room.capacity]}
                            editable={room.editable}
                            toggleEdit={() => this.toggleEdit(room.id, room)}
                            updateRoomRow={() => this.updateRoomRow(room.id, room)}
                            onRemove={() => this.onRemove(room.id)}>

                            <input type="text" value={room.newName}
                                   onChange={(e) => this.onNameChange(e, room.id)}/>
                            <select
                                value={room.newCapacity}
                                onChange={(e) => this.onCapacityChange(e, room.id)}>
                                <option key="30" value='30'>30</option>
                                <option key="100" value='100'>100</option>
                                <option key="200" value='200'>200</option>
                            </select>

                        </CrudTableRow>
                    ))}
                </div>

                <Popup
                    show={this.state.showPopup}
                    title="Add new Room"
                    onSave={() => this.onModalSave()}
                    handleClose={() => this.setState({showPopup: false})}>

                    <div className="room-input add-row">
                        <span>Room Name:</span>
                        <input
                            type="text"
                            value={popupState.name}
                            onChange={e => this.setState({popupState: {...popupState, name: e.target.value}})}
                        />
                    </div>

                    <div className="room-input add-row">
                        <span>Capacity:</span>
                        <select
                            value={popupState.capacity}
                            onChange={(e) => this.setState({popupState: {...popupState, capacity: e.target.value}})}>
                            <option key="30" value='30'>30</option>
                            <option key="100" value='100'>100</option>
                            <option key="200" value='200'>200</option>
                        </select>
                    </div>
                </Popup>
            </div>
        );
    };

}

export default RoomInput;
