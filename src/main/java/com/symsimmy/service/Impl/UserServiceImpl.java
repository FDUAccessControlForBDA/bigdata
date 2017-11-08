package com.symsimmy.service.Impl;

import com.symsimmy.dao.UserDao;
import com.symsimmy.model.User;
import com.symsimmy.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service("UserService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    // 根据用户的id查询用户信息
    public User getUserById(Integer id){
        return userDao.getUserById(id);
    }
}
