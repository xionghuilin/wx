package com.xiong.wx.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author xionghl
 * @Date 2017/9/1514:24
 */
@Controller
@RequestMapping(value = "/index")
public class IndexController {

    @RequestMapping(value = {""})
    public String index() {
        return "index";
    }
}
