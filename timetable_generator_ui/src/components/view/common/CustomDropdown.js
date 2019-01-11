import React, {Component} from 'react';
import './customDropdown.css';
import {SplitButton, MenuItem} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

class CustomDropdown extends Component {

    getTitle() {
        if (this.props.selectedItem) {
            return <div>{this.props.selectedItem.name}</div>
        } else {
            return <div>Choose a {this.props.defaultName}</div>
        }
    }

    createSelectItems(values) {
        let selectItems = [];
        if (values) {
            for (let element of values) {
                selectItems.push(<MenuItem key={element.index} eventKey={element}>{element.name}</MenuItem>)
            }
        }
        return selectItems;
    }

    createTimetableDropDown() {
        return <SplitButton id={'dropdown-id-'+this.getTitle} title={this.getTitle()}
                            bsClass="dropdown"
                            bsSize="small"
                            onSelect={this.props.handleOnChange}
                            disabled={!this.props.selectItems}>
            {this.createSelectItems(this.props.selectItems)}
        </SplitButton>;
    }

    render() {
        return this.createTimetableDropDown();
    }

}
export default CustomDropdown;
