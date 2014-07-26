package org.tju.wx.dao;

import org.tju.wx.pojo.LoginUser;

import java.util.List;

/**
 * Created by Legend on 2014/7/17.
 */
public interface LoginUserDao {
    LoginUser findById(int id);
    List<LoginUser> findByName(String username);
}
