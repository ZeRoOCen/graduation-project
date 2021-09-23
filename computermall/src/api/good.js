/**
 * 配置参数说明
 * @param { String } method 请求方法
 * @param { String } url 连接后端地址
 * @param { Blooean } isDycRouter 是否是动态请求
 */

export default {
    getGoodList: { 
        method: 'post',
        url: '/getgoodlist'
    },
    addGood: {
        method: 'post',
        url: '/addgood'
    },
    goodStateChanged: {
        method: 'post',
        url: '/goodstate'
    },
    deleteGood: {
        method: 'delete',
        url: '/deletegood'
    },
    getGoodInfoByName: {
        method: 'post',
        url: '/getgoodinfobyname'
    },
    getUpdateGood: {
        method: 'post',
        url: '/getupdategood'
    },   
    editGood: {
        method: 'post',
        url: '/editgood'
    },
    addGoodInfoToCart:{
        method:'post',
        url: '/addgoodinfotoCart'
    },
    addNameAndRoleToCart:{
        method:'post',
        url: '/addnameandroletocart'
    },
    //待删除，如果后续没有再使用，请删除它
    // getCartGood:{
    //     method:'post',
    //     url: '/getcartgood'
    // },
    getGoodsDiscount:{
        method:'post',
        url: '/getgoodsdiscount'
    },
    //待删除，如果后续没有再使用，请删除它
    // favorablePrice:{
    //     method:'post',
    //     url: '/favorableprice'
    // },
    getGoodsCartInfo:{
        method:'post',
        url: '/getgoodscartinfo'
    },
    //待删除，如果后续没有再使用，请删除它
    // selectGoodsAmount:{
    //     method:'post',
    //     url: '/selectgoodsamount'
    // },
    updateSameGoods:{
        method:'post',
        url: '/updatesamegoods'
    },
    amountChanged:{
        method:'post',
        url: '/amountchanged'
    },
    submitOrder:{
        method:'post',
        url: '/submitorder'
    },
    createPaymentTime:{
        method:'post',
        url: '/createpaymenttime'
    },
    cancelOrder:{
        method:'post',
        url: '/cancelorder'
    },
    deleteCartGoods:{
        method:'post',
        url: '/deletecartgoods'
    },
    getOrderInfo:{
        method:'post',
        url: '/getorderinfo'
    },
    getOrderRow:{
        method:'post',
        url: '/getorderrow'
    },
    paymentOrder:{
        method:'post',
        url: '/paymentorder'
    },
    orderCancel:{
        method:'post',
        url: '/ordercancel'
    },
    addGoodsToActivity:{
        method:'post',
        url: '/addgoodstoactivity'
    },
    getGoodsActivity:{
        method:'post',
        url: '/getgoodsactivity'
    },
    activitySubmit:{
        method:'post',
        url: '/activitysubmit'
    },
    judgeUidToGoodsExist:{
        method:'post',
        url: '/judgeuidtogoodsexist'
    },
    addGoodsCommentPower:{
        method:'post',
        url: '/addgoodscommentpower'
    },
    getCommentList:{
        method:'post',
        url: '/getcommentlist'
    },
    getComment:{
        method:'post',
        url: '/getcomment'
    },
    addComment:{
        method:'post',
        url: '/addcomment'
    },
    getAvatar:{
        method:'post',
        url: '/getavatar'
    },
    sendChatMessage:{
        method:'post',
        url: '/sendchatmessage'
    },
    getChatMessage:{
        method:'post',
        url: '/getchatmessage'
    },
    getGoodActivityList:{
        method:'post',
        url: '/getgoodactivitylist'
    },
    deleteGoodsActivity:{
        method:'post',
        url: '/deletegoodsactivity'
    }
}