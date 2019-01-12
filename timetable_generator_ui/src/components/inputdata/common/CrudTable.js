import React from 'react';
import {Glyphicon} from "react-bootstrap";

import './crudTable.css';

export default class CrudTable extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let {columns, editable, toggleEdit, updateRoomRow, onRemove} = this.props;
        if(!columns) return null;

        return (
            <div className="crud-table row">
                {!editable ? columns.map((column, index) => (
                    <div key={index} className="column">{column}</div>
                )) : (
                    this.props.children
                )}

                <div className="action-items">
                    {!editable ?
                        <Glyphicon glyph="glyphicon glyphicon-pencil"  onClick={toggleEdit} />
                        :
                        <Glyphicon glyph="glyphicon glyphicon-ok" onClick={updateRoomRow} />
                    }
                    <Glyphicon glyph="glyphicon glyphicon-trash" onClick={onRemove} />
                </div>
            </div>
        );
    };
}