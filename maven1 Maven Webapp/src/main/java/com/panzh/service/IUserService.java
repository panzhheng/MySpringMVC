package com.panzh.service;

import java.util.List;

import com.panzh.dto.User;

public interface IUserService {  
      
    public User getUserById(int userId);  
  
    public void insertUser(User user);  
  
    public void addUser(User user);  
  
    public List<User> getAllUser();  
    
    public User getUserInfo(int uid);  
}  