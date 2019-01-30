import React, {Component} from 'react';
import './app.css';
import {BrowserRouter as Router, Route, Switch, Redirect} from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import Home from "./home/Home";
import InputData from "./inputdata/InputData";
import ConfigTimetable from "./config/ConfigTimetable";
import ViewTimetable from "./view/ViewTimetable";
import Navigation from "./navigation/Navigation";

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            activePage: 1
        };
    }

    handleSelect(selectedKey) {
        this.setState({activePage: selectedKey});
    }

    render() {
        return (
            <Router>
                <div className="main-page">
                    <Navigation/>
                    <div className="page-content">
                        <Switch>
                            <Route path="/home" component={Home}/>
                            <Route path="/inputData" component={InputData}/>
                            <Route path="/timetableConfig" component={ConfigTimetable}/>
                            <Route path="/viewTimetable" component={ViewTimetable}/>
                            <Route render={() => <Redirect to="/home" />} />
                        </Switch>
                    </div>

                </div>
            </Router>

        );
    };

}

export default App;
