import React, {Component} from 'react';
import './lectureInput.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {
    retrieveAllLectures,
    saveLecture,
    deleteLecture,
    updateLecture
} from '../../services/LectureService';
import CrudTableHeader from "../common/CRUDTable/CrudTableHeader";
import CrudTableRow from "../common/CRUDTable/CrudTableRow";
import Popup from "../common/popup/Popup";

class LectureInput extends Component {

    constructor(props) {
        super(props);
        this.state = {
            lectures: [],
            isReadyToRender: false,
            showPopup: false,
            popupState: {
                name: ''
            }
        };
    }

    componentDidMount() {
        retrieveAllLectures()
            .then(response => {
                let lectures = this.initLectures(response);
                this.setState({lectures, isReadyToRender: true});
            })
            .catch(error => {
                console.error(error);
            });
    }

    initLectures(lectures) {
        return lectures.map(lec => ({
            id: lec.id,
            name: lec.name,
            editable: false,
            newName: lec.name
        }));
    }

    updateLectureObject(id, newProperties) {
        return this.state.lectures.map(lecture => {
            if (lecture.id === id) {
                return Object.assign({}, lecture, newProperties)
            }
            return lecture;
        });
    }

    toggleEdit(id, lecture) {
        this.setState({lectures: this.updateLectureObject(id, {editable: !lecture.editable})});
    }

    onNameChange(e, id) {
        this.setState({lectures: this.updateLectureObject(id, {newName: e.target.value})});
    }

    updateClassRow(id, lecture) {
        let {newName} = lecture;

        updateLecture(id, {name: newName})
            .then(() => {
                this.setState({
                    lectures: this.updateLectureObject(id, {
                        name: lecture.newName,
                        editable: false
                    })
                });
            });
    }

    onRemove(id) {
        deleteLecture(id).then(() => {
            let lectures = this.state.lectures.filter(lecture => lecture.id !== id);
            this.setState({lectures});
        });
    }

    onModalSave() {
        let {name} = this.state.popupState;
        saveLecture({name}).then(response => {
            let newLectures = [...this.state.lectures, response];
            this.setState({
                lectures: this.initLectures(newLectures),
                showPopup: false,
                popupState: {
                    name: ''
                }
            });
        });
    }

    render() {
        let {lectures, popupState, isReadyToRender} = this.state;

        return (
            <div className="lecture-input">
                <CrudTableHeader
                    title="Manage Lectures"
                    columns={['Lecture Title']}
                    addNewItem={() => this.setState({showPopup: true})}
                />

                <div className="scrollable-items">
                    {isReadyToRender && lectures && lectures.map(lecture => (
                        <CrudTableRow
                            key={lecture.id}
                            columns={[lecture.name]}
                            editable={lecture.editable}
                            toggleEdit={() => this.toggleEdit(lecture.id, lecture)}
                            updateRow={() => this.updateClassRow(lecture.id, lecture)}
                            onRemove={() => this.onRemove(lecture.id)}>

                            <input type="text" value={lecture.newName}
                                   onChange={(e) => this.onNameChange(e, lecture.id)}/>

                        </CrudTableRow>
                    ))}
                </div>

                {(isReadyToRender && (!lectures || lectures.length === 0)) &&
                    <div className="no-items">
                        <span>No lectures to display</span>
                    </div>
                }

                <Popup
                    show={this.state.showPopup}
                    title="Add a new lecture"
                    onSave={() => this.onModalSave()}
                    handleClose={() => this.setState({showPopup: false})}>

                    <div className="lecture-input add-row">
                        <span>Lecture title:</span>
                        <input
                            type="text"
                            value={popupState.name}
                            onChange={e => this.setState({popupState: {...popupState, name: e.target.value}})}
                        />
                    </div>
                </Popup>
            </div>
        );
    };

}

export default LectureInput;
