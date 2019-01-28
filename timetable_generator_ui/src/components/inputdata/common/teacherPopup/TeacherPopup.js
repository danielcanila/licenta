import React from 'react';

import './teacherPopup.css';

import Popup from '../popup/Popup';
import CheckboxList from '../../../commons/CheckboxList/CheckboxList';

export default class TeacherPopup extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            name: props.name,
            unavailabilitySlots: props.unavailabilitySlots
        };
    }

    componentDidUpdate(prevProps) {
        if(prevProps.showPopup !== this.props.showPopup && this.props.showPopup === true) {
            let {name, lectures, allLectures} = this.props;
            this.setState({
                name
            });
        }
    }

    onNameChange = (event) => {
        this.setState({name: event.target.value});
    };

    onInit = (api) => {
        this.getSelectedLectures = api.getSelectedItems;
    };

    onSave = () => {
        let selectedLectures = this.getSelectedLectures();
        this.props.onSave(selectedLectures);
    };

    render() {
        let {showPopup, title, onClose, allLectures, lectures} = this.props,
            {name, unavailabilitySlots} = this.state;

        return (
            <Popup
                dialogClassName="teacher-modal-dialog"
                show={showPopup}
                title={title}
                onSave={this.onSave}
                handleClose={onClose}>

                <div className="teacher-popup">
                    <div className="popup-row">
                        <span className="label-name">Teacher Name:</span>
                        <input
                            type="text"
                            value={name}
                            onChange={this.onNameChange}
                        />
                    </div>

                    <div className="popup-row">
                        <span className="label-name">Lectures:</span>

                        <div className="list-wrapper">
                            <CheckboxList
                                onInit={this.onInit}
                                items={allLectures}
                                selectedItems={lectures}
                            />
                        </div>
                    </div>

                    <div className="popup-row">
                        <span className="label-name">Unavailability:</span>

                        <div className="list-wrapper">

                        </div>
                    </div>

                </div>
            </Popup>
        );
    }
}