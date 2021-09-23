/**
*
* describe: 联系用户备用界面
*
**/
<template>
  <div>
    <!-- 头部组件 -->
    <Header />
    <!-- 对话框容器 -->
    <div class="dialog_container">
      <!-- 头部区域 -->
      <section class="header_container">
        <!-- 客服头像 -->
        <img
          class="login_user_avatar"
          :src="loginUserAvatar"
          alt="头像加载中....."
        />
        <!-- 客服名 -->
        <span class="login_user_name">{{ loginUserName }}</span>
      </section>

      <!--主体区域
    用户列表和聊天区域容器（做flex布局用） -->
      <section class="customer_chat_container">
        <!-- 用户列表+用户上线通知 -->
        <section class="customer_listAndOnline">
          <div class="customer_list_text">用户列表</div>
          <!-- 用户列表 -->
          <div class="customer_list_container">
            <ul class="customer_listAndOnline_ul">
              <li
                class="customer_list_li"
                v-for="info in userList"
                :key="info.id"
              >
                <a
                  class="customer_a"
                  @click="showChatView(info.uname, info.id)"
                >
                  <!-- <img class="customer_avatar" :src="loginUserAvatar" alt="" /> -->
                  <div>{{ info.uname }}</div>
                </a>
              </li>
            </ul>
          </div>
        </section>

        <!-- 聊天区域外部边框控制容器 -->
        <section class="chat_outer_container">
          <!-- 聊天区域内部展示控制容器 -->
          <div class="chat_inner_container" :style="dialogVisible">
            <!-- <正在和谁聊天>显示框 -->
            <div id="show_chatting_user">正在和"{{ toName }}"聊天</div>
            <!-- 聊天消息展示区域 -->
            <section class="chat_show_message_area">
              <!-- 消息区域（控制聊天消息按照时间循环展示） -->
              <div v-for="messageInfo in chatMessageInfo" :key="messageInfo.id">
                <!-- 左侧对方消息区域 -->
                <div
                  v-if="messageInfo.name == toName"
                  class="chat_left_show_message_area"
                >
                  <!-- 历史消息区域 -->
                  <div class="history_left_userInfo">
                    <!-- <img class="customer_avatar" :src="customerAvatar" alt="" /> -->
                    <div class="message_name">{{ toName }}</div>
                    <div class="history_message_container">
                      <div class="history_message">
                        {{ messageInfo.message }}
                      </div>
                    </div>
                  </div>
                </div>
                <!-- 右侧本人消息区域 -->
                <div
                  v-else-if="messageInfo.name == loginUserName"
                  class="chat_right_show_message_area"
                >
                  <!-- 历史消息区域 -->
                  <div class="history_right_userInfo">
                    <!-- <img class="customer_avatar" :src="loginUserAvatar" alt="" /> -->
                    <div class="message_name">{{ loginUserName }}</div>
                    <!-- <div class="history_message_container">
                      <span class="history_message">{{
                        messageInfo.message
                      }}</span>
                    </div> -->

                    <span class="history_message_container">
                      <span class="history_message">{{
                        messageInfo.message
                      }}</span>
                    </span>
                  </div>
                </div>
              </div>
            </section>

            <!-- 聊天输入内容区域 -->
            <section class="chat_input_message_area">
              <textarea
                class="chat_textArea"
                placeholder="请输入消息"
                v-model="chatMessage"
                @keyup.enter="send_text_keydown"
              >
              </textarea>

              <el-button
                type="info"
                size="medium"
                class="submit_text"
                @click="text_cancel"
                >重置</el-button
              >
              <el-button
                type="primary"
                size="medium"
                class="submit_text"
                @click="send_text_click"
                >发送 (Ctrl+Enter)</el-button
              >
            </section>
          </div>
        </section>
      </section>
    </div>
  </div>
</template>

<script>
//头部组件，组件名需要大写
import Header from "@/components/user/Header.vue";

export default {
  name: "spareContactCustomer",

  components: {
    Header,
  },

  created() {
    this.getUserName();
    this.getUserAvatar();
    this.getUserList(); //获取该商品对应的用户列表
  },

  mounted() {},

  beforeDestroy(){
      clearInterval(this.messageTimer) //关闭定时器
  },

  data() {
    return {
      loginUserAvatar: require("@/assets/userAvatar-image/" +
        this.getUserAvatar()), // 登录用户本人的头像

      // customerAvatar: require("@/assets/handAdd-image/" +
      //   this.getCustomerAvatar()), //客服头像

      loginUserName: "", //登录的客服名

      dialogVisible: {
        //控制聊天框是否显示
        display: "none",
      },

      chatMessage: "", //聊天框输入内容

      toName: "", //正在聊天的对方的名字
      connectId: "", //唯一连接id

      userList: "", //用户信息列表
      chatMessageInfo: "", //聊天内容（发送人，发送消息内容，发送时间）

      messageTimer: '', //定时器开关
    };
  },

  methods: {
    //获取登录的用户名
    getUserName() {
      const userName = window.sessionStorage.getItem("userName");
      //保存在浏览器上的用户信息是JSON字符串类型，把它转换成JSON对象类型就可以通过打点方式取出自己想要的值
      // const UserData = JSON.parse(userName);
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
    //获取该客服对应的用户列表
    async getUserList() {
      const cname = this.loginUserName
      const unameList = await this.$http.getUserList({cname});
      this.userList = unameList;
    },
    //通过想要聊天的对象打开相应的聊天框
    async showChatView(uname, connectid) {
      clearInterval(this.messageTimer); //当切换聊天对象时，关闭定时器

      this.toName = uname; //把想进行聊天的用户名赋给toName
      this.connectId = connectid; //获取到它们的唯一id
      //根据他们的唯一id获取到他们之间的聊天信息
      const resMessage = await this.$http.getChatMessage({ connectid });
      this.chatMessageInfo = resMessage;
      
      this.dialogVisible.display = "block"; //打开聊天框

      this.messageTimer = setInterval(()=>{
        this.getMessageInfo()
      }, 1000); //打开定时器：每秒获取聊天信息
    },

    //获取聊天信息
    async getMessageInfo() {
      //根据他们的唯一id获取到他们之间的聊天信息
      const connectid = this.connectId;
      const resMessage = await this.$http.getChatMessage({ connectid });
      this.chatMessageInfo = resMessage;
    },

    //获取客服头像
    // async getCustomerAvatar(){
    //   let cname = "";
    //   const typeCname = typeof cname;
    //   const typeToName = typeof this.toName;
    //   if(typeCname != typeToName){
    //     return "1T西部数据SSD固态硬盘.jpg";
    //   }
    //   //通过客服名获取客服的头像
    //   cname = this.toName
    //   console.log("cname",cname)
    //   const customerAvatar = await this.$http.getAvatar({cname});
    //   console.log("2", customerAvatar)
    //   return customerAvatar;
    // },

    //重置聊天输入框内容
    text_cancel() {
      this.chatMessage = "";
    },

    //点击发送按钮发送消息
    async send_text_click() {
      const id = this.connectId; //唯一id
      const name = this.loginUserName; //发送消息的用户名称
      const message = this.chatMessage; //发送的消息内容
      //根据发送的信息来获取发送时间并将他们保存到聊天信息表中
      const sendJudge = await this.$http.sendChatMessage({ id, name, message });
      //如果发送信息成功，则清空消息内容
      if (sendJudge == "success") {
        this.chatMessage = ""; //将消息内容清空
      } else {
        this.$message.error("发送消息失败，请检查网络状态");
      }
      this.getMessageInfo(); //将发送的消息展示到历史消息中
    },

    //ctrl+enter快捷键发送消息
    async send_text_keydown(e) {
      //根据发送的信息来获取发送时间并将他们保存到聊天信息表中
      //用户点击了ctrl+enter触发
      if (e.ctrlKey && e.keyCode == 13) {
        const id = this.connectId; //唯一id
        const name = this.loginUserName; //发送消息的用户名称
        const message = this.chatMessage; //发送的消息内容
        //根据发送的信息来获取发送时间并将他们保存到聊天信息表中
        const sendJudge = await this.$http.sendChatMessage({
          id,
          name,
          message,
        });
        //如果发送信息成功，则清空消息内容
        if (sendJudge == "success") {
          this.chatMessage = ""; //将消息内容清空
        } else {
          this.$message.error("发送消息失败，请检查网络状态");
        }
        this.getMessageInfo(); //将发送的消息展示到历史消息中
      }
    },
  },
};
</script>

<style scoped>
/* -----聊天框容器----- */
.dialog_container {
  width: 900px;
  height: 600px;
  margin: 5px auto;
  border: 5px ridge black;
  border-radius: 1%;
  box-shadow: 0 8px 8px 2px rgba(10, 16, 20, 0.24),
    0 0 8px 3px rgba(10, 16, 20, 0.12);
}

/* -----头部容器----- */
.header_container {
  display:flex;
  align-items: center;
  height: 60px;
  width: 100%;
  border-bottom: 1px solid black;
}
/* 头部左侧本用户名 */
.login_user_name {
  height: 36px;
  /* width: 50px; */
  /* margin-left:-130px; */
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
  box-shadow: 2px 5px 5px rgba(10, 16, 20, 0.24);
}

/* -----客服列表 + 聊天显示信息区域（做flex布局）----- */
.customer_chat_container {
  height: 100%;
  display: flex;
  justify-content: space-between;
}

/* 左侧 (客服列表) 区域 */
.customer_listAndOnline {
  height: 90%;
  width: 25%;
}
.customer_list_text {
  height: 3%;
  text-align: center;
  color: rgb(187, 187, 187);
  font-weight: bold;
}
/* 客服列表区域 */
.customer_list_container {
  height: 77%;
  margin-right: -1px;
  padding-right: 8px; /* 滚动条不占用内容框解决方案：步骤1 */
  border-bottom: 1px solid #000;
  overflow: hidden;
}
.customer_list_container:hover {
  overflow-y: scroll;
  padding-right: 0px; /* 滚动条不占用内容框解决方案：步骤2 */
  scrollbar-color: rgb(211, 211, 211) rgba(32, 32, 32, 0); /* 滑块颜色  滚动条背景颜色 */
  scrollbar-width: thin; /* 滚动条宽度样式有三种：thin（椭圆长方形）、auto（正方形）、none（无） */
}

.customer_listAndOnline_ul {
  margin: 0;
  padding: 0;
  list-style: none;
}
.customer_list_li {
  display: flex;
  align-items: center;
  height: 40px;
  margin-top: 5px;
  padding-left: 5px;
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
  overflow-y: scroll;
  scrollbar-color: rgb(211, 211, 211) rgba(32, 32, 32, 0); /* 滑块颜色  滚动条背景颜色 */
  scrollbar-width: thin; /* 滚动条宽度样式有三种：thin（椭圆长方形）、auto（正方形）、none（无） */
  border-bottom: 1px solid black;
}
/* 正在和谁聊天-显示框 */
#show_chatting_user {
  text-align: center;
}
/* 历史用户聊天名 */
.message_name {
  color: rgb(61, 61, 61);
  margin: 0px 5px;
}
/* 历史聊天内容容器 */
.history_message_container {
  display: inline-block;
  margin: 10px 5px;
  border: 1px solid rgb(190, 190, 190);
  background-color: rgb(190, 190, 190);
  padding: 5px;
}
/* 历史聊天内容 */
.history_message {
  padding: 5px;
  border-radius: 10%;
}
/* 右侧聊天消息区域 */
.chat_right_show_message_area {
  text-align: right;
}
/* -----聊天输入内容区域----- */
.chat_input_message_area {
  height: 30%;
}
/* 聊天框 */
.chat_textArea {
  width: 99.3%;
  height: 55%;
  border: 0px solid black;
  outline: none; /* 隐藏选中时样式 */
  resize: none; /* 隐藏多行文本框拉伸 */
  scrollbar-color: rgb(211, 211, 211) rgba(32, 32, 32, 0); /* 滑块颜色  滚动条背景颜色 */
  scrollbar-width: thin; /* 滚动条宽度样式有三种：thin（椭圆长方形）、auto（正方形）、none（无） */
}
/* 提交聊天输入框内容按钮 */
.submit_text {
  margin-top: 5px;
  margin-left: 435px;
}
</style>