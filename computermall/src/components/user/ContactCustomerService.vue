
<template>
  <!-- 对话框容器 -->
  <div class="dialog_container">
    <!-- 头部区域 -->
    <section class="header_container">
      <!-- 用户头像 -->
      <img
        class="login_user_avatar"
        :src="loginUserAvatar"
        alt="头像加载中....."
      />
      <!-- 用户名 -->
      <div class="login_user_name">{{ loginUserName }}</div>
      <!-- 连接判断 -->
      <div class="Connection_judge_success" v-if="connectionSuccess">连接成功 在线中...</div>
      <div class="Connection_judge_failure" v-if="connectionFailure">连接失败 离线中...</div>
    </section>

    <!--主体区域 
    客服列表和聊天区域容器（做flex布局用） -->
    <section class="customer_chat_container">
      <!-- 客服列表+客服上线通知 -->
      <section class="customer_listAndOnline">
        <div class="customer_list_text">客服列表</div>
        <!-- 客服列表 -->
          <div class="customer_list_container"> 
              <ul class="customer_listAndOnline_ul">
              
              <li class="customer_list_li" v-for="customerName in customerNameList" :key="customerName">
                <a class="customer_a" @click="showChatView('{{customerName}}')">
                  <!-- <img class="customer_avatar" :src="loginUserAvatar" alt="" /> -->
                  <div>{{customerName}}</div>
                </a>
              </li>

              <li class="customer_list_li" >
                <a class="customer_a" @click="showChatView('张三')">
                  <!-- <img class="customer_avatar" :src="loginUserAvatar" alt="" /> -->
                  <div>张三</div>
                </a>
              </li>

            </ul>
          </div>
        <!-- 客服上线通知 -->
        <div class="customer_online_text">上线通知</div>
        <div class="customer_online_container">
          <ul class="customer_listAndOnline_ul">
            
            <li class="customer_online_li" v-for="customerName in customerNameList" :key="customerName">{{customerName}}上线了</li>

            <li class="customer_online_li">李四上线了</li>
            
          </ul>
        </div>
      </section>

      <!-- 聊天区域外部边框控制容器 -->
      <section class="chat_outer_container">
        <!-- 聊天区域内部展示控制容器 -->
        <div class="chat_inner_container" :style="dialogVisible">
          <div class=""></div>
          <!-- 聊天消息展示区域 -->
          <section class="chat_show_message_area">
            <!-- 正在和谁聊天显示框 -->
            <div id="show_chatting_user">正在和"{{ toName }}"聊天</div>
            <!-- 左侧对方消息区域 -->
            <div class="chat_left_show_message_area">
              <!-- 历史消息区域 -->
              <div class="history_left_userInfo">
                <img class="customer_avatar" :src="loginUserAvatar" alt="" />
                <div id="history_message">历史消息</div>
              </div>
              <!-- 实时消息区域 -->
              <div class="realTime_left_userInfo">
                <img class="customer_avatar" :src="loginUserAvatar" alt="" />
                <div id="realTime_message">实时消息</div>
              </div>
            </div>

            <!-- 右侧本人消息区域 -->
            <div class="chat_right_show_message_area">
              <!-- 历史消息区域 -->
              <div class="history_right_userInfo">
                <img class="customer_avatar" :src="loginUserAvatar" alt="" />
                <div id="history_right_message">历史消息</div>
              </div>
              <!-- 实时消息区域 -->
              <div class="realTime_right_userInfo">
                <img class="customer_avatar" :src="loginUserAvatar" alt="" />
                <div id="realTime_right_message">实时消息</div>
              </div>
            </div>
          </section>

          <!-- 聊天输入内容区域 -->
          <section class="chat_input_message_area">hello-input</section>
        </div>
      </section>
    </section>
  </div>
</template>

<script>
export default {
  name: "contactCustomerService",

  created() {
    // this.getUserName();
    // this.getUserAvatar();
  },

  mounted(){
    // this.connectionWebSocket();
  },

  data() {
    return {
      loginUserAvatar: require("@/assets/userAvatar-image/" + this.getUserAvatar()), // 登录用户本人的头像

      loginUserName: "", //登录用户本人的姓名

      dialogVisible: {  //控制聊天框是否显示
        display: "none",
      },

      toName: "",  //正在聊天的对方的名字

      customerNameList:"", //客服名称列表

      websocket:"", //WebSOcket连接对象

      connectionSuccess:false, //连接成功判断
      connectionFailure:true, //连接失败判断
      
    };
  },

  methods: {
    //获取登录的用户名
    getUserName() {
      const userName = window.sessionStorage.getItem("userName");
      //保存在浏览器上的用户信息是JSON字符串类型，把它转换成JSON对象类型就可以通过打点方式取出自己想要的值
      // const UserData = JSON.parse(userName);
      console.log("userName===>",userName)
      this.loginUserName = userName;
    },
    //获取登录的用户头像
    getUserAvatar() {
      const loginUserInfo = window.localStorage.getItem("loginUserInfo");
      //保存在浏览器上的用户信息是JSON字符串类型，把它转换成JSON对象类型就可以通过打点方式取出自己想要的值
      const UserData = JSON.parse(loginUserInfo);
      const avatarUrl = UserData.uavatar;
      return avatarUrl.substring(avatarUrl.lastIndexOf("/") + 1);
    },
    showChatView(name) {
      this.toName = name; //把想进行聊天的用户名赋给toName
      this.dialogVisible.display = "block"; //打开聊天框
    },

//   ======================================================================================================  
    // //WebSocket连接
    // connectionWebSocket(){
    //   console.log("正在创建WebSocket连接.......");

    //   //判断当前浏览器是否支持WebSocket
    //   if("WebSocket" in window){
    //     // const host = window.location.host;
    //     this.websocket = new WebSocket( "ws://localhost:9000/websocket" );
    //   }else{
    //     this.$message.error("此浏览器不支持WebSocket连接");
    //   }
      
    //   //↓给ws绑定事件↓

    //   //当WebSocket的连接状态readyState变为1时调用;这意味着当前连接已经准备好发送和接受数据。这个事件处理程序通过事件（建立连接时）触发。
    //   this.websocket.onopen = () => {
    //     this.connectionSuccess = true;  //开启连接成功显示
    //     this.connectionFailure = false; //关闭连接失败显示
    //   };

    //   //当浏览器(前端)收到来自服务器(后端)的消息时被调用的 EventHandler。它由一个MessageEvent调用（此处的event调用）
    //   this.websocket.onmessage = (event) => {
    //     //获取服务端推送过来的消息。由于推送过来的参数是String类型的，所以需要将它转换成JSON对象类型，这样就可以打点使用内部方法了
    //     const res = JSON.parse(event.data);
    //     // 判断是否是系统消息
    //     if(res.isSystem){
    //       //系统消息
    //       const customerNames = res.message;

    //       //循环展示每次获取到的客服信息到客服列表
    //       for(const customerName of customerNames){
    //         this.customerNameList = customerName;
    //       }

    //     }else{
    //       //不是系统消息
    //     }
    //   };


    //   this.websocket.onclose = () => {
    //     this.connectionSuccess = false;  //开启连接成功显示
    //     this.connectionFailure = true; //关闭连接失败显示
    //   };


    // }
//   ======================================================================================================  



  },
};
</script>


<style scoped>

/* -----聊天框容器----- */
.dialog_container {
  position: absolute;
  width: 900px;
  height: 600px;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  margin: auto auto;
  border: 5px ridge black;
  border-radius: 1%;
  box-shadow: 0 8px 8px 2px rgba(10, 16, 20, 0.24),
    0 0 8px 3px rgba(10, 16, 20, 0.12);
}



/* -----头部容器----- */
.header_container {
  height: 60px;
  width: 100%;
  border-bottom: 1px solid black;
}
/* 头部左侧本用户名 */
.login_user_name {
  height:36px;
  left: 10%;
  top: 2.5%;
  position: absolute;
  z-index: 51;
  color: white;
  font-size: 25px;
  font-weight: bold;
  text-shadow: 1px 1px white;
  /*线性渐变方向从上到下 */
  background: linear-gradient(rgba(61, 49, 49, 0.4), rgba(48, 48, 63, 0.4));
}
/* 头部左侧头像 */
.login_user_avatar {
  height: 60px;
  width: 25%;
  position: absolute;
  z-index: 50;
  box-shadow: 2px 5px 5px rgba(10, 16, 20, 0.24);
}
/* 头部连接成功判断 */
.Connection_judge_success{
  line-height: 60px;
  margin-left:250px;
  color:seagreen;
  font-weight: bold;
}
/* 头部连接失败判断 */
.Connection_judge_failure{
  line-height: 60px;
  margin-left:250px;
  color:lightcoral;
  font-weight: bold;
}

/* -----客服列表 + 聊天显示信息区域（做flex布局）----- */
.customer_chat_container {
  height: 100%;
  display: flex;
  justify-content: space-between;
}

/* 左侧 (客服列表 + 上线通知) 区域 */
.customer_listAndOnline{
  height:90%;
  width: 25%;
}
.customer_list_text{
  height:3%;
  text-align: center;
  color:rgb(187, 187, 187);
  font-weight: bold;
}
/* 客服列表区域 */
.customer_list_container {
  height: 77%;
  margin-right:-1px;
  padding-right:8px; /* 滚动条不占用内容框解决方案：步骤1 */
  border-bottom: 1px solid #000;
  overflow:hidden;
}
.customer_list_container:hover{
  overflow-y:scroll;
  padding-right:0px; /* 滚动条不占用内容框解决方案：步骤2 */
  scrollbar-color: rgb(211, 211, 211) rgba(32, 32, 32, 0); /* 滑块颜色  滚动条背景颜色 */
  scrollbar-width:thin; /* 滚动条宽度样式有三种：thin（椭圆长方形）、auto（正方形）、none（无） */
}

.customer_listAndOnline_ul {
  margin: 0;
  padding: 0;
  list-style: none;
}
.customer_list_li {
  height: 40px;
  margin-top:5px;
  padding-left:5px;
  font-size: 17px;
  text-shadow: 1px 1px 0px black;
  font-family: "Microsoft YaHei", 微软雅黑;
  cursor: default;
}
/* 客服列表 — 鼠标悬停时 */
.customer_list_li:hover {
  background-color: rgb(234, 234, 234);
}
/* 客服头像 */
.customer_avatar {
  width: 35px;
  height: 35px;
  margin-right: 10px;
  border-radius: 50%;
}
/* 客服名 */
.customer_a {
  display: flex;
  justify-content: flex-start; /*水平左侧*/
  align-items: center; /*垂直居中*/
  width: 100%;
}

/* 客服上线通知区域 */
.customer_online_container{
  height:17%;
  margin-right:-1px;
  padding-right:8px; /* 滚动条不占用内容框解决方案 */
  overflow:hidden;
}
.customer_online_container:hover{
  overflow-y:scroll;
  padding-right:0px;
  scrollbar-color: rgb(211, 211, 211) rgba(32, 32, 32, 0); /* 滑块颜色  滚动条背景颜色 */
  scrollbar-width:thin; /* 滚动条宽度样式有三种：thin（椭圆长方形）、auto（正方形）、none（无） */
}
.customer_online_text{
  height:3%;
  text-align: center;
  color:rgb(187, 187, 187);
  font-weight: bold;
}

.customer_online_li{
  margin-top:2px;
  padding-left:5px;
  font-size: 12px;
  color:rgb(82, 81, 81);
  text-shadow: 0px 0px 1px rgb(182, 182, 182);
  font-family: "Microsoft YaHei", 微软雅黑;
  cursor: default;
}



/* -----右侧聊天区域外部边框容器----- */
.chat_outer_container {
  /* height:538px; */
  height: 90%;
  width: 75%;
  border-left: 1px solid black;
}
/* 聊天区域内部展示容器 */
.chat_inner_container {
  height: 100%;
  width: 100%;
}
/* -----聊天显示消息区域----- */
.chat_show_message_area {
  height: 70%;
  border-bottom: 1px solid black;
}
/* 正在和谁聊天-显示框 */
#show_chatting_user{
  text-align: center;
}
/* 右侧聊天消息区域 */
.chat_right_show_message_area{
  text-align: right;
}
/* -----聊天输入内容区域----- */
.chat_input_message_area {
  height: 30%;
}
</style>