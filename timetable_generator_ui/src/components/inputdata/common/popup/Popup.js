import React from 'react';

import './popup.css';
import {Modal, Button} from 'react-bootstrap';

export default function({title, show, handleClose, onSave, children}) {
    return (
        <div className="static-modal">

            <Modal show={show}>
                <Modal.Header>
                    <Modal.Title>{title}</Modal.Title>
                </Modal.Header>

                <Modal.Body>{children}</Modal.Body>

                <Modal.Footer>
                    <Button onClick={handleClose}>Close</Button>
                    <Button bsStyle="primary" onClick={onSave}>Save</Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}