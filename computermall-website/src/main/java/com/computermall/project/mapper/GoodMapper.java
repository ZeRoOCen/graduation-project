package com.computermall.project.mapper;

import com.computermall.project.POJO.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.List;

@Repository
public interface GoodMapper {
        //获取商品信息，根据商品信息判断limit索引
        //limit i,n 常用于分页查询
        //pagestart(i)：查询结果的索引值(默认从0开始)，当i=0时可省略i
        //pagesize(n)：查询结果返回的数量
        //例子：# 查询10条数据，索引从0到9，第1条记录到第10条记录：  select * from t_user limit 10;   &&   select * from t_user limit 0,10;
        //     # 查询8条数据，索引从5到12，第6条记录到第13条记录：  select * from t_user limit 5,8;
    public List<Good> getAllGood(@Param("goodName")String gname, @Param("pageStart")Integer pageStart, @Param("pageSize")Integer pageSize);

    public List<GoodsActivity> getAllGoodsActivity(@Param("goodsName")String gname, @Param("pageStart")Integer pageStart, @Param("pageSize")Integer pageSize);

        //根据商品名称来获取到商品的总数量
    public int getGoodCounts(@Param("goodName")String gname);

        //根据活动商品名来获取到商品的总数量
    public Integer getGoodAcvityCounts(@Param("goodsName")String gname);

        //添加商品到good商品表中
    public int addGood(Good good);

        //添加商品到cart表中
    public int addGoodToCart(Good good);

        //修改商品状态
    public int updateGoodState(@Param("goodId")Integer gid, @Param("goodState")Integer gstate);

        //删除商品
    public int deleteGood(@Param("goodId")Integer gid);

        //根据商品名获取商品全部信息
    public List<Good> getGoodInfoByName(@Param("goodName")String gname);

        //通过商品id获取商品全部信息，（用来给编辑商品弹框增加初始值）
    public Good getUpdateGood(@Param("goodId")Integer gid);
        //编辑商品信息
    public int editGood(Good good);

        //计算会员用户商品价格优惠的金额
    public int updateGoodsDiscount(@Param("userId") Integer uid, @Param("goodsName") String gname);

        //查询购物车相同商品数据
    public List<CartSameGoodsNumbers> getCartGoodsSameCount(@Param("userId") Integer uid);

        //查找购物车相同商品分类表中有没有uid和gname对应的唯一id
    public String selectSameGoodsId(@Param("goodsName") String gname, @Param("userId") Integer uid);

        //更新购物车列表至汇总展示列表
    public Integer updateSameGoods(@Param("userId") Integer uid, @Param("goodsName")String gname);

        //更新商品表唯一id至对应的汇总展示列表
    public Integer updateSameGoodsId(@Param("sameGoodsId") String sameGoodsId, @Param("userId") Integer uid, @Param("goodsName") String gname);

        //购物车商品数量+1操作
    public Integer updateGoodsAmount(@Param("userId") Integer uid, @Param("goodsName") String gname);

        //根据商品名更新购物车商品数量
    public Integer amountChanged(@Param("goodsAmount") Integer amount, @Param("goodsName") String gname);

        //将用户id、购物车商品总价格、订单号和订单时间推送至goodsorder表中
    public Integer addOrder(@Param("userId") Integer uid, @Param("totalPrice") Integer totalPrice,
                            @Param("orderNumber") String ordernumber, @Param("orderTime") String ordertime);

        //根据订单号创建付款时间
    public Integer createPaymentTime(@Param("orderNumber") String ordernumber, @Param("payTime") String paytime, @Param("payState") Integer paystate);

        //根据用户ID删除购物车中的商品
    public Integer deleteCartGoods(@Param("goodsName") String gname, @Param("userId") Integer uid);

        //根据用户ID删除购物车中的商品
    public Integer deleteCartSameGoods(@Param("goodsName") String gname, @Param("userId") Integer uid);

        //根据订单号取消订单
    public Integer cancelOrder(@Param("orderNumber") String ordernumber);

        //获取所有订单信息
    public List<GoodsOrder> getOrderInfo(@Param("userId") Integer userId);

        //根据订单号获取订单信息
    public GoodsOrder getOrderRow(@Param("orderNumber") String ordernumber);
//    测试用的添加商品图片
//    public String addGoodImgUrl(String gimgurl);

//    根据商品ID将商品添加至活动商品表中
    public Integer addGoodsToActivity(@Param("goodsId") Integer gid);

//    获取所有商品活动的信息
    public List<GoodsActivity> getGoodsActivity();

    //根据商品名获取good商品表中此商品所有信息
    public Good getActivitySingleGoods(@Param("goodsName") String gname);

    //根据用户ID查看对应的商品名是否存在与评论表中，如果存在，返回评论权限（此时的评论权限一般为1）
    public Integer judgeUidToGoodsExist(@Param("goodsName") String gname, @Param("userId") Integer uid);

    //添加商品名、用户ID和用户名将用户对该商品评论权限变为1（开启）
    public Integer addGoodsCommentData(@Param("goodsName") String gname, @Param("userId") Integer uid, @Param("userName") String uname);

    //获取商品评论信息，根据商品名和商品评论信息判断limit索引
    public Integer getCommentCount(@Param("commentMessage") String commentMessage, @Param("currentGoodsName") String currentGoodsName);

    //根据商品评论信息，商品名，每页开头的数量，每页展示数量来获取每页展示的商品评论信息内容
    public List<GoodsComment> getGoodsComment(@Param("commentMessage") String commentMessage, @Param("currentGoodsName") String currentGoodsName,
                                              @Param("commentPageStart") Integer commentPageStart, @Param("commentPageSize") Integer commentPageSize);

    //根据商品名和用户id查找相应的商品评论
    public GoodsComment getComment(@Param("goodsName") String gname, @Param("userId") Integer uid);

    //根据用户id，商品名提交商品评分和输入框评价内容
    public Integer updateUserComment(@Param("userId") Integer uid, @Param("goodsName") String gname, @Param("goodsComment") String comment, @Param("goodsGrade") Integer grade);

    //根据商品名获取到对应的商品客服名
    public String selectCustomerName(@Param("goodsName") String gname);

    //删除活动商品
    public Integer deleteGoodsActivity(@Param("goodsName") String gname);
}
