<template>
  <div class="good_interface_container">
    <!-- 头部区域 -->
    <Header />

    <!-- 商品界面主体区域 -->
    <section class="good_interface_main">
      <!-- 商品界面主体左部 -->
      <!-- <div class="good_interface_main_left">123</div> 撑开区域空间内容-->
      <div class="good_interface_main_left"></div>
      <!-- 商品界面主体中部 -->
      <div class="good_interface_main_middle">
        <!-- 商品信息区域 -->
        <div class="goodInfo_container">
          <!-- 商品信息——图片 -->
          <img class="good_img" :src="imgUrl" alt="" />
          <!-- 商品信息和按钮区域 -->
          <div class="good_InfoAndButton">
            <!-- 商品信息区域 -->
            <div class="good_info">
              <!-- 商品名 -->
              <div class="goodInfo_name">{{ goodInfo.gname }}</div>
              <!-- 商品类别 -->
              <div class="goodInfo_sort">
                {{ goodInfo.sgsort }} >> {{ goodInfo.mgsort }}
              </div>
              <!-- 商品消息区域 -->
              <div class="goodMessage">
                <span class="goodMessage_prefix_word">核心配置：</span>
                <span class="goodMessage_word">{{ goodInfo.gmessage }}</span>
              </div>
              <!-- 价格区域 -->
              <div class="goodInfo_price">
                <span class="goodInfo_price_prefix_word">购买价</span>
                <span class="goodInfo_price_sign">￥</span>
                <span class="goodInfo_price_word">{{ goodInfo.gprice }}</span>
              </div>
              <!-- 联系客服区域 -->
              <button class="contact_customer_button" @click="goto_contact_view">
                联系客服
              </button>
              <!-- 按钮区域 -->
              <div class="goodInfo_button">
                <el-button type="primary" @click="addCart"
                  >加入购物车</el-button
                >
                <el-button type="danger">立即购买</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 商品界面主体右部 -->
      <!-- <div class="good_interface_main_right">123</div>  撑开区域空间内容 -->
      <div class="good_interface_main_right"></div>
    </section>

    <!----- 评论区 + 编辑区 ----->
    <div class="comment_write_container">
      <!-- 评论区容器 -->
      <div class="comment_container">
        <!-- 商品评价提示语 -->
        <div class="comment_introduce">
          <span class="comment_introduce_word"
            >商品评价({{ commentTotalNum }})</span
          >
        </div>
        <!-- 用户评论展示区域 -->
        <div
          class="comment_show"
          v-for="comment in commentList"
          :key="comment.uname"
        >
          <!-- 如果评论内容不为空才显示 -->
          <div v-if="comment.gcomment != ''">
            <!-- 用户名 -->
            <div class="comment_uname">
              {{ comment.uname }}
            </div>
            <!-- 评分 -->
            <div class="comment_grade">
              <el-rate v-model="comment.grade" disabled :colors="commentColors">
              </el-rate>
            </div>
            <!-- 商品评论内容 -->
            <div class="comment_gcomment">
              {{ comment.gcomment }}
            </div>
          </div>
        </div>
      </div>
      <!-- 分页控件 -->
      <div class="pagination">
        <el-pagination
          @current-change="CurrentPageNumChanged"
          :current-page.sync="commentQueryInfo.currentPageNum"
          :page-size="commentQueryInfo.commentPageSize"
          layout="total, prev, pager, next"
          :total="commentTotalNum"
        >
        </el-pagination>
      </div>

      <!-- 商品评论区域 -->
      <div class="go_comment_container">
        <!-- 允许用户进行编辑界面 -->
        <div
          class="allow_user_comment"
          v-if="
            this.userInfo.uid == this.singleComment.uid &&
            this.singleComment.power == 1 &&
            this.singleComment.gcomment == '' &&
            this.singleComment.grade == 0
          "
        >
          <!-- 商品评价提示语 -->
          <div class="comment_input_title">
            <span class="comment_input_title_word">评论</span>
          </div>
          <!-- 商品打分 -->
          <div class="comment_grade">
            <div class="grade_word">商品评价</div>
            <el-rate v-model="gradeValue" :colors="commentColors"></el-rate>
          </div>
          <!-- 输入框区域 -->
          <div class="comment_write">
            <textarea
              v-model="commentWord"
              class="comment_input"
              rows="8"
              @input="judgeWordLength"
            ></textarea>
          </div>
          <!-- 剩余数字与提交按钮区域 -->
          <div class="remainWord_submitButton">
            <!-- 剩余可输入字数提示 -->
            <div class="remain_word_container">
              您还可以输入
              <span class="remain_word">{{ remainWordLength }}</span>
              个字
            </div>
            <!-- 提交按钮 -->
            <button class="submit_input_button" @click="comment_input_submit" type="primary"
              >提交<i class="el-icon-upload el-icon--right"></i
            ></button>
          </div>
        </div>
        <!-- 不允许用户进行编辑界面 -->
        <div v-else class="comment_input_notAllow">无法对该商品进行评价<span class="comment_input_notAllow_inner">/</span>已经评价了该商品</div>
      </div>
    </div>

    <!-- 底部商城描述 -->
    <div class="footer_container_allow" v-if="
            this.userInfo.uid == this.singleComment.uid &&
            this.singleComment.power == 1 &&
            this.singleComment.gcomment == '' &&
            this.singleComment.grade == 0
          ">
    </div>
    <div class="footer_container_notAllow" v-else></div>
  </div>
</template>

<script>
//头部组件，组件名需要大写
import Header from "@/components/user/Header.vue";

export default {
  name: "goodInterface",

  components: {
    Header,
  },

  created() {
    this.getUserInfo();
    this.getGoodInfo();
    this.getCommentGoodsName();
    this.getCommentList();
    this.getComment();
  },

  mounted() {},

  data() {
    return {
      userInfo: [], //登录的用户信息

      goodInfo: [], //商品信息

      contactNeedInfo: [], //跳转到联系客服界面需要的数据（用户名，商品名）

      imgUrl: require("@/assets/handAdd-image/" + this.getGoodInfo()),

      commentQueryInfo: {
        currentGoodsName: "", //当前商品名
        commentMessage: "", //查询到的评论内容
        currentPageNum: 1, //当前页（默认为1）
        commentPageSize: 5, //每页最大展示数量
      },

      commentTotalNum: 0, // 评论总数

      commentList: [], //商品评论内容列表
      commentColors: ["#99A9BF", "#F7BA2A", "#FF9900"], // 等同于 { 2: '#99A9BF', 4: { value: '#F7BA2A', excluded: true }, 5: '#FF9900' }

      singleComment: {}, //当前用户对该商品评论相关参数

      gradeValue:0, //用户对商品的评分

      commentWord: "", //评论输入框内容
      maxWordLength: 150, //输入框最大内容长度
      remainWordLength: 150, //剩余输入框最大内容长度
    };
  },

  methods: {
    //获取本地存储的用户信息
    getUserInfo() {
      const loginUserInfo = window.localStorage.getItem("loginUserInfo");
      this.userInfo = JSON.parse(loginUserInfo);
    },

    getGoodInfo() {
      //获取本地存储的商品信息
      const resStr = window.sessionStorage.getItem("userHomeGoodInfo"); //此时的resData是JSONString类型
      this.goodInfo = JSON.parse(resStr);
      return this.goodInfo.gimgurl;
    },

    //将浏览器存储中的当前商品名赋值给this.commentQueryInfo.currentGoodsName
    getCommentGoodsName() {
      const resStr = window.sessionStorage.getItem("userHomeGoodInfo"); //此时的resData是JSONString类型
      this.goodInfo = JSON.parse(resStr);
      this.commentQueryInfo.currentGoodsName = this.goodInfo.gname;
      console.log("111111111111111111111111111111",this.commentQueryInfo.currentGoodsName)
    },

    //加入购物车并进入个人页面
    async addCart() {
      let resDiscount = null;

      //获取用户信息
      const nameIdAndRole = {};
      const userInfo = window.localStorage.getItem("loginUserInfo");
      const user = JSON.parse(userInfo);
      nameIdAndRole.uid = user.uid;
      nameIdAndRole.uname = user.uname;
      nameIdAndRole.urole = user.urole;

      //将商品全部信息和用户的姓名、角色权限传入购物车数据库中(之前由于未理解封装问题，这两个对象没有放在一起传，现不做调整)
      const resGoodInfo = await this.$http.addGoodInfoToCart({
        ...this.goodInfo,
      });
      const resNameIdAndRole = await this.$http.addNameAndRoleToCart({
        ...nameIdAndRole,
      });

      //如果是会员，则对商品的价格进行优惠；否则不优惠（优惠金额等于0）
      const uid = user.uid.toString();
      const gname = this.goodInfo.gname;
      if (user.urole == 2) {
        resDiscount = await this.$http.getGoodsDiscount({ uid, gname });
      }else{
        resDiscount = 1;
      }
      if (resGoodInfo && resNameIdAndRole && resDiscount) {
        this.$message.success("加入购物车成功");
      } else {
        this.$message.error("加入购物车失败");
      }

      //更新购物车列表至汇总展示列表
      const updateJudge = await this.$http.updateSameGoods({ gname, uid });
    },
    
    //---进入联系客服界面 ---
    async goto_contact_view(){
      const urole = this.userInfo.urole;
      if(urole == 13){
        return this.$message.error("客服账户无法联系客服");
      }
      const uname = this.userInfo.uname;
      const gname = this.goodInfo.gname;
      this.contactNeedInfo.push(uname);
      this.contactNeedInfo.push(gname)
      window.localStorage.setItem("contactNeedInfo", this.contactNeedInfo);
      this.$router.push({path:"/spare_contact_customer"});
    },

    //获取商品评论每页列表内容
    async getCommentList() {
      const resData = await this.$http.getCommentList({
        ...this.commentQueryInfo,
      });
      const { goodsComment, commentTotalNumbers } = resData;
      this.commentList = goodsComment;
      this.commentTotalNum = commentTotalNumbers;
    },

    //获取该用户对应的该商品的所有商品评论
    async getComment() {
      const uid = this.userInfo.uid;
      const gname = this.goodInfo.gname;
      const resData = await this.$http.getComment({ gname, uid });
      this.singleComment = resData;
    },

    //处理当前页变化时的操作
    CurrentPageNumChanged(newPageNum) {
      this.commentQueryInfo.currentPageNum = newPageNum;
      this.getCommentList();
    },

    //判断输入框已输入的内容长度
    judgeWordLength() {
      this.remainWordLength = this.maxWordLength - this.commentWord.length;
    },

    //根据用户id，商品名提交输入框内容
    async comment_input_submit(){
      const uid = this.userInfo.uid
      const gname = this.goodInfo.gname
      const comment = this.commentWord
      const grade = this.gradeValue;
      if(grade == 0){
        return this.$message.error("请对商品进行评分")
      }
      if(comment == ""){
        return this.$message.error("请对商品进行评价")
      }
      const submitJudge = await this.$http.addComment({uid, gname, comment, grade})
      if(submitJudge == "success"){
        this.$message.success("评论提交成功")
        this.getComment();
      }else{
        this.$message.error("评论提交失败")
      }
    }
  },
};
</script>

<style scoped>
/* -----商品信息展示主体区域----- */
.good_interface_main {
  width: 100%;
  height: 400px;
  margin-top: 20px;
  /* border:1px solid black;   区域边框显示范围*/
  display: flex;
  justify-content: space-around;
}
/* 商品信息左部 */
.good_interface_main_left {
  width: 200px;
  height: 400px;
  /* border:1px solid black;    区域边框显示范围*/
}

/* 商品信息中部 */
.good_interface_main_middle {
  width: 800px;
  height: 400px;
  /* border:1px solid black;    区域边框显示范围*/
}
/* 商品信息区域 */
.goodInfo_container {
  display: flex;
  flex-wrap: nowrap; /*不换行*/
  width: 100%;
  height: 400px;
  /* border:1px solid black;    区域边框显示范围 */
}
/* 商品图片 */
.good_img {
  width: 300px;
  height: 300px;
  margin-top: 30px;
  margin-left: 50px;
  box-shadow: rgb(223, 223, 223) 0px 0px 10px 3px;
}
/* 商品信息和按钮模块 */
.good_InfoAndButton {
  width: 50px;
}
/* 商品信息区域 */
.good_info {
  width: 400px;
  height: 300px;
  margin-top: 30px;
  /* border:1px solid black; */
}
.goodInfo_name {
  margin-left: 10px;
  text-indent: 1em;
}
/* 商品信息类别 */
.goodInfo_sort {
  margin-top: 10px;
  text-align: center;
  color: rgb(129, 129, 129);
}
/* 商品信息区域 */
.goodMessage {
  margin-top: 10px;
  margin-left: 10px;
}
.goodMessage_prefix_word {
  text-shadow: 1px 1px 0px black;
}
.goodMessage_word {
  color: #666;
  font: 15px/150% tahoma, arial, Microsoft YaHei, Hiragino Sans GB,
    "\u5b8b\u4f53", sans-serif;
}
/* 商品价格区域 */
.goodInfo_price {
  margin-top: 20px;
  margin-left: 10px;
  display: flex;
  align-items: center;
}
.goodInfo_price_prefix_word {
  color: #666;
  font: 13px tahoma, arial, Microsoft YaHei, Hiragino Sans GB, "\u5b8b\u4f53",
    sans-serif;
}
.goodInfo_price_sign {
  margin-left: 5px;
  margin-right: 2px;
  color: #e4393c;
  font: 20px tahoma, arial, Microsoft YaHei, Hiragino Sans GB, "\u5b8b\u4f53",
    sans-serif;
}
.goodInfo_price_word {
  color: #e4393c;
  font: 25px tahoma, arial, Microsoft YaHei, Hiragino Sans GB, "\u5b8b\u4f53",
    sans-serif;
}
/* 商品按钮区域 */
.goodInfo_button {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}
/* 联系客服按钮 */
.contact_customer_button{
  height:25px;
  margin-top:10px;
  margin-left:10px;
  font-size:13px;
  font-weight: bold;
  color:#fff;
  background-color:rgb(255, 19, 235);
  box-shadow:0px 0px 2px 1px rgb(255, 19, 235);
  border:1px solid #fff;
  cursor:pointer;
}
.contact_customer_button:hover{
  background:rgba(255, 73, 240, 0.9);
}
.contact_customer_button:active{
  background:rgb(243, 0, 223);
}

/* 商品信息右部 */
.good_interface_main_right {
  width: 200px;
  height: 400px;
  /* border:1px solid black;    区域边框显示范围 */
}

/* 主体底部 —— 评论区 + 编辑区容器 */
.comment_write_container {
  width: 1000px;
  height: 530px;
  margin: 0 auto;
  background-color: rgba(241, 241, 241, 0.822);
}
/* 评论区容器 */
.comment_container {
  min-height: 530px;
  box-shadow: 0px 0px 5px 5px rgb(240, 240, 240);
}
.comment_introduce {
  display: flex;
  align-items: center;
  height: 50px;
  background-color: #e4393c;
  font: 700 17px "microsoft yahei";
  color: #fff;
}
.comment_introduce_word {
  margin-left: 10px;
}
.comment_show {
  background-color: rgb(253, 251, 246);
}
.comment_uname {
  margin-left: 40px;
  margin-bottom: 5px;
}
.comment_grade {
  margin-bottom: 5px;
}
.comment_gcomment {
  margin-bottom: 10px;
  text-indent: 1em;
}
/* 分页 */
.pagination{
  margin-top:10px;
}

/* 用户对商品评论编辑区 */
.go_comment_container {
  height: 500px;
  width: 100%;
}
/* 评论提示语 */
.comment_input_title {
  display: flex;
  align-items: center;
  height: 50px;
  background: linear-gradient(45deg, rgb(231, 0, 181) 40%, #e4393c 60%);
  font: 700 17px "microsoft yahei";
  color: #fff;
  /* margin-bottom:10px; */
}
/* 商品打分区域 */
.comment_grade{
  display:flex;
  justify-content: left;
  margin:10px 0px;
}
.grade_word{
  margin-right:10px;
}
.comment_input_title_word {
  margin-left: 10px;
}
/* 评论文本编辑区域 */
.comment_write {
  width: 100%;
  margin-top: -6px;
}
/* 评论输入框 */
.comment_input {
  height: 200px;
  width: 99.5%;
}
/* 剩余数字与提交按钮区域 */
.remainWord_submitButton{
  display:flex;
  justify-content: space-between;
}
.remain_word_container {
  display: flex;
  align-items: center;
}
.remain_word {
  color: fuchsia;
  font-weight: bold;
  font-size: 20px;
  padding: 0px 10px;
}
.submit_input_button{
  height:40px;
  width:150px;
  margin-top:5px;
  color:#fff;
  background-color:rgb(55, 155, 255);
  border:1px solid #fff;
  cursor:pointer;
}
.submit_input_button:hover{
  background:rgb(64, 158, 255, 0.9)
}
.submit_input_button:active{
  background:rgb(36, 143, 250)
}
/* 不允许用户编辑评论界面 */
.comment_input_notAllow{
  height:100px;
  line-height:100px;
  margin-top:10px;
  text-align: center;
  color:rgb(192, 0, 192);
  font-weight: bold;
  border-top:1px solid black;
}
/* 不允许用户编辑评论界面---斜杠 */
.comment_input_notAllow_inner{
  text-shadow: 1px 1px rgb(192, 0, 192);
}
/* ---商品界面底部--- */
.footer_container_allow {
  margin-top: 600px;
  height: 550px;
  background-color: #f9f9f9;
  background-image: url("../../assets/goodsInterFace-image/商品界面底部图片.png");
  background-size: 100%;
}
.footer_container_notAllow {
  margin-top: 200px;
  height: 550px;
  background-color: #f9f9f9;
  background-image: url("../../assets/goodsInterFace-image/商品界面底部图片.png");
  background-size: 100%;
}
</style>