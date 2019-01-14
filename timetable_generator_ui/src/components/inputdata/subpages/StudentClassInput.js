import React, {Component} from 'react';
import './studentClassInput.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {
    retrieveAllStudentClasses,
    saveClass,
    deleteClass,
    updateClass
} from '../../services/StudentClassService';

import CrudTableRow from '../common/CRUDTable/CrudTableRow';
import CrudTableHeader from '../common/CRUDTable/CrudTableHeader';
import Popup from '../common/popup/Popup';

export default class StudentClassInput extends Component {

    constructor(props) {
        super(props);
        this.state = {
            classes: [],
            showPopup: false,
            popupState: {
                name: ''
            }
        }
    }

    componentDidMount() {
        retrieveAllStudentClasses()
            .then(response => {
                let classes = this.initClasses(response);
                this.setState({classes});
            })
            .catch(error => {
                console.error(error);
            });
    }

    initClasses(classes) {
        return classes.map(cls => ({
            id: cls.id,
            name: cls.name,
            editable: false,
            newName: cls.name
        }));
    }

    updateClassObject(id, newProperties) {
        return this.state.classes.map(cls => {
            if (cls.id === id) {
                return Object.assign({}, cls, newProperties)
            }
            return cls;
        });
    }

    toggleEdit(id, classObject) {
        this.setState({classes: this.updateClassObject(id, {editable: !classObject.editable})});
    }

    onNameChange(e, id) {
        this.setState({classes: this.updateClassObject(id, {newName: e.target.value})});
    }

    updateClassRow(id, classObject) {
        let {newName} = classObject;

        updateClass(id, {name: newName})
            .then(() => {
                this.setState({
                    classes: this.updateClassObject(id, {
                        name: classObject.newName,
                        editable: false
                    })
                });
            });
    }

    onRemove(id) {
        deleteClass(id).then(() => {
            let classes = this.state.classes.filter(cls => cls.id !== id);
            this.setState({classes});
        });
    }

    onModalSave() {
        let {name} = this.state.popupState;
        saveClass({name}).then(response => {
            let newClasses = [...this.state.classes, response];
            this.setState({
                classes: this.initClasses(newClasses),
                showPopup: false,
                popupState: {
                    name: ''
                }
            });
        });
    }

    render() {
        let {classes, popupState} = this.state;

        return (
            <div className="class-input">

                <CrudTableHeader
                    title="Manage Student Classes"
                    columns={['Name']}
                    addNewItem={() => this.setState({showPopup: true})}
                />

                <div className="scrollable-items">
                    {classes && classes.map(classObject => (
                        <CrudTableRow
                            key={classObject.id}
                            columns={[classObject.name]}
                            editable={classObject.editable}
                            toggleEdit={() => this.toggleEdit(classObject.id, classObject)}
                            updateRow={() => this.updateClassRow(classObject.id, classObject)}
                            onRemove={() => this.onRemove(classObject.id)}>

                            <input type="text" value={classObject.newName}
                                   onChange={(e) => this.onNameChange(e, classObject.id)}/>

                        </CrudTableRow>
                    ))}
                </div>

                {(!classes || classes.length === 0) &&
                <div className="no-items">
                    <span>No classes to display</span>
                </div>
                }

                <Popup
                    show={this.state.showPopup}
                    title="Add New Student Class"
                    onSave={() => this.onModalSave()}
                    handleClose={() => this.setState({showPopup: false})}>

                    <div className="class-input add-row">
                        <span>Class Name:</span>
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

