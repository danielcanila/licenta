import React from 'react';

import './teacherPopup.css';

import Popup from '../popup/Popup';
import CheckboxList from '../../../commons/CheckboxList/CheckboxList';
import {Button} from "react-bootstrap";

export default class TeacherPopup extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            name: props.name,
            day: '',
            time: '',
            unavailabilitySlots: (props.unavailabilitySlots && props.unavailabilitySlots.length > 0) ? [...props.unavailabilitySlots] : []
        };
    }

    componentDidUpdate(prevProps) {
        if(prevProps.showPopup !== this.props.showPopup && this.props.showPopup === true) {
            this.setState({
                name: this.props.name
            });
        }
    }

    onNameChange = (event) => {
        this.setState({name: event.target.value});
    };

    onDayChange = e => {
		this.setState({day: e.target.value});
	};
	
	onTimeChange = e => {
		this.setState({time: e.target.value});
	};

	addInterval = () => {
		let {day, time} = this.state;
		if(!day || !time) return;

		this.setState({
			day: '',
			time: '',
			unavailabilitySlots: [...this.state.unavailabilitySlots, {
				day,
				time
			}],
		});
	}

    onInit = (api) => {
        this.getSelectedLectures = api.getSelectedItems;
    };

    onSave = () => {
        let selectedLectures = this.getSelectedLectures();
        this.props.onSave(selectedLectures);
    };

    render() {
        let {showPopup, title, onClose, allLectures, lectures, getDayObject, getTimeInterval} = this.props,
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
							<span>Day:</span>
                            <select value={this.state.day} onChange={this.onDayChange}>
                                <option value="">-</option>
                                {Object.values(getDayObject()).map(dayObj => (
                                    <option key={dayObj} value={dayObj}>{dayObj}</option>
                                ))}
                            </select>
							
							<span>Time:</span>
                            <select value={this.state.time} onChange={this.onTimeChange}>
                                <option value="">-</option>
                                {Object.values(getTimeInterval()).map(timeInt => (
                                    <option key={timeInt} value={timeInt}>{timeInt}</option>
                                ))}
                            </select>

							<Button bsStyle="primary" onClick={this.addInterval}>Add</Button>
							
							{unavailabilitySlots.length > 0 &&
								<div className="selected-intervals">
									<div className="title">Selected Intervals:</div>
									{unavailabilitySlots.map((interval, i) => (
										<div key={i} className="row">
											<span>{interval.day}</span>
											<span>{interval.time}</span>
										</div>
									))}
								</div>
							}
                        </div>
                    </div>

                </div>
            </Popup>
        );
    }
}