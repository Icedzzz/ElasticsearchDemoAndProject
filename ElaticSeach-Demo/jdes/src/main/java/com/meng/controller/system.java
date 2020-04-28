package com.meng.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zwt
 * @version 1.0
 * @date 2020/4/25 22:52
 */
@Controller
public class system {
    @RequestMapping({"/","/index"})
    public String toIndexPage(){
        return "index";
    }
}
