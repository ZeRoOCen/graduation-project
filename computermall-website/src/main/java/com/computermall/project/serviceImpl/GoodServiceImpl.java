package com.computermall.project.serviceImpl;

import com.computermall.project.POJO.Good;
import com.computermall.project.POJO.GoodsActivity;
import com.computermall.project.controller.UserController;
import com.computermall.project.mapper.GoodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.GoodService;

import java.util.List;

@Service
public class GoodServiceImpl implements GoodService {
    @Autowired
    private GoodMapper goodMapper;


    @Override
    public String selectCustomerName(String gname) {
        return goodMapper.selectCustomerName(gname);
    }

    @Override
    public Integer getGoodCounts(String query) {
        return goodMapper.getGoodCounts(query);
    }

    @Override
    public List<Good> getAllGood(String query, Integer pageStart, Integer pageSize) {
        return goodMapper.getAllGood(query, pageStart, pageSize);
    }

    @Override
    public Integer getGoodAcvityCounts(String query) {
        return goodMapper.getGoodAcvityCounts(query);
    }

    @Override
    public List<GoodsActivity> getAllGoodsActivity(String query, Integer pageStart, Integer pageSize) {
        return goodMapper.getAllGoodsActivity(query, pageStart, pageSize);
    }

    @Override
    public Integer addGood(Good good) {
        return goodMapper.addGood(good);
    }

    @Override
    public Integer addGoodToCart(Good good) {
        return goodMapper.addGoodToCart(good);
    }

    @Override
    public Integer updateGoodState(Integer gid, Integer gstate) {
        return goodMapper.updateGoodState(gid, gstate);
    }

    @Override
    public List<Good> getGoodInfoByName(String gname) {
        return goodMapper.getGoodInfoByName(gname);
    }

    @Override
    public Good getUpdateGood(Integer gid) {
        return goodMapper.getUpdateGood(gid);
    }

    @Override
    public Integer editGood(Good good) {
        return goodMapper.editGood(good);
    }

    @Override
    public Integer deleteGood(Integer gid) {
        return goodMapper.deleteGood(gid);
    }

}
