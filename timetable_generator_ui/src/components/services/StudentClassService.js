import axios from "axios";

const url = 'http://localhost:8090/studentClass';

const retrieveAllStudentClasses = function () {
    let requestUrl = url;
    let config = {
        headers: {
            'Access-Control-Allow-Origin': '*',
        }
    };
    return axios.get(requestUrl, config)
        .then(response => response.data)
        .catch(function (error) {
            console.error(error);
        });

};

const saveClass = function (classData) {
    let requestUrl = url;
    return axios.post(requestUrl, classData)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};

const deleteClass = function (classData) {
    let requestUrl = url + "/" + classData;
    return axios.delete(requestUrl)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};

const updateClass = function (classId, classData) {
    let requestUrl = url + "/" + classId;
    return axios.patch(requestUrl, classData)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};

export {retrieveAllStudentClasses, saveClass, deleteClass, updateClass};