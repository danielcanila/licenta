import axios from "axios";

const url = 'http://localhost:8090/lecture';

const retrieveAllLectures = function () {
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

const saveLecture = function (lectureData) {
    let requestUrl = url;
    return axios.post(requestUrl, lectureData)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};

const deleteLecture = function (lectureId) {
    let requestUrl = url + "/" + lectureId;
    return axios.delete(requestUrl)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};

const updateLecture = function (lectureId, lectureData) {
    let requestUrl = url + "/" + lectureId;
    return axios.patch(requestUrl, lectureData)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};

const addTeacherToLecture = function (lectureId, teacherIds) {
    let requestUrl = url + "/" + lectureId + "/teacher";
    return axios.post(requestUrl, teacherIds)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};

const removeTeacherToLecture = function (lectureId, teacherIds) {
    let requestUrl = url + "/" + lectureId + "/teacher";
    return axios.delete(requestUrl, teacherIds)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};

export {retrieveAllLectures, saveLecture, deleteLecture, updateLecture, addTeacherToLecture,removeTeacherToLecture};