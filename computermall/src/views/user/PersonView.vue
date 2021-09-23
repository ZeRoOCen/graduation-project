<template>
  <div>
    <!-- 头部区域 -->
    <Header />

    <!-- 主体区域 -->
    <section class="main_container">
<!-- style="display:flex;justify-self: flex-end;" -->
      <!-- 主体左侧区域 -->
      <section class="main_left_container">
        <template>
          <el-tabs :tab-position="tabLeftPosition" style="height: 600px">
            <el-tab-pane label=".我的资料.">  <Person-info />  </el-tab-pane>
            <el-tab-pane label=".会员中心.">会员中心</el-tab-pane>
            <el-tab-pane label=".我的积分.">我的积分</el-tab-pane>
            <el-tab-pane label=".积分商品.">积分商品</el-tab-pane>
          </el-tabs>
        </template>
      </section>

      <!-- 主体右侧区域 -->
      <section class="main_right_container">
        <template>
          <el-tabs :tab-position="tabRightPosition" style="height: 600px">
            <el-tab-pane label=".我的订单.">  <Goods-order />  </el-tab-pane>
            <el-tab-pane label="..购物车..">  <Shopping-cart />  </el-tab-pane>
            <el-tab-pane label=".收藏商品.">收藏商品</el-tab-pane>
            <el-tab-pane label=".历史浏览.">历史浏览</el-tab-pane>
          </el-tabs>
        </template>
      </section>

    </section>
  </div>
</template>

<script>
import Header from "@/components/user/Header.vue";
import PersonInfo from "@/components/user/PersonInfo.vue"
import ShoppingCart from "@/components/user/ShoppingCart.vue"
import GoodsOrder from "@/components/user/GoodsOrder.vue"

export default {
  name: "personView",
  components: {
    Header,
    PersonInfo,
    ShoppingCart,
    GoodsOrder
  },

  data() {
    return {
      tabLeftPosition: "right",
      tabRightPosition: "left",
    };
  },
};
</script>

<style scoped>

.main_container{
    width:100%;
    overflow-x:hidden;
    background-color: #f4f5f7;
}

.main_left_container{
    position:relative;
    min-width:600px;
    max-width:48%;
    top:30px;
    background-color: rgb(255, 255, 249);
    margin-left:25px;
}

.main_right_container{
    position:relative;
    min-width:10px;
    width:50%;
    top:-570px;
    left:48%;
    background-color: rgb(255, 255, 249);
}

.main_left_tab{
    display:flex;
    justify-self: flex-end;
}

</style>