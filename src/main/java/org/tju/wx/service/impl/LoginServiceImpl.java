package org.tju.wx.service.impl;

import org.apache.log4j.Logger;
import org.tju.wx.dao.LoginUserDao;
import org.tju.wx.dao.impl.LoginUserDaoImpl;
import org.tju.wx.pojo.LoginUser;
import org.tju.wx.service.LoginService;

import java.util.List;

/**
 * Created by Legend on 2014/7/17.
 */
public class LoginServiceImpl implements LoginService {
    LoginUserDao loginUserDao;

    public void setLoginUserDao(LoginUserDao loginUserDao){
        this.loginUserDao = loginUserDao;
    }

    @Override
    public boolean login(LoginUser user) {
        List<LoginUser> list = loginUserDao.findByName(user.getUsername());
        if(list.size() == 0 || !list.get(0).getPassword().equals(user.getPassword()))
            return false;
        return true;
    }
}
