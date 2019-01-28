import React, {Component} from 'react';
import {Button, FieldGroup} from "react-bootstrap";
import './createConfig.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {
    retrieveLatestConfig,
    saveConfig
} from '../../services/ConfigService';

class CreateConfig extends Component {

    constructor(props) {
        super(props);
        this.state = {
            configName: '',
            enableNext: this.props.enableNext
        };
        // this.handleChange = this.handleChange.bind(this);
        this.createConfig = this.createConfig.bind(this);
    }

    createConfig() {
        // let saveConfig1 = saveConfig({name: this.state.configName});
        this.props.enableNext();
    }

    handleChange(e) {
        this.setState({configName: e.target.value});
    }

    render() {
        return (
            <div>
                <div>
                    <label>
                        Config name:
                        <input type="text" value={this.state.configName} onChange={this.handleChange.bind(this)}/>
                    </label>
                </div>
                <Button bsStyle="primary" onClick={this.createConfig}>Next</Button>
            </div>
        );
    };
}

export default CreateConfig;
