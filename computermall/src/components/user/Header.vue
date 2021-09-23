<template>
  <div>
    <!---- 头部区域 ---->
    <el-header class="header_container" height="80px">
      <div class="header_left" @click="header_left_word_router">电脑销售商城</div>
      <div class="header_middle">
        <!--下拉查询输入框
            clearable:显示删除小按钮
            prefix-icon="el-icon-search":显示前置查询图标
            v-model="state":绑定data中的state数据（此数据为显示下拉列表的内容
            :fetch-suggestions="querySearchAsync"：返回输入建议的方法
            :trigger-on-focus="false"：输入框变为焦点时，不显示输入建议
            value-key = "gname"：输入建议对象中用于显示匹配的键名-->
        <el-autocomplete
          clearable
          class="header_middle_input"
          size="small"
          prefix-icon="el-icon-search"
          v-model="state"
          :fetch-suggestions="querySearchAsync"
          placeholder="请输入内容"
          :trigger-on-focus="true"
          @select="handleSelect"
          value-key="gname"
        ></el-autocomplete>
      </div>
      <!-- 头部右侧：联系客服、用户头像、用户信息、退出按钮 -->
      <div class="header_right">
        <!-- 联系客服界面 -->
        <el-tooltip class="header_right_customer" effect="dark" content="联系客服" placement="bottom">
          <div class="header_right_to_customer_url" @click="toCustomerUrl">
            <img class="header_right_customer_img" :src="contactCustomerImg" alt="">
          </div>
        </el-tooltip>
        <!-- 用户头像 -->
        <img
          class="header_right_avatar"
          :src="loginUserAvatar"
          @click="enterPersonView"
          alt="头像加载中....."
        />
        <!-- 用户信息 -->
        <el-button type="text" size="small" @click="enterPersonView"
          ><span class="header_right_UserInfo">{{
            loginUserUname
          }}</span></el-button
        >
        <!-- 退出按钮 -->
        <el-button type="text" size="small" @click="logout"
          ><span class="header_right_logoutButton">退出</span></el-button
        >
      </div>
    </el-header>
  </div>
</template>

<script>
export default {
  name: "userHeader",

    //created:在模板渲染成html前调用，即通常初始化某些属性值，然后再渲染成视图
  created(){
    this.getUserUname();
    this.getUserAvatar();
  },
  mounted(){
  },

  data() {
    return {
      state: "",
      timeout: null,
      //只有通过require才能通过图片地址获取到图像；require不能直接解析变量，所以需要加上固定值（如此题的固定的地址前缀），才能配合变量使用
      //  @/：src目录下的内容
      loginUserAvatar:require("@/assets/userAvatar-image/" + this.getUserAvatar()),
      loginUserUname:"",

      //联系客服图标
      contactCustomerImg:require("@/assets/header-component-image/联系客服.png/"),
    };
  },
  methods: {

    //点击头部左边文字进入用户首页
    header_left_word_router(){
        this.$router.push({path:"/userhome"});
    },

    //点击头部右边联系客服图标进入客服总页面
    toCustomerUrl(){
      // this.$router.push({path:"/contact_customer_service"}); //进入客服界面（websocket）
      const infoData = window.localStorage.getItem("loginUserInfo");
      const data = JSON.parse(infoData);
      const userRole = data.urole;
      if(userRole < 13){
        this.$router.push({path:"/spare_contact_customer"})
      }else{
        this.$router.push({path:"/spare_customer_view"})
      }
    },

    //查询输入框（含输入建议）
    //queryString：输入框中显示的内容
    //cb：callback简写，返回输入库中的内容。   cb(results)：可以将results中的数据进行显示在下拉框中
    // createStateFilter：函数用来判断下拉内容与输入内容是否匹配的
    // state：下拉框显示的内容
    // toLowerCase()：将字符串转换为小写
    // indexOf()：返回某个指定的字符串值在字符串中首次出现的位置
    // state.gname.toLowerCase().indexOf(queryString.toLowerCase()) === 0: 在头部(index=0)的位置匹配数据框中的内容与下拉菜单的内容是否一致
    // state.gname.toLowerCase().indexOf(queryString.toLowerCase()) !== -1; 输入框中的内容只要在下拉菜单中的任意位置存在，则匹配成功
    async querySearchAsync(queryString, cb) {
        const res = await this.$http.getGoodInfoByName({ queryString });
        var results = queryString ? res.filter(this.createStateFilter(queryString)) : res;
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
          cb(results);
        }, 1000 * Math.random());
      },
      createStateFilter(queryString) {
        return (state) => {
          return (state.gname.toLowerCase().indexOf(queryString.toLowerCase()) !== -1);
        };
      },
      handleSelect(goodInfo) {
        const userHomeGoodInfo = JSON.stringify(goodInfo); //将Object类型转换成String类型才能存入sessionStorage中
        window.sessionStorage.setItem("userHomeGoodInfo",userHomeGoodInfo);
        this.$router.push( {path:"/good_interface"} );
      }, 

      //头部用户头像显示
      getUserAvatar(){
          //获取保存在浏览器上的用户信息
        const getLocalData = window.localStorage.getItem("loginUserInfo"); 
          //保存在浏览器上的用户信息是JSON字符串类型，把它转换成JSON对象类型就可以通过打点方式取出自己想要的值
        const getUserAvatar = JSON.parse(getLocalData);
        const avatarUrl = getUserAvatar.uavatar;
          //返回头像名称+后缀
            // substring(start,end)获取start-end索引值之间的字符串（包括start索引值，不包括end索引值），如果只有start，则从start到结尾  
            // lastIndexOf（从最后一个此字符串开始索引）
        return avatarUrl.substring(avatarUrl.lastIndexOf("/")+1);
      },

      //头部用户信息显示
      getUserUname(){
          //获取保存在浏览器上的用户信息
        const getLocalData = window.localStorage.getItem("loginUserInfo"); 
          //保存在浏览器上的用户信息是JSON字符串类型，把它转换成JSON对象类型就可以通过打点方式取出自己想要的值
        const getUserUname = JSON.parse(getLocalData);
        this.loginUserUname = getUserUname.uname;
      },

      //点击头部用户信息，跳转到用户个人中心
      enterPersonView(){
        this.$router.push("/PersonView");
      },

      //头部退出按钮，退出到登录页
      logout(){
      window.localStorage.clear(); //手动清除保存的用户信息 index.js下的window.localStorage.getItem("allUser");
      this.$router.push("/login"); 
      }

    },
};
</script>

<style scoped>
/* ---------------头部区域--------------- */
/* rgb(39, 72, 98) rgb(33, 61, 83) */
.header_container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: rgb(3, 39, 46);
  border-bottom: 1px solid #ecebeb;
}

.header_left {
  font-size: 26px;
  font-weight: bold;
  width: 200px;
  margin-left:20px;
  color: #fefefe;
  cursor:pointer;
  text-shadow: 0px 1px 0px #c0c0c0, 0px 1px 0px #b0b0b0, 0px 1px 0px #a0a0a0,
    0px 1px 0px #909090, 1px 1.3px 1.3px #c0c0c0;
}

.header_middle_input {
  width: 350px;
}

.header_right {
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.header_right_to_customer_url{
  margin-right:15px;
  cursor:pointer;
}

.header_right_customer_img{
  width:40px;
  height:40px;
  background-color:white;
  border-radius:30%;
}

.header_right_avatar {
  width: 45px;
  height: 45px;
  border: 2px outset white;
  border-radius: 80%;
  cursor:pointer;
}

.header_right_UserInfo {
  margin:0px 0px 0px 10px;
  font-size: 21px;
  color: white;
  background-color: rgb(3, 39, 46);
  text-shadow: 0px 1px 0px #c0c0c0, 0px 1px 0px #b0b0b0, 0px 1px 0px #a0a0a0,
    0px 1px 0px #909090, 1px 2px 2px #c0c0c0;
}

.header_right_logoutButton {
  color: white;
  font-size: 21px;
  background-color: rgb(3, 39, 46);
  text-shadow: 0px 1px 0px #c0c0c0, 0px 1px 0px #b0b0b0, 0px 1px 0px #a0a0a0,
    0px 1px 0px #909090, 1px 2px 2px #c0c0c0;
}
</style>