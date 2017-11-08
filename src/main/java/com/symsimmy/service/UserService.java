package com.symsimmy.service;

import com.symsimmy.model.User;

public interface UserService {

    //根据用户的id查询用户信息
    public User getUserById(Integer id);
}
