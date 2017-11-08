package com.symsimmy.dao;

import com.symsimmy.model.User;

public interface UserDao extends BaseDao<User>{

    //根据用户的id查询用户信息
    public User getUserById(Integer id);
}
