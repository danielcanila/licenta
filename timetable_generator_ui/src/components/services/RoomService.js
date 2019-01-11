import axios from "axios";

const url = 'http://localhost:8090/room';

const retrieveAllRooms = function () {
    let requestUrl = url;
    let config = {
        headers: {
            'Access-Control-Allow-Origin': '*',
        }
    };
    return axios.get(requestUrl, config)
        .then(response => response.data)
        .then(data => {
            return data;
        });

};

const saveRoom = function (roomData) {
    let requestUrl = url;
    return axios.post(requestUrl, roomData)
        .then(function (response) {
            return response;
        })
        .catch(function (error) {
            return error;
        });
};

const deleteRoom = function (roomId) {
    let requestUrl = url + "/" + roomId;
    return axios.delete(requestUrl)
        .then(function (response) {
            return response;
        })
        .catch(function (error) {
            return error;
        });
};

const updateRoom = function (roomId, roomData) {
    let requestUrl = url + "/" + roomId;
    return axios.patch(requestUrl, roomData)
        .then(function (response) {
            return response;
        })
        .catch(function (error) {
            return error;
        });
};

export {retrieveAllRooms, saveRoom, deleteRoom, updateRoom};