import axios from "axios";

const url = 'http://localhost:8090/teacher';

const retrieveAllTeachers = function () {
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

const saveTeacher = function (teacherData) {
    let requestUrl = url;
    return axios.post(requestUrl, teacherData)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};

const deleteTeacher = function (teacherId) {
    let requestUrl = url + "/" + teacherId;
    return axios.delete(requestUrl)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};

const updateTeacher = function (teacherId, teacherData) {
    let requestUrl = url + "/" + teacherId;
    return axios.patch(requestUrl, teacherData)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};

const addLectures = function (teacherId, lectureIds) {
    let requestUrl = url + "/" + teacherId + "/lecture";
    return axios.post(requestUrl, lectureIds)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};

const removeLectures = function (teacherId, lectureIds) {
    let requestUrl = url + "/" + teacherId + "/lecture";
    return axios.delete(requestUrl, lectureIds)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};

const setUnavailabilityTimeslots = function (teacherId, timeslots) {
    let requestUrl = url + "/" + teacherId + "/unavailabilityTimeslots";
    return axios.put(requestUrl, timeslots)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.error(error);
        });
};


export {
    retrieveAllTeachers,
    saveTeacher,
    deleteTeacher,
    updateTeacher,
    addLectures,
    removeLectures,
    setUnavailabilityTimeslots
};