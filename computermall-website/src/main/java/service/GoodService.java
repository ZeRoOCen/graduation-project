package service;

import com.computermall.project.POJO.Good;
import com.computermall.project.POJO.GoodsActivity;

import java.util.List;

public interface GoodService {

    //根据商品名获取到对应的商品客服名
    public String selectCustomerName(String gname);

    //通过模糊查询商品名来获取商品总量
    public Integer getGoodCounts(String query);

    //模糊查询判断商品名，当前页，每页最大存放数
    public List<Good> getAllGood(String query, Integer pageStart, Integer pageSize);

    //通过模糊查询活动商品名来获取商品总量
    public Integer getGoodAcvityCounts(String query);

    //模糊查询判断活动商品名，当前页，每页最大存放数
    public List<GoodsActivity>  getAllGoodsActivity(String query, Integer pageStart, Integer pageSize);

    //添加商品
    public Integer addGood(Good good);

    //将全部商品信息加入到购物车数据库中
    public Integer addGoodToCart(Good good);

    //修改商品状态
    public Integer updateGoodState(Integer gid, Integer gstate);

    //根据商品名获取商品全部信息
    public List<Good> getGoodInfoByName(String gname);

    //通过gid获取到此商品的所有信息
    public Good getUpdateGood(Integer gid);

    //编辑用户信息
    public Integer editGood(Good good);

    //删除用户
    public Integer deleteGood(Integer gid);
}
