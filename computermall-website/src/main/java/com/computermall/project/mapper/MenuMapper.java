package com.computermall.project.mapper;

import com.computermall.project.POJO.MainMenu;
import com.computermall.project.POJO.SubMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuMapper {
    public List<MainMenu> getMenus(); //主菜单与子菜单的一对多查询
//  public List<SubMenu> getSubMenus(); //子菜单与三级菜单的一对多查询
}
