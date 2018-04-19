package com.panzh.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.panzh.dto.User;
import com.panzh.service.IUserService;
/**
 * Redis数据持久化Controller
 * @author panzh
 *
 */
@Controller  
@RequestMapping("/redis") 
public class RedisController {
	@Resource  
    private IUserService userService;  
      
    @RequestMapping(value="/userInfo",method=RequestMethod.GET)  
    public String getUserInfo(HttpServletRequest request,Model model, @RequestParam int uid){  
        User userInfo = userService.getUserInfo(uid);  
        if(userInfo!=null){  
        	model.addAttribute("user", userInfo);  
            return "showUser";  
        }  
        return "error";  
    }  
}
