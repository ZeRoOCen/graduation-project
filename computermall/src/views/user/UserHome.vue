<template>
  <div>
    <el-container>

<!-- 头部区域，标签名同组件名 -->
      <Header />

<!-- 中部区域 -->
      <el-main class="main_container">

<!---- 中部分类区域 ---->
        <section class="main_sort_container">
          <div class="main_sort_title">整机</div>
          <el-row class="main_sort_row" type="flex" justify="space-around" >
            <el-button type="text"> 
              <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">一体机</span> </el-col> 
            </el-button>
            <el-button type="text" > 
              <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">台式机</span> </el-col> 
            </el-button>
            <el-button type="text" > 
              <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">平板电脑</span> </el-col> 
            </el-button>
            <el-button type="text" > 
              <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">游戏本</span> </el-col> 
            </el-button>
            <el-button type="text" > 
              <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">笔记本</span> </el-col> 
            </el-button>
            <el-button type="text" > 
              <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">轻薄本</span> </el-col> 
            </el-button>
          </el-row>

          <div class="main_sort_title">配件</div>
          <el-row class="main_sort_row" type="flex" justify="space-around" >
            <el-button type="text"> <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">CPU处理器</span></el-col></el-button>
            <el-button type="text"> <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">SSD固态硬盘</span></el-col></el-button>
            <el-button type="text"> <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">显卡</span></el-col></el-button>
            <el-button type="text"> <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">显示器</span></el-col></el-button>
            <el-button type="text"> <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">机箱外壳</span></el-col></el-button>
            <el-button type="text"> <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">组装机箱</span></el-col></el-button>
          </el-row>

          <div class="main_sort_title">外设</div>
          <el-row class="main_sort_row" type="flex" justify="space-around" >
            <el-button type="text"> <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">U盘</span></el-col></el-button>
            <el-button type="text"> <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">摄像头</span></el-col></el-button>
            <el-button type="text"> <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">移动硬盘</span></el-col></el-button>
            <el-button type="text"> <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">键盘</span></el-col></el-button>
            <el-button type="text"> <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">鼠标</span></el-col></el-button>
            <el-button type="text"> <el-col :span="3" class="main_sort_col"> <span class="main_sort_col_text">鼠标垫</span></el-col></el-button>
          </el-row>
        </section>

<!---- 中部图片区域(没有做数据库展示) ---->
        <section class="main_image_container">
          
          <div class="main_image_first">
             <el-button type="text" @click="go_xiaoxinAir15()"> <span class="main_image_word_first">点击进入 联想小新Air15</span> </el-button>
          </div>

          <div class="main_image_second">
             <el-button type="text" @click="go_GTX3060()"> <span class="main_image_word_second">点击立马购入 RTX 3090 最优惠价格</span> </el-button>
          </div>

          <div class="main_image_third">
             <el-button type="text" @click="go_HuaWei_MatePadPro()"> <span class="main_image_word_third">点击进入</span> </el-button>
          </div>

        </section>

        <!-- 中部活动区域 -->
        <section class="activiry_container">
          <button class="activity_main" v-for="goods in goodsActivityData" :key="goods.gname" @click="activityButton(goods.gname)">
            <div class="activity_img"></div>

            <div class="activity_word_container">
              <div class="activity_word">{{goods.gname}}</div>
              <div class="activity_word">价格：￥{{goods.gprice}}</div>
              <div class="activity_word">{{goods.gmessage}}</div>
            </div>
          </button>
        </section>


      </el-main>



<!---- 底部区域 ---->
      <el-footer>
        <!-- 此处仅载入图片作为底部功能 -->
        <section class="footer_container"></section>
      </el-footer>

    </el-container>
  </div>
</template>

<script>
//头部组件，组件名需要大写
import Header from '@/components/user/Header.vue'

export default {
  name:'userHome',

  components:{
    Header
  },

  created(){
    this.goodsActivity();
  },

  data(){
    return {
      goodId: "", //商品ID

      goodsActivityData: [], //活动商品信息
    };
  },

  methods:{

    //进入小新Air15的商品界面
    async go_xiaoxinAir15(){
      const goodId = 13;
      this.goodId = goodId.toString();
      const res = await this.$http.getUpdateGood({...this.goodId})
      const userHomeGoodInfo = JSON.stringify(res); //将Object类型转换成String类型才能存入sessionStorage中
      window.sessionStorage.setItem("userHomeGoodInfo",userHomeGoodInfo);
      this.$router.push( {path:"/good_interface"} );
    },

    //进入GTX3060的商品界面
    async go_GTX3060(){
      const goodId = 8;
      this.goodId = goodId.toString(); //number后端接收不到，要转换成String类型的才可以
      const res = await this.$http.getUpdateGood({...this.goodId})
      const userHomeGoodInfo = JSON.stringify(res); //将Object类型转换成String类型才能存入sessionStorage中
      window.sessionStorage.setItem("userHomeGoodInfo",userHomeGoodInfo);
      this.$router.push( {path:"/good_interface"} );
    },

    //进入华为MatePad Pro的商品界面
    async go_HuaWei_MatePadPro(){
      const goodId = 6;
      this.goodId = goodId.toString();
      const res = await this.$http.getUpdateGood({...this.goodId})
      const userHomeGoodInfo = JSON.stringify(res); //将Object类型转换成String类型才能存入sessionStorage中
      window.sessionStorage.setItem("userHomeGoodInfo",userHomeGoodInfo);
      this.$router.push( {path:"/good_interface"} );
    },

    //获取所有商品活动的信息
    async goodsActivity(){
      const resGoodsActivity = await this.$http.getGoodsActivity({});
      this.goodsActivityData = resGoodsActivity;
    },

    //通过活动商品名来获取商品所有信息并把信息保存进sessionstorage中再跳转至商品界面
    async activityButton(gname){
      const resGoodsData = await this.$http.activitySubmit({gname})
      const userHomeGoodInfo = JSON.stringify(resGoodsData); //将Object类型转换成String类型才能存入sessionStorage中
      window.sessionStorage.setItem("userHomeGoodInfo",userHomeGoodInfo);
      this.$router.push( {path:"/good_interface"} );
    }

  },
}
</script>


<style scoped>

/* ---------------中部区域--------------- */
.main_container{
  padding:0;
  margin:0;
}
/* rgb(16, 31, 43) */
.main_sort_container{
  /* ************************** */
  /* background-color: rgb(0, 0, 0); */
  background-color: rgb(255, 252, 246);
  /* background-image:url("../../assets/userHome-image/用户首页分类背景图-备选1.jpg"); */
  background-size:105%;
  background-position-y: -200px;
  width:100%;
  height:600px;
  overflow-x: hidden;
  /* ************************** */
  /* border-bottom:1px solid rgb(37, 37, 37); */
}

.main_sort_title{
  text-align:center;
  /* ************* */
  /* color:white; */
  color:black;
  font-size:25px;
  letter-spacing:5px;
  margin:50px 0px 10px 0px; 
  /* text-shadow: h-shadow v-shadow blur color */
  /* ********************************************** */
  /* text-shadow: 0px 0px 1px #dcfbff, 0px 0px 1px #dcfbff, 0px 0px 1px #dcfbff, 0px 0px 1px #dcfbff; */
  text-shadow: 0px 0px 1px #000000, 0px 0px 1px #000000;
}

.main_sort_col{
  margin-bottom:20px;
  text-align:center;
  margin-top:10px;
  width:170px;
  min-height: 36px;
  border-radius: 6px;
  /* ********************************************** */
  /* border: 1px solid white; */
  border: 1px solid black;
  /* box-shadow：color|阴影颜色|可选  h-shadow|水平偏移量|必选  v-shadow|垂直偏移量|必选 
             blur|模糊距离|可选 spread|阴影尺寸|可选  inset|内阴影|可选；*/
  /* ********************************************** */
  /* box-shadow:white 0px 0px 8px 1px; */
  box-shadow:black 0px 0px 3px 1px;
}

.main_sort_col_text {
  text-decoration:none;
  line-height:36px;
  /* ********************************************** */
  /* color:white; */
  color:black;
  letter-spacing:5px;
  /* text-shadow: h-shadow v-shadow blur color */
  /* ********************************************** */
  /* text-shadow: 0px 0px 2px #eed7d7, 0px 0px 2px #cab2b2, 0px 0px 2px #968787, 0px 0px 2px #837e7e; */
  text-shadow: 0px 0px 1px #000000, 0px 0px 1px #000000;
}

/* ----图片区域---- */
.main_image_container{
  width:100%;
}

.main_image_first{
  height:1200px;
  /* margin-top:-35px; */
  background-image:url("../../assets/userHome-image/联想小新Air15.jpg");
  background-repeat:no-repeat;
  background-size:100%;

}


.main_image_word_first{
  position:relative;
  left:620px;
  top:100px;
  font-size:30px;
  font-weight: bold;
  color:white;
  /* text-shadow: h-shadow v-shadow blur color */
  text-shadow: 0px 0px 3px #2c2c2c, 0px 0px 3px #2c2c2c, 0px 0px 3px #2c2c2c, 0px 0px 3px #2c2c2c;
}

.main_image_second{
  height:850px;
  background-image:url("../../assets/userHome-image/3090新款显卡概念图.jpg");
  background-repeat:no-repeat;
  background-size:100%;
}

.main_image_word_second{
  font-size:30px;
  color:white;
  position:relative;
  left:200px;
  top:100px;
  /* text-shadow: h-shadow v-shadow blur color */
  text-shadow: 0px 0px 5px #6e2222, 0px 0px 5px #255e79, 0px 0px 5px #2c7725, 0px 0px 5px #6a226d;
}

.main_image_third{
  height:700px;
  background-image:url("../../assets/userHome-image/华为平板MatePad pro.jpg");
  background-repeat:no-repeat;
  background-size:100%;
}

.main_image_word_third{
  font-size:30px;
  font-weight: bold;
  color:white;
  position:relative;
  left:1100px;
  top:480px;
  /* text-shadow: h-shadow v-shadow blur color */
  text-shadow: 0px 0px 5px #000000, 0px 0px 5px #000000, 0px 0px 5px #000000, 0px 0px 5px #000000;
}


/* 中部商品活动区域 */
.activiry_container{
  margin:20px 0px;
  display: flex;                                                             
  flex-wrap:wrap;
  justify-content: space-around;
}
.activity_main{
  display:flex;
  background-color:white;
  border:0px;
  cursor: pointer;
}
.activity_img{
  width:80px;
  height:80px;
  border: 1px solid gray;
}
.activity_word_container{
  margin-left:10px;
  width:300px; 
  display:flex;
  justify-content: flex-start;
  align-items: center;
  flex-wrap:wrap;
}
.activity_word{
  width:300px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow:ellipsis;
}



/* ---------------底部区域--------------- */
.footer_container{
  width:100%;
  height:460px;
  background-color: #F9F9F9;
  background-image:url("../../assets/userHome-image/用户首页底部图片.jpg");
  background-size:100%;
  /* background-repeat: no-repeat; */
}

</style>