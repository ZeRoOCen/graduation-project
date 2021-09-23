<template>
  <div>
    <!-- :header-cell-style="{textAlign: 'center'}" 表头内容居中
    :cell-style="{ textAlign: 'center' }" 表格内容居中-->
    <el-table
      :data="
        orderInfo.filter(
          (data) =>
            !search ||
            data.ordernumber.toLowerCase().includes(search.toLowerCase())
        )
      "
      :header-cell-style="{ textAlign: 'center' }"
      :cell-style="{ textAlign: 'center' }"
      style="width: 100%"
      stripe
    >
      <el-table-column label="订单号" prop="ordernumber" width="80px">
      </el-table-column>
      <el-table-column label="金额" prop="totalprice" width="55px">
      </el-table-column>
      <el-table-column label="下单时间" prop="ordertime" width="90px">
      </el-table-column>
      <el-table-column label="付款时间" prop="paytime" width="90px">
      </el-table-column>
      <el-table-column label="状态" prop="paystate" width="80px">
        <!-- slot="header" 自定义表头 -->
      </el-table-column>
      <el-table-column align="right">
        <template slot="header">
          <el-input v-model="search" size="mini" placeholder="输入订单号" />
        </template>
      </el-table-column>
      <el-table-column align="right">
        <template v-slot="slotProps">
          <!-- 付款按钮 -->
          <el-button
            size="mini"
            v-if="slotProps.row.paystate == 0"
            @click="paymentButton(slotProps.row.ordernumber)"
            type="success"
            class="payment_button"
            >付 款
          </el-button>
          <div class="comment_cancelOrder_container">
            <!-- 取消订单按钮 -->
            <el-button
              size="mini"
              type="danger"
              v-if="slotProps.row.paystate == 1"
              @click="orderCancel(slotProps.row.ordernumber)"
              class="cancelOrder_button"
              >取消订单</el-button
            >
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 付款界面 -->
    <el-dialog title="确认付款" :visible.sync="paymentVisible" width="30%">
      <span>是否支付订单费用，付款金额为：{{ this.orderRow.totalprice }}</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="paymentVisible = false">取 消</el-button>
        <el-button type="primary" @click="paymentOrder(orderRow.ordernumber)"
          >付 款</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "goodsOrder",

  created() {
    this.getOrderInfo();
  },

  mounted() {},

  updated() {},

  data() {
    return {
      orderInfo: [], //订单信息
      search: "",

      orderRow: {}, //每一行的订单信息

      paymentVisible: false, //付款界面
    };
  },

  methods: {
    //获取订单信息
    async getOrderInfo() {
      const loginUserInfo = window.localStorage.getItem("loginUserInfo");
      const userInfo = JSON.parse(loginUserInfo);
      const uid = userInfo.uid;
      const resOrderData = await this.$http.getOrderInfo({uid});
      this.orderInfo = resOrderData;
    },

    //支付按钮
    async paymentButton(ordernumber) {
      this.paymentVisible = true;
      const orderRow = await this.$http.getOrderRow({ ordernumber }); //获取每一行的订单信息
      this.orderRow = orderRow;
    },

    //进行付款操作
    async paymentOrder(ordernumber) {
      const payJudge = await this.$http.paymentOrder({ ordernumber });
      if (payJudge == "success") {
        this.getOrderInfo();
        this.paymentVisible = false;
        this.$message.success("付款成功");
      }
    },

    //删除订单
    async orderCancel(ordernumber) {
      const cancelJudge = await this.$http.orderCancel({ ordernumber });
      if (cancelJudge == "取消订单成功") {
        this.getOrderInfo();
      } else {
        this.$message.error(cancelJudge);
      }
    },
  },
};
</script>

<style scoped>
/* 付款按钮 */
.payment_button {
  margin-left: 10px;
}

/*  取消订单按钮 */
.cancelOrder_button {
  margin-top: 5px;
}
</style>