import axios from "axios";

const API_BASE_URL = 'http://localhost:8080/students';

export const listStoryTaleSer= () => {
    return axios.get(API_BASE_URL)
}