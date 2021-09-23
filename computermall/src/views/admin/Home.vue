<template>
  <!-- 全局container -->
  <el-container class="home_container">
    <!-- 头部 -->
    <el-header class="header_container">
      <div class="header_left_container">
        <img class="header_logo" src="../../assets/dinosaur-logo.jpg" alt="" />
        <span class="header_word">电脑销售商城</span>
      </div>

      <span class="header_button_container">
        <el-button class="header_button" @click="logout" type="info" plain round
          >退出</el-button
        >
      </span>
    </el-header>
    <!-- 中部主体 -->
    <el-container class="main_container">
      <!-- 主体->侧边栏 -->
      <el-aside class="aside_container" width="210px">
        <!-- 后台管理菜单栏   
        unique-opened：同时只能展开一个主菜单
        :router:true：router是否使用 vue-router 的模式，true启用该模式会在激活导航时以 index的值 作为 path 进行路由跳转
        :default-active：设置默认激活菜单的index-->
        <el-menu class="aside_menu_container" background-color="#545c64" text-color="rgb(23, 255, 157)" active-text-color="#ffd04b"
        :router="true"
        :default-active="activePath">
          <!-- 一级菜单（主菜单），展示主菜单中的mtitle信息-->   
          <!-- :index中使用 mpath/mid+'' 两种方法都是可以的，效果是一样的-->
          <!-- :index="firstItem.mpath" 当点击某个主菜单时，可以单独弹出此主菜单的子菜单，如果不设置，当点击某个主菜单时，所有的子菜单都会弹出 -->
          <!-- :index=" firstItem.mid+''"   因为firstItem.mid是int类型，:index只接受字符串类型，所以需要进行字符串拼接-->
          <el-submenu :index="firstItem.mid+''" v-for="firstItem in AsideMenuList" :key="firstItem.mid">
            <template slot="title">
              <i v-if=" firstItem.mtitle == '信息查询管理' " class="el-icon-notebook-1"></i>
              <i v-else-if=" firstItem.mtitle == '商城活动管理' " class="el-icon-present"></i>
              <i v-else-if=" firstItem.mtitle == '权限控制管理' " class="el-icon-trophy"></i>
              <i v-else class="el-icon-loading"></i>
              <span>{{firstItem.mtitle}}</span>
            </template>
            <!-- 二级菜单，展示子菜单中的stitle信息 -->
            <!-- :index="secondItem.spath"  如果不设置，当点击主菜单弹出子菜单时，会选中所有子菜单 -->
            <el-submenu :index="secondItem.spath" v-for="secondItem in firstItem.sList" :key="secondItem.sid" >
              <template slot="title">
              <i v-if=" secondItem.stitle == '用户信息列表' " class="el-icon-user"></i>
              <i v-else-if=" secondItem.stitle == '商品信息列表' " class="el-icon-shopping-bag-1"></i>
              <i v-else-if=" secondItem.stitle == '活动商品列表' " class="el-icon-sell"></i>
              <i v-else class="el-icon-loading"></i>
              <span>{{secondItem.stitle}}</span>
              </template>
              <!-- 三级菜单 
                   @click="saveNavState(secondItem.spath)"：添加一个点击事件，当点击某一个子菜单时，保存它的路径，使刷新页面后也显示此菜单的选中高亮
                   v-show="thirdItem.tid"。当三级菜单的tid不等于0时显示，否则隐藏。
                      由于后端sql语句的left join语法，当三级菜单没有匹配二级菜单时，会返回三级菜单空值null而不是不返回值-->
              <el-menu-item :index="thirdItem.tpath" v-for="thirdItem in secondItem.tList" :key="thirdItem.tid" 
                            @click="saveNavState(thirdItem.tpath)" v-show="thirdItem.ttitle">
                <template slot="title">
                <i v-if=" thirdItem.ttitle == '用户信息' " class="el-icon-user-solid"></i>
                <i v-else-if=" thirdItem.ttitle == '全部商品信息' " class="el-icon-s-goods"></i>
                <i v-else-if=" thirdItem.ttitle == '全部活动商品信息' " class="el-icon-s-shop"></i>
                <i v-else class="el-icon-loading"></i>
                <span>{{thirdItem.ttitle}}</span>
                </template>
              </el-menu-item>
            </el-submenu>
          </el-submenu>
        </el-menu>
      </el-aside>
      <!-- 主体->内容 -->
      <el-main class="content_container">
        <!-- 展示子组件的内容，比如UserList.vue用户信息列表页面、商品信息列表页面 -->
        <router-view></router-view>
        </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name:'home',

  //在模板渲染成html前调用，即通常初始化某些属性值，然后再渲染成视图。
  created() {
    this.getAsideMenuList(); //this指向methods
    this.activePath = window.sessionStorage.getItem("activePath"); //取出saveNavState(activePath)方法中存放在session里的activePath
  },

  data() {
    return {
      activePath:"",
      AsideMenuList: [],
    };
  },

  methods: {
    //获取侧边栏菜单列表信息
    async getAsideMenuList() {
      // const {data:res} = await this.$http.get("menus");//使用axios通过get请求发送给/menus页面，/menus对应的是后端的MenuController，无参数返回
      const res = await this.$http.getMenus({})
      if(res){
        //此时的AsideMenuList已经获取到了主、子菜单的信息了；res.menus需要打点来指定获取到的内容，如果直接res，是无法获取到res的属性里面的值的
        this.AsideMenuList = res.menus; 
        // console.log(res.menus); //查看res里面保存的数据
      }
    },
    //退出按钮的方法
    logout() {
      window.localStorage.clear(); //手动清除保存的用户信息 index.js下的window.localStorage.getItem("allUser");
      this.$router.push("/login"); 
    },
    //保存路径
    saveNavState(activePath){
      window.sessionStorage.setItem("activePath",activePath);//把当前激活的子菜单路径保存在session中。使用sessionStorage，关闭页面后重新打开就不会保存高亮状态了
      // this.activePath = activePath;  //似乎删除这句话也不会影响
    }

  },

};
</script>

<style scoped>
.home_container {
  height: 100%;
}

.header_container {
  /* ************************ */
  /* background-color: #ffb3b3; */
  background-color:rgb(228, 228, 228);
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header_left_container {
  display: flex;
  align-items: center;
}

.header_logo {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}

.header_word {
  margin-left: 15px;
  font-size: 22px;
  /* ************* */
  /* color: #6affe4; */
  color:rgb(31, 31, 31);
  font-weight: bold;
  /* ************* */
  /* text-shadow: 1px 1px 2px #5698ff; */
  text-shadow: 1px 1px 0px #313131;
}

.header_button_container {
}

.header_button {
}

.main_container {
}

.aside_container {
  /* background-color: darkgray; */
  /* width:200px;  无法使用，el自带的css样式中的width优先级高，需要在行内样式直接修改*/
}

.aside_menu_container {
  font-weight: bold;
  border-right:none; /*如果不设置，子菜单的右侧边会凸出*/
}

.content_container {
  /* background-color: #fff2dd; */
  background-color: #fffaf2;
}
</style>
