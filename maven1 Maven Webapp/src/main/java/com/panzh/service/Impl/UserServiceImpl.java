package com.panzh.service.Impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.panzh.dao.IUserDao;
import com.panzh.dto.User;
import com.panzh.service.IUserService;
import com.panzh.web.redis.RedisService;
import com.panzh.web.util.JsonUtils;

@Service("userService")  
@Transactional  
public class UserServiceImpl implements IUserService {  
    @Resource  
    private IUserDao userDao;  
    
    @Resource
    private RedisService redisService;
    
    public static final String USER_INFO = "uid";
      
    public User getUserById(int userId) {  
        return userDao.queryByPrimaryKey(userId);  
    }  
  
    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class, readOnly = false) 
    public void insertUser(User user) {  
        userDao.insertUser(user);  
        
        throw new RuntimeException("hehe"); 
    }  
  
    public void addUser(User user) {  
        userDao.insertUser(user);  
    }  
  
    public List<User> getAllUser() {  
        return userDao.getAllUser();  
    }  
    
    public User getUserInfo(int uid) {  
        String json = redisService.get(USER_INFO+uid);  
        if(json==null){  
            User user = getUserById(uid);  
            if(user!=null){  
                redisService.put(USER_INFO+uid, user, 1, TimeUnit.HOURS);  
            }  
            return user;  
        }  
  
        return JsonUtils.fromJson(json, User.class);  
    }  
  
}  
