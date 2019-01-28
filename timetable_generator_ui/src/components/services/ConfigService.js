import axios from "axios";

const url = 'http://localhost:8090/timetableConfig';

const retrieveLatestConfig = function () {
    let requestUrl = url + "/latest";
    let config = {
        headers: {
            'Access-Control-Allow-Origin': '*',
        }
    };
    return axios.get(requestUrl, config)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};

const saveConfig = function (configData) {
    let requestUrl = url;
    return axios.post(requestUrl, configData)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};
const addStudentsToConfig = function (id, students) {
    let requestUrl = url + "/" + id + "/studentclasses";
    return axios.post(requestUrl, students)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};
const addTeachersToConfig = function (id, assignments) {
    let requestUrl = url + "/" + id + "/assignTeachers";
    return axios.post(requestUrl, assignments)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};

export {retrieveLatestConfig, saveConfig, addStudentsToConfig, addTeachersToConfig};