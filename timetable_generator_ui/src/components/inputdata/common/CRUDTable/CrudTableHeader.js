import React from 'react';

import {Glyphicon} from "react-bootstrap";
import './crudTableHeader.css';

export default function({columns, title, addNewItem}) {
    return (
        <div className="table-header">
            <div className="title-section">
                <h5>{title}</h5>
                <Glyphicon glyph="glyphicon glyphicon-plus" onClick={addNewItem} />
            </div>

            <div className="row">
                {columns && columns.map((title, index) => (
                    <div key={index} className="column">{title}</div>
                ))}
            </div>
        </div>
    );
}