package org.tju.wx.dao.impl;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.tju.wx.dao.LoginUserDao;
import org.tju.wx.pojo.LoginUser;

import java.util.List;

/**
 * Created by Legend on 2014/7/17.
 */
public class LoginUserDaoImpl implements LoginUserDao {
    SessionFactory sessionFactory;
    HibernateTemplate hibernateTemplate;

    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    private HibernateTemplate getHibernateTemplate(){
        if(null == hibernateTemplate)
            hibernateTemplate = new HibernateTemplate(sessionFactory);
        return hibernateTemplate;
    }

    @Override
    public LoginUser findById(int id) {
        return (LoginUser)getHibernateTemplate().get(LoginUser.class, new Integer(id));
    }

    @Override
    public List<LoginUser> findByName(String username) {
        String query = "from LoginUser u where u.username = ?";
        return (List<LoginUser>)(getHibernateTemplate().find(query, username));
    }
}
