<template>
    <section class="shoppingcart_container">
        <!-- 用户信息确认区域 -->
        <div class="userInfo_container">
            <!-- 用户地址 -->
            <div class="userInfo_uaddress">{{userInfo.uaddress}}</div>
            <div class="userInfo_uname_umobile">
                <span class="userInfo_uname">{{userInfo.uname}}</span>
                <span class="userInfo_umobile">{{userInfo.umobile}}</span>
            </div>
        </div>
        <!-- 购物车列表展示 -->
        <div class="shoppingcart_info">
            <!-- :header-cell-style="{textAlign: 'center'}" 表头内容居中
                 :cell-style="{ textAlign: 'center' }" 表格内容居中-->
            <el-table :data="cartInfo" height="350px" class="el_table_container" :header-cell-style="{textAlign: 'center'}" :cell-style="{ textAlign: 'center' }">
                <el-table-column prop="gname" label="商品名"></el-table-column>
                <el-table-column prop="gprice" label="商品单件价格"></el-table-column>
                <el-table-column prop="amount" label="商品数量" >
                    <template v-slot="slotProps" >
                        <div class="amount_choose">
                            <el-input-number v-model="slotProps.row.amount" @change="amountChanged(slotProps.row.amount, slotProps.row.gname)" :min="0" :max="99" size="mini"></el-input-number>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="favorableprice" label="会员优惠价格(单件)"></el-table-column>
            </el-table>
        </div>
        <!-- 购物车价格展示 -->
        <div class="shoppingcart_price">
            <!-- 总价格 -->
            <div class="total_price">
                <span class="total_price_left">总价格：</span>
                <span class="total_price_sign">￥</span>
                <span class="total_price_right">{{totalPrice}}</span>
            </div>
            <!-- 总优惠 -->
            <div class="total_discount">
                <span class="total_discount_left">累计优惠：</span>
                <span class="total_discount_sign">￥</span>
                <span class="total_discount_right">{{totalDiscount}}</span>
            </div>
        </div>
        <!-- 提交至订单按钮 -->
        <div class="submit_order_button">
            <el-button type="danger" @click="submit_order">提交订单</el-button>
        </div>

        <el-dialog title="确认订单信息" :visible.sync = "cofirmOrderVisible" width="30%" :before-close="sumbitOrder_close">
            <div>您的订单已经生成，请确认订单信息并且进行付款</div>
            <div><span>用户名：</span><span>{{this.userInfo.uname}}</span></div>
            <div><span>付款地址：</span><span>{{this.userInfo.uaddress}}</span></div>
            <div><span>付款金额：</span><span>{{this.totalPrice}}</span></div>

            <span slot="footer" class="dialog-footer">
                <el-button @click="cancelOrder()">取 消 订 单</el-button>
                <el-button type="primary" @click="createPayTime()">付 款</el-button>
            </span>
        </el-dialog>
        
    </section>
</template>

<script>
export default {
    name:'shoppingCart',

    created(){
        this.getUserInfo();
    },

    mounted(){
        this.getCartGood();
    },
    updated(){
    },

    data(){
        return{
            cartInfo: [], //商品信息和用户角色权限
            totalPrice: 0, //商品总价
            totalDiscount: 0, //商品总优惠

            userInfo:{}, //用户信息

            orderNumber:"", //订单号
            cofirmOrderVisible: false, //确认订单信息框
        }
    },
    
    methods:{
        //根据用户ID获取购物车商品信息和用户角色权限
        async getCartGood(){
            //获取购物车商品汇总展示列表
            const uid = this.userInfo.uid;
            const resGoodsCartInfo = await this.$http.getGoodsCartInfo({uid});
            this.cartInfo = resGoodsCartInfo;

            let TotalPrice = 0.00;//商品总价
            let TotalDiscount = 0.00;//总优惠价格

            //循环获得购物车商品总价和商品总优惠
            for(const i in this.cartInfo){
                const goodsPrice = this.cartInfo[i].gprice;
                const amount = this.cartInfo[i].amount;
                const goodsDiscount = this.cartInfo[i].favorableprice;
                TotalPrice += goodsPrice * amount - goodsDiscount * amount;
                TotalDiscount += goodsDiscount * amount;
            }
            this.totalPrice = parseFloat(TotalPrice);
            this.totalDiscount = TotalDiscount;
        },

        //获取用户信息
        getUserInfo(){
            const loginUserInfo = window.localStorage.getItem("loginUserInfo");
            this.userInfo = JSON.parse(loginUserInfo);
        },

        //当购物车商品数量发生变化时，把变化的数量传递给数据库中的amount进行变更，然后再次执行getCartGood()方法来更新购物车信息
        async amountChanged(amount, gname){
            await this.$http.amountChanged( {amount, gname} );
            this.getCartGood();
        },

        //提交订单
        async submit_order(){
            const uid = this.userInfo.uid; //用户id
            const totalPrice = this.totalPrice; //商品总价

            //将用户id、购物车商品总价格加入至goodsorder表中并返回订单号
            const resOrderNumber = await this.$http.submitOrder( {uid, totalPrice} )
            this.orderNumber = resOrderNumber; 

            //打开对话框
            if(resOrderNumber) this.cofirmOrderVisible = true;
        },

        //进行伪付款并创建付款时间,伪付款成功把对应的用户ID的购物车商品删除
        async createPayTime(){
            //进行伪付款并创建付款时间
            const createJudge = await this.$http.createPaymentTime(this.orderNumber)
            //return createJudge > 0 ? "付款成功" : "付款失败";
            if(createJudge == "付款成功") {

                this.$message.success(createJudge)
                //付款成功，将对应商品名和用户ID添加到评论表中，并且删除对应的用户ID的购物车商品
                const userId = this.userInfo.uid;
                const userName = this.userInfo.uname;
                for(const i in this.cartInfo){
                    const goodsName = this.cartInfo[i].gname;
                    
                    // 查询用户ID对应的商品名是否存在评论表中，如果存在，则不进行下面的添加操作
                    const existJudge = await this.$http.judgeUidToGoodsExist( {goodsName, userId, userName} );
                    if(existJudge == "您拥有了" + goodsName + "评论权限，快去评论吧~"){
                        //将对应商品名和用户ID添加到评论表中，让用户获取到该商品的评论权限
                        const CommentPowerJudge = await this.$http.addGoodsCommentPower( {goodsName, userId, userName}  );
                        if(CommentPowerJudge == "添加用户评论商品权限成功"){
                            this.$message.success(existJudge);
                        }else{
                            this.$message.error("获取商品评论权限失败...")
                        }
                    }else{
                        this.$message.warning(existJudge)
                    }

                    //删除对应的用户ID的购物车商品
                    const deleteCartGoodsJudge = await this.$http.deleteCartGoods( {goodsName, userId} );
                    if(deleteCartGoodsJudge == "删除商品失败") this.$message.error(deleteCartGoodsJudge)
                    if(deleteCartGoodsJudge == "删除商品成功") this.getCartGood()
                }

            }else{
                this.$message.error(createJudge)
            }

            this.cofirmOrderVisible = false //关闭对话框
        },

        //取消订单
        async cancelOrder(){
            const orderNumber = this.orderNumber;
            const cancelJudge = await this.$http.cancelOrder({orderNumber})
            if(cancelJudge == "取消订单成功"){
                this.$message.success(cancelJudge)
            }else{
                this.$message.error(cancelJudge)
            }
            this.cofirmOrderVisible = false//关闭对话框
        },
        
        //点X关闭订单时，删除对应的用户ID的购物车商品
        async sumbitOrder_close(){
            const userId = this.userInfo.uid;
            for(const i in this.cartInfo){
                const goodsName = this.cartInfo[i].gname;
                //删除对应的用户ID的购物车商品
                const deleteCartGoodsJudge = await this.$http.deleteCartGoods( {goodsName, userId} );
                if(deleteCartGoodsJudge == "删除商品失败") this.$message.error(deleteCartGoodsJudge)
                if(deleteCartGoodsJudge == "删除商品成功") this.getCartGood()
            }
            this.cofirmOrderVisible = false//关闭对话框
        }
        
    },
}
</script>

<style scoped>

/* 用户信息确认区域 */
.userInfo_container{
    width:80%;
    margin:15px auto;
    border-bottom:1px dashed rgb(212, 212, 212);
}
.userInfo_uaddress{
    font-size:20px;
    font-weight: bold;
    text-align:center;
    margin-bottom:10px;
}
.userInfo_uname_umobile{
    font-size:17px;
    text-align:center;
    margin-bottom:10px;
}
.userInfo_uname{
    margin-right:20px;
}


/* 商品价格相关显示区域 */
.shoppingcart_price{
    margin-top:20px;
}

/* 总价格 */
.total_price{
    display:flex;
    align-items: center;
}

.total_price_left{
    font-size:20px;
    color:rgb(102, 102, 102);
    text-shadow:1px 1px 0px rgb(102, 102, 102);
}

.total_price_sign{
    font-size:20px;
    color:orangered;
    text-shadow:1px 1px 0px orangered;
    margin-right:1px;
}

.total_price_right{
    font-size:30px;
    color:orangered;
    text-shadow:1px 1px 0px orangered;
}

/* 总优惠 */
.total_discount{
    display:flex;
    align-items: center;
    margin-top:2px;
}

.total_discount_left{
    font-size:18px;
    color:rgb(102, 102, 102);
    text-shadow:1px 1px 0px rgb(102, 102, 102);
}

.total_discount_sign{
    font-size:15px;
    color:rgb(0, 197, 16);
    text-shadow:1px 1px 0px rgb(0, 197, 16);
    margin-right:1px;
}

.total_discount_right{
    font-size:23px;
    color:rgb(0, 197, 16);
    text-shadow:1px 1px 0px rgb(0, 197, 16);
}

/* 提交订单按钮 */
.submit_order_button{
    margin:10px 0px 0px 265px;
}
</style>