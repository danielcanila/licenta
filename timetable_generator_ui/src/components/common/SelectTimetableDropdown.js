import React, {Component} from 'react';
import {SplitButton, MenuItem} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

class SelectTimetableDropdown extends Component {

    constructor(props) {
        super(props);
    }

    createSelectItems(values) {
        let selectItems = [];
        if (this.props.selectItems) {
            for (let timetable of values) {
                selectItems.push(<MenuItem eventKey={timetable}>{timetable.name}</MenuItem>)
            }
        }
        return selectItems;
    }

    createTimetableDropDown() {
        return <SplitButton title={this.getTitle()} pullRight id="split-button-pull-right"
                            onSelect={this.props.handleOnChange}>
            {this.createSelectItems(this.props.selectItems)}
        </SplitButton>;
    }

    render() {
        return this.createTimetableDropDown();
    }

    getTitle() {
        if (this.props.selectedItem) {
            return <div>{this.props.selectedItem.name}</div>
        } else {
            return <div>Choose a {this.props.elementName}</div>
        }
    }
    ;
}

export default SelectTimetableDropdown;
