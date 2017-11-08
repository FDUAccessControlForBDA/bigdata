package com.symsimmy.dao.Impl;

import com.symsimmy.dao.UserDao;
import com.symsimmy.model.User;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("UserDao")
@SuppressWarnings("all")
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

    // 根据用户的id查询用户信息
    public User getUserById(Integer id) {
        String hql = "from User u where u.id=?";
        Query query = this.getCurrentSession().createQuery(hql);
        query.setParameter(0, id);

        return (User) query.uniqueResult();
    }
}
