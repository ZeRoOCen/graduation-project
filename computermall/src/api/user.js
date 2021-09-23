/**
 * 配置参数说明
 * @param { String } method 请求方法
 * @param { String } url 连接后端地址
 * @param { Blooean } isDycRouter 是否是动态请求
 */

export default {
    alluser: {
        method: 'get',
        url: '/alluser'
    },
    adduser: {
        method: 'post',
        url: '/adduser'
    },
    deleteuser: {
        method: 'delete',
        url: '/deleteuser'
    },
    getupdateuser: {
        method: 'get',
        url: '/getupdateuser'
    },
    edituser: {
        method: 'post',
        url: '/edituser'
    },
    userstate: {
        method: 'get',
        url: '/userstate',
    },
    updateavatar: {
        method: 'post',
        url: '/updateavatar',
    },
    getuserinfo: {
        method: 'post',
        url: '/getuserinfo',
    },
    updateuname: {
        method: 'post',
        url: '/updateuname',
    },
    updateusex: {
        method: 'post',
        url: '/updateusex',
    },
    updateumobile: {
        method: 'post',
        url: '/updateumobile',
    },
    updateuaddress: {
        method: 'post',
        url: '/updateuaddress',
    },
    useravatar: {
        method: 'post',
        url: '/useravatar',
    },
    getuseravatar: {
        method: 'post',
        url: '/getuseravatar',
    },
    idExistJudge:{
        method: 'post',
        url: '/idexistjudge',
    },
    getUserList:{
        method: 'post',
        url: '/getuserlist',
    },
    getCustomerInfo:{
        method: 'post',
        url: '/getcustomerinfo', 
    },
}