import React, {Component} from 'react';
import './customDropdown.css';
import {SplitButton, MenuItem} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

class CustomDropdown extends Component {
    getTitle() {
        let {selectedItem, defaultName} = this.props;

        if (selectedItem) {
            return <div>{selectedItem.name}</div>
        } else {
            return <div>Choose a {defaultName}</div>
        }
    }

    render() {
        let {selectItems} = this.props;

        return (
            <SplitButton id="dropdown-id" title={this.getTitle()}
                         bsClass="dropdown"
                         bsSize="small"
                         onSelect={this.props.handleOnChange}
                         disabled={!this.props.selectItems}>
                {selectItems && selectItems.map(item => (
                    <MenuItem key={item.id} eventKey={item}>{item.name}</MenuItem>
                ))}
            </SplitButton>
        );
    }

}
export default CustomDropdown;
