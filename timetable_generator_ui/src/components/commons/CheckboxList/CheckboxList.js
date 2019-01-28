import React from 'react';
import {Button} from "react-bootstrap";

import './checkboxList.css';

export default class CheckboxList extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            items: props.selectedItems && props.selectedItems.length > 0 ? this.initItemsList() : props.items,
            selectedItems: props.selectedItems || []
        };
    }

    componentDidMount() {
        this.props.onInit && this.props.onInit({
            getSelectedItems: () => ([...this.state.selectedItems])
        });
    }

    initItemsList() {
        let {items, selectedItems} = this.props;

        return items.map(item => {
            if(selectedItems.find(i => i.id === item.id)) {
                return Object.assign({}, item, {checked: true});
            }
            return item;
        });
    }

    onChange = (event, item) => {
        let checked = event.target.checked,
            {items, selectedItems} = this.state;

        //update items state:
        this.setState({
            items: items.map(i => {
                if(i.id === item.id) {
                    return Object.assign({}, i, {checked});
                }
                return i;
            })
        });

        //update selectedItems state:
        if(checked) {
            // add the selected lecture
            this.setState({selectedItems: [...selectedItems, Object.assign({}, item)]});
        } else {
            // remove the selected lecture
            this.setState({selectedItems: selectedItems.filter(i => i.id !== item.id)});
        }
    };

    render() {
        let {items, selectedItems} = this.state,
            {buttonTitle, onTrigger} = this.props;

        return (
            <div className="checkbox-list">
                {items.map(item => (
                    <div key={item.id} className="item">
                        <input type="checkbox" checked={item.checked} onChange={(e) => this.onChange(e, item)} />
                        <span>{item.name}</span>
                    </div>
                ))}
            </div>
        );
    }
}

// How to use:

// class Blahblah {
//
//     onInit = (api) => {
//         this.getCheckboxListSelectedItems = api.getSelectedItems;
//     };
//
//     aCallback() {
//         let selectedItems = this.getCheckboxListSelectedItems();
//     }
//
//     anotherCallback() {
//         let selectedItems = this.getCheckboxListSelectedItems();
//     }
//
//     render() {
//         return (
//             <CheckboxList
//                 onInit={this.onInit}
//                 items={this.props.allLectures}
//                 selectedItems={this.props.lectures}
//             />
//         );
//     }
//
// }