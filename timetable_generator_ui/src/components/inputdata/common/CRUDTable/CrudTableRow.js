import React from 'react';
import {Glyphicon} from "react-bootstrap";

import './crudTableRow.css';

export default class CrudTableRow extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let {columns, editable, toggleEdit, updateRow, onRemove} = this.props;
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
                        <Glyphicon glyph="glyphicon glyphicon-pencil" onClick={toggleEdit} />
                        :
                        <Glyphicon glyph="glyphicon glyphicon-ok" onClick={updateRow} />
                    }
                    <Glyphicon glyph="glyphicon glyphicon-trash" onClick={onRemove} />
                </div>
            </div>
        );
    };
}