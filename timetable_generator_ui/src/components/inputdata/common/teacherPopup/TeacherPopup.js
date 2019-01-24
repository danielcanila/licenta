import React from 'react';

import './teacherPopup.css';

import Popup from '../popup/Popup';

export default class TeacherPopup extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            name: props.name,
            lectures: props.lectures,
            unavailabilitySlots: props.unavailabilitySlots
        };
    }

    componentDidUpdate(prevProps) {
        if(prevProps.showPopup !== this.props.showPopup && this.props.showPopup === true) {
            let {name, lectures} = this.props;
            this.setState({
                name,
                lectures
            });
        }
    }

    onNameChange = (event) => {
        this.setState({name: event.target.value});
    };

    onLectureChange = (event, lecture) => {
        let checked = event.target.checked;

        this.setState({
            lectures: this.state.lectures.map(l => {
                if(l.id === lecture.id) {
                    return Object.assign({}, l, {checked});
                }
                return l;
            })
        });
    };

    render() {
        let {showPopup, title, onSave, onClose} = this.props,
            {name, lectures, unavailabilitySlots} = this.state;

        return (
            <Popup
                show={showPopup}
                title={title}
                onSave={() => onSave({...this.state})}
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
                            {lectures.map((lecture, i) => (
                                <div key={i} className="item">
                                    <input type="checkbox" checked={lecture.checked} onChange={(e) => this.onLectureChange(e, lecture)} />
                                    <span>{lecture.name}</span>
                                </div>
                            ))}
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