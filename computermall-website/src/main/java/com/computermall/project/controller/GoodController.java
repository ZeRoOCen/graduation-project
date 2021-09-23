package com.computermall.project.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.computermall.project.POJO.*;
import com.computermall.project.mapper.GoodMapper;
import com.computermall.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import service.GoodService;
import service.UserService;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class GoodController {

    @Autowired
    GoodService goodService;

    @Autowired
    UserService userService;

    @Autowired
    GoodMapper goodMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    @Qualifier("redisTemplate")  //由于我们自定义的redisTemplate与官方的重名了，所以一定要让配置的redisTemplate指向的是自己配置的
    private RedisTemplate redisTemplate;

    //获取商品信息，商品总量，分页查询
    @PostMapping("/getgoodlist")
    public Object getGoodList(@RequestBody QueryInfo queryInfo){
        //通过模糊查询商品名来获取商品总量
        Integer goodCounts = goodService.getGoodCounts("%" + queryInfo.getQuery() + "%");
//        int goodCounts = goodMapper.getGoodCounts("%" + queryInfo.getQuery() + "%");
        //页数从哪开始，即当前编号。因为第一页的索引值是0，所以需要进行减一操作（queryInfo.getPageNum() - 1）第二页就是：1*此页最大数量（此时索引值就达到第二页的开始）
        Integer pageStart = (queryInfo.getPageNum() - 1) * queryInfo.getPageSize();
        //模糊查询判断商品名，当前页，每页最大存放数
        List<Good> goods = goodService.getAllGood("%" + queryInfo.getQuery() + "%", pageStart, queryInfo.getPageSize());
//        List<Good> goods = goodMapper.getAllGood("%" + queryInfo.getQuery() + "%", pageStart, queryInfo.getPageSize());
        HashMap<String,Object> res = new HashMap<>();  //由于无法return返回多个参数（goodCounts + goods），所以需要使用HashMap封装一下
        res.put("goodCounts", goodCounts);
        res.put("goods", goods);
        redisTemplate.opsForValue().set("res_json",res);
        Object res_json = redisTemplate.opsForValue().get("res_json");
        return res_json;
    }

    //获取活动商品信息，商品总量，分页查询
    @PostMapping("/getgoodactivitylist")
    public Object getGoodActivityList(@RequestBody QueryInfo queryInfo){
        //通过模糊查询活动商品名来获取商品总量
        Integer goodsActivityCounts = goodService.getGoodAcvityCounts("%" + queryInfo.getQuery() + "%");
//        Integer goodsActivityCounts = goodMapper.getGoodAcvityCounts("%" + queryInfo.getQuery() + "%");
        //页数从哪开始，即当前编号。因为第一页的索引值是0，所以需要进行减一操作（queryInfo.getPageNum() - 1）第二页就是：1*此页最大数量（此时索引值就达到第二页的开始）
        int pageStart = (queryInfo.getPageNum() - 1) * queryInfo.getPageSize();
        //模糊查询判断活动商品名，当前页，每页最大存放数
        List<GoodsActivity> goodsActivities = goodService.getAllGoodsActivity("%" + queryInfo.getQuery() + "%", pageStart, queryInfo.getPageSize());
//        List<GoodsActivity> goodsActivities = goodMapper.getAllGoodsActivity("%" + queryInfo.getQuery() + "%", pageStart, queryInfo.getPageSize());
        HashMap<String,Object> res = new HashMap<>();  //由于无法return返回多个参数（goodCounts + goods），所以需要使用HashMap封装一下
        res.put("goodsActivityCounts", goodsActivityCounts);
        res.put("goodsActivities", goodsActivities);
        redisTemplate.opsForValue().set("res_json",res);
        Object res_json = redisTemplate.opsForValue().get("res_json");
        return res_json;
    }

    //添加商品
        @PostMapping("/addgood")
        public String addGood(@RequestBody Good good){
            good.setGstate(1); //添加的商品默认状态为1（0代表商品异常，1代表商品正常）
            int judge = goodService.addGood(good);
//        int judge = goodMapper.addGood(good);
            return judge > 0 ? "success" : "error";
    }

    //将全部商品信息加入到购物车数据库中
    @PostMapping("/addgoodinfotoCart")
    public String addCart(@RequestBody Good good){
        Integer judge = goodService.addGoodToCart(good);
//        int judge = goodMapper.addGoodToCart(good);
        return judge > 0 ? "success" : "error";

    }

    //将用户名和用户角色权限加入到购物车数据库中
    @PostMapping("/addnameandroletocart")
    public String addCart(@RequestBody User nameIdAndRole){
        String uname = nameIdAndRole.getUname();
        int uid = nameIdAndRole.getUid();
        int urole = nameIdAndRole.getUrole();

        Integer cartid = userService.getCartId();
//        Integer cartid = userMapper.getCartId();

        Integer judge = userService.addNameIdAndRoleToCart(uid,uname,urole,cartid);
//        Integer judge = userMapper.addNameIdAndRoleToCart(uid,uname,urole,cartid);

        return judge > 0 ? "success" : "error";
    }

    //修改商品状态
    @PostMapping("/goodstate")
    public String updateGoodState(@RequestBody GidAndGoodState gidAndGoodState){
        Integer judge = goodService.updateGoodState(gidAndGoodState.getGid(),gidAndGoodState.getGstate());
//        Integer judge = goodMapper.updateGoodState(gid,gstate);
        return judge > 0 ? "success":"error";
    }

    //根据商品名获取商品全部信息
    @PostMapping("/getgoodinfobyname")
    public Object getGoodInfoByName(@RequestBody String gname){
        JSONObject goodname = JSON.parseObject(gname);
        String gnameValue = goodname.getString("queryString");
        List<Good> good = goodService.getGoodInfoByName("%" + gnameValue + "%");
//        List<Good> good = goodMapper.getGoodInfoByName("%" + gnameValue + "%");
        redisTemplate.opsForValue().set("goodInfo",good);
        Object res_json = redisTemplate.opsForValue().get("goodInfo");
        return res_json;
    }

    //根据商品id获取商品全部信息
    @PostMapping("/getupdategood")
    public Object getUpdateGood(@RequestBody String gid){
        String gidStr = "";
        JSONObject gidObj = JSONObject.parseObject(gid); //转换成Objcet形式，可以使用get()方法取其中的值
        for(int i = 0; i < gidObj.size(); i++){
            gidStr += gidObj.get(i);
            System.out.println("===>"+gidStr);
        }
        Integer goodsId = Integer.parseInt(gidStr);
        //通过gid获取到此商品的所有信息
        Good good = goodService.getUpdateGood(goodsId);
//        Good good = goodMapper.getUpdateGood(goodsId);

        redisTemplate.opsForValue().set("goodInfo",good);
        Object goodInfo = redisTemplate.opsForValue().get("goodInfo");

        return goodInfo;
    }

    //编辑商品信息
    @PostMapping("/editgood")
    public String editGood(@RequestBody Good good){
        Integer judge = goodService.editGood(good);
//        Integer judge = goodMapper.editGood(good);
        return judge > 0 ? "success":"error";
    }

    //删除商品
    @DeleteMapping("/deletegood")
    public String deleteGood(Integer gid){
        Integer judge = goodService.deleteGood(gid);
//        Integer judge = goodMapper.deleteGood(gid);
        return judge > 0 ? "success":"error";
    }

    //如果是会员用户，则给商品进行打折
    @PostMapping("/getgoodsdiscount")
    public String getGoodsDiscount(@RequestBody String uidAndGname){
        String userId = "";
        String goodsName = "";
        JSONObject uidObj = JSONObject.parseObject(uidAndGname);
        for(int i = 0; i < uidObj.size(); i++){
            if(i == 0){
                userId = uidObj.getString("uid");
            }else{
                goodsName = (String) uidObj.get("gname");
            }
        }
        Integer uid = Integer.parseInt(userId);
        //会员用户进行商品打折
        Integer judge = goodMapper.updateGoodsDiscount(uid, goodsName);

        return judge > 0 ? "success" : "error";
    }

    //获取购物车商品汇总展示列表
    @PostMapping("/getgoodscartinfo")
    public Object getGoodsCartInfo(@RequestBody Uid uid){
        Integer userId = uid.getUid();
        List<CartSameGoodsNumbers> cartInfo = null; //购物车数据
        cartInfo = goodMapper.getCartGoodsSameCount(userId);
        redisTemplate.opsForValue().set("cartGoodsInfo",cartInfo);
        Object cartGoodsInfo = redisTemplate.opsForValue().get("cartGoodsInfo");
        return cartGoodsInfo;
    }

    //更新购物车列表至购物车商品汇总展示列表
    @PostMapping("/updatesamegoods")
    public String updateSameGoods(@RequestBody String gnameAndUid){
        String goodsName = "";
        String userId = "";
        JSONObject uidObj = JSONObject.parseObject(gnameAndUid);
        for(int i = 0; i < uidObj.size(); i++){
            if(i == 0){
                goodsName = uidObj.getString("gname");
            }else{
                userId = uidObj.getString("uid");
            }
        }

        Integer uid = Integer.parseInt(userId);


        Random r = new Random();
        String sameGoodsId = "";
        //当购物车相同商品分类表中没有uid和gname对应的唯一id，则创建一个
        String id = goodMapper.selectSameGoodsId(goodsName, uid); //查找购物车相同商品分类表中有没有uid和gname对应的唯一id
        if(id == null){
            //随机生成三位数字
            int r1 = (r.nextInt(900)+100); // 100 ~ 899
            int r2 = (r.nextInt(900)+100); // 同上
            String randomOne = Integer.toString(r1);
            String randomTwo = Integer.toString(r2);

            //随机生成三个大写字母
            String uppercaseLetter = "";
            for(int i=0; i<3; i++){
                char letter = (char)(Math.random()*26 + 65);
                uppercaseLetter += String.valueOf(letter);
            }

            //生成相同商品表唯一id
            sameGoodsId = randomOne + uppercaseLetter + randomTwo;

            //更新购物车列表至汇总展示列表（不包括amount重复商品数量）
            Integer judge = goodMapper.updateSameGoods(uid, goodsName);

            //更新商品表唯一id至的对应的汇总展示列表
            Integer updateIdJudeg = goodMapper.updateSameGoodsId(sameGoodsId, uid, goodsName);

            //更新汇总展示列表的amount重复商品数量
            Integer GoodsAmountJudge = goodMapper.updateGoodsAmount(uid, goodsName);

            return (judge>0 && updateIdJudeg>0 && GoodsAmountJudge>0) ? "success" : "error";
        }

        //如果id不为空，则直接更新汇总展示列表的amount重复商品数量
        Integer GoodsAmountJudge = goodMapper.updateGoodsAmount(uid, goodsName);

        return GoodsAmountJudge > 0 ? "success" : "error";
    }

    //根据商品名更新购物车商品数量
    @PostMapping("/amountchanged")
    public String amountChanged(@RequestBody String amountAndGname){
        String goodsAmount = "";
        String goodsName = "";
        JSONObject uidObj = JSONObject.parseObject(amountAndGname);
        for(int i = 0; i < uidObj.size(); i++){
            if(i == 0){
                goodsAmount = uidObj.getString("amount");
            }else{
                goodsName = uidObj.getString("gname");
            }
        }
        Integer gAmount = Integer.parseInt(goodsAmount);
        Integer changeJudge = goodMapper.amountChanged(gAmount, goodsName);

        return changeJudge > 0 ? "success" : "error";
    }

    //提交用户ID、购物车商品总价格、订单号至订单中
    @PostMapping("/submitorder")
    public Object submitOrder(@RequestBody String uidTotalPrice){
        String userId = "";
        String tPrice = "";
        JSONObject Object = JSONObject.parseObject(uidTotalPrice);
        for(int i = 0; i < Object.size(); i++){
            if(i == 0){
                userId = Object.getString("uid");
            }else{
                tPrice = Object.getString("totalPrice");
            }
        }
        Integer uid = Integer.parseInt(userId);
        Integer totalPrice = Integer.parseInt(tPrice);

/* !-----封装订单号-----! */
        //封装当前时间转换成String类型
            //parse方法：将字符串类型（java.lang.String）解析为日期类型（java.util.Date）
            //format方法：将日期类型（Date）数据格式化为字符串（String）
        String orderTimeStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        //随机生成三个大写字母
        String rdLetterFirst = "";
        for(int i=0; i<3; i++){
            char LetterFirst = (char)(Math.random()*26 + 65);
            rdLetterFirst += String.valueOf(LetterFirst);
        }

        //随机六位数(100000 ~ 999999)
        Random r = new Random();
        int random = (r.nextInt(899999)+100000);
        String randomSix = Integer.toString(random);

        //随机生成三个大写字母
        String rdLetterSecond = "";
        for(int i=0; i<3; i++){
            char LetterFirst = (char)(Math.random()*26 + 65);
            rdLetterSecond += String.valueOf(LetterFirst);
        }

        //获取四位用户id
            //将int类型的uid转换成StringBuffer类型
        StringBuffer stringBuffer = new StringBuffer(uid.toString());
            //如果uid长度大于4，则取后四位
        if(stringBuffer.length() > 4){
            stringBuffer = new StringBuffer(stringBuffer.substring(stringBuffer.length()-4, stringBuffer.length()));
        };
            //当uid长度小于4时，用0补全4位（insert，在指定位置添加指定字符串）
        while(stringBuffer.length() < 4){
            stringBuffer.insert(0,"0");
        };

        //生成订单号
        String orderNumber = orderTimeStr + rdLetterFirst + randomSix + rdLetterSecond + stringBuffer;

        //----将用户id、购物车商品总价格、订单号和订单时间推送至goodsorder表中并返回订单号-----
        Integer changeJudge = goodMapper.addOrder(uid, totalPrice, orderNumber, orderTimeStr);
        if(changeJudge == 0)return "订单提交失败";

        return orderNumber;
    }

    //创建付款时间
    @PostMapping("/createpaymenttime")
    public String createPaymentTime(@RequestBody String orderNumber){

        String orderNumberStr = "";
        JSONObject orderNumberObj = JSONObject.parseObject(orderNumber);
        for(int i = 0; i < orderNumberObj.size(); i++){
            String iStr = String.valueOf(i);
            orderNumberStr += orderNumberObj.getString(iStr);
        }

        //封装当前时间转换成String类型
            //parse方法：将字符串类型（java.lang.String）解析为日期类型（java.util.Date）
            //format方法：将日期类型（Date）数据格式化为字符串（String）
        String payTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        //根据订单号添加付款时间，并将支付状态修改为1
        Integer payState = 1;
        Integer createJudge = goodMapper.createPaymentTime(orderNumberStr, payTime, payState);
        if(createJudge == 0) return null;

        return createJudge > 0 ? "付款成功" : "付款失败";
    }

    //付款成功，根据对应的用户ID删除购物车中的商品
    @PostMapping("/deletecartgoods")
    public String deleteCartGoods(@RequestBody String gnameAndUid){
        String gname = "";
        String userId = "";
        JSONObject Object = JSONObject.parseObject(gnameAndUid);
        for(int i = 0; i < Object.size(); i++){
            if(i == 0){
                gname = Object.getString("goodsName");
            }else{
                userId = Object.getString("userId");
            }
        }
        Integer uid = Integer.parseInt(userId);

        Integer deleteCartSameGoods =  goodMapper.deleteCartSameGoods(gname, uid);
        Integer deleteCartGoodsJudge =  goodMapper.deleteCartGoods(gname, uid);

        if(deleteCartSameGoods == 0 ) return "删除商品失败";
        if(deleteCartGoodsJudge == 0 ) return "删除商品失败";

        return "删除商品成功";
    }

    //取消订单
    @PostMapping("/cancelorder")
    public String cancelOrder(@RequestBody String orderNumber){
        String ordernumber = "";
        JSONObject Object = JSONObject.parseObject(orderNumber);
        for(int i = 0; i < Object.size(); i++){
            if(i == 0){
                ordernumber = Object.getString("orderNumber");
            }
        }
        Integer cancelJudge = goodMapper.cancelOrder(ordernumber);
        return cancelJudge > 0 ? "取消订单成功" : "取消订单失败";
    }

    //取消订单（获取到的ordernumber是JSON字符串对象类型，需要进行转换）
    @PostMapping("/ordercancel")
    public String orderCancel(@RequestBody String ordernumber){
        String orderNumber = "";
        JSONObject Object = JSONObject.parseObject(ordernumber);
        for(int i = 0; i < Object.size(); i++){
            if(i == 0){
                orderNumber = Object.getString("ordernumber");
            }
        }
        Integer cancelJudge = goodMapper.cancelOrder(orderNumber);
        return cancelJudge > 0 ? "取消订单成功" : "取消订单失败";
    }

    //获取所有订单信息
    @PostMapping("/getorderinfo")
    public Object getOrderInfo(@RequestBody Uid uid){
        Integer userId = uid.getUid();
        List<GoodsOrder> goodsOrder = goodMapper.getOrderInfo(userId);
        redisTemplate.opsForValue().set("orderInfo", goodsOrder);
        Object orderInfo = redisTemplate.opsForValue().get("orderInfo");

        return orderInfo;
    }

    //根据订单号获取订单信息
    @PostMapping("/getorderrow")
    public Object getOrderRow(@RequestBody String ordernumber){
        String orderNumber = "";
        JSONObject Object = JSONObject.parseObject(ordernumber);
        for(int i = 0; i < Object.size(); i++){
            if(i == 0){
                orderNumber = Object.getString("ordernumber");
            }
        }
        GoodsOrder goodsOrder = goodMapper.getOrderRow(orderNumber);
        redisTemplate.opsForValue().set("objData", goodsOrder);
        Object orderRowData = redisTemplate.opsForValue().get("objData");
        return orderRowData;
    }

    //根据订单号添加付款时间和修改付款状态为1
    @PostMapping("/paymentorder")
    public String paymentOrder(@RequestBody String ordernumber){
        String orderNumberStr = "";
        JSONObject orderNumberObj = JSONObject.parseObject(ordernumber);
        for(int i = 0; i < orderNumberObj.size(); i++){
            orderNumberStr = orderNumberObj.getString("ordernumber");
        }

        //封装当前时间转换成String类型
        //parse方法：将字符串类型（java.lang.String）解析为日期类型（java.util.Date）
        //format方法：将日期类型（Date）数据格式化为字符串（String）
        String payTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        //根据订单号添加付款时间，并将支付状态修改为1
        Integer payState = 1;
        Integer createJudge = goodMapper.createPaymentTime(orderNumberStr, payTime, payState);

        return createJudge > 0 ? "success" : "error";
    }

//    根据商品ID将商品添加至活动商品表中
    @PostMapping("/addgoodstoactivity")
    public String addGoodsToActivity(@RequestBody String gid){
        String goodsId = "";
        JSONObject gidObj = JSONObject.parseObject(gid);
        for(int i = 0; i < gidObj.size(); i++){
            goodsId = gidObj.getString("gid");
        }
        Integer goodId = Integer.parseInt(goodsId);

        Integer addJudge = 0;
        try{
            addJudge = goodMapper.addGoodsToActivity(goodId);
        }catch(Exception e){
            return "false";
        }
        return addJudge > 0 ? "success" : "error";
    }

//    获取所有商品活动的信息并返回
    @PostMapping("/getgoodsactivity")
    public Object getGoodsActivity(){
        List<GoodsActivity> goodsActivity =  goodMapper.getGoodsActivity();

        redisTemplate.opsForValue().set("goodsActivity", goodsActivity);
        Object gActivity = redisTemplate.opsForValue().get("goodsActivity");

        return gActivity;
    }

    @PostMapping("/activitysubmit")
    public Object getActivitySingleGoods(@RequestBody String gname){
        String goodsName = "";
        JSONObject gidObj = JSONObject.parseObject(gname);
        for(int i = 0; i < gidObj.size(); i++){
            goodsName = gidObj.getString("gname");
        }
        Good goods = goodMapper.getActivitySingleGoods(goodsName);

        return goods;
    }

    //根据用户ID查看对应的商品名是否存在与评论表中
    @PostMapping("/judgeuidtogoodsexist")
    public String judgeUidToGoodsExist(@RequestBody String gnameAndUid){
        String goodsName = "";
        String userId = "";

        JSONObject object = JSONObject.parseObject(gnameAndUid);
        for(int i = 0; i < object.size(); i++){
            if( i == 0){
                goodsName = object.getString("goodsName");
            }else{
                userId = object.getString("userId");
            }
        }
        Integer uid = Integer.parseInt(userId);

        try {
            Integer existJudge = goodMapper.judgeUidToGoodsExist(goodsName, uid);
            if (existJudge == 1) return "您已经拥有了" + goodsName + "评论权限，若还没有评论，就快去评论一下吧~";
        }catch(Exception e){
            return "您拥有了" + goodsName + "评论权限，快去评论吧~";
        }

        return "您拥有了" + goodsName + "评论权限，快去评论吧~";
    }

    //添加商品名、用户ID和用户名将用户对该商品评论权限变为1（开启）
    @PostMapping("/addgoodscommentpower")
    public String addGoodsCommentData(@RequestBody String gnameUidAndUname){
        String goodsName = "";
        String userId = "";
        String userName = "";

        JSONObject object = JSONObject.parseObject(gnameUidAndUname);
        for(int i = 0; i < object.size(); i++){
            if(i == 0){
                goodsName = object.getString("goodsName");
            }else if(i == 1){
                userId = object.getString("userId");
            }else{
                userName = object.getString("userName");
            }
        }
        Integer uid = Integer.parseInt(userId);

        Integer addJudge = goodMapper.addGoodsCommentData(goodsName, uid, userName);

        if(addJudge == 1){
            return "添加用户评论商品权限成功";
        }

        return "添加用户评论商品权限失败";
    }

    @PostMapping("/getcommentlist")
    public Object CommentLlist(@RequestBody CommentQueryInfo commentQueryInfo){
        //当前商品评论总数
        Integer commentNumbers = goodMapper.getCommentCount("%" + commentQueryInfo.getCommentMessage() + "%",
                commentQueryInfo.getCurrentGoodsName());
        //页面从哪开始
        Integer commentPageStart = (commentQueryInfo.getCurrentPageNum()-1) * commentQueryInfo.getCommentPageSize();
        //获取每页的商品评论内容
        List<GoodsComment> goodsComment = goodMapper.getGoodsComment("%" + commentQueryInfo.getCommentMessage() + "%",
                commentQueryInfo.getCurrentGoodsName(), commentPageStart, commentQueryInfo.getCommentPageSize());
        HashMap<String, Object> resData = new HashMap<>();
        resData.put("goodsComment", goodsComment);
        resData.put("commentTotalNumbers", commentNumbers);

        redisTemplate.opsForValue().set("commentData", resData);
        Object commentData = redisTemplate.opsForValue().get("commentData");

        return commentData;
    }

    //获取当前用户对应的某个商品的评论信息
    @PostMapping("/getcomment")
    public Object getComment(@RequestBody GnameAndUid gnameAndUid){
        GoodsComment gComment = goodMapper.getComment(gnameAndUid.getGname(), gnameAndUid.getUid());
        redisTemplate.opsForValue().set("goodsComment", gComment);
        Object goodsComment = redisTemplate.opsForValue().get("goodsComment");

        return goodsComment;
    }

    //根据用户id，商品名提交商品评分和输入框评价内容
    @PostMapping("/addcomment")
    public String addComment(@RequestBody UidGnameCommentAndGrade uidGnameCommentAndGrade){
        Integer updateJudge = goodMapper.updateUserComment(uidGnameCommentAndGrade.getUid(), uidGnameCommentAndGrade.getGname(),
                                                           uidGnameCommentAndGrade.getComment(), uidGnameCommentAndGrade.getGrade());
        return updateJudge > 0 ? "success": "error";
    }

    //删除活动商品
    @PostMapping("/deletegoodsactivity")
    public String deleteGoodsActivity(@RequestBody Gname gname){
        String goodsName = gname.getGname();
        try{
            goodMapper.deleteGoodsActivity(goodsName);
        }catch(Exception e){
            System.out.println(e);
        }
        return "success";
    }
}
