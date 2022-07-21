package com.mvpt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/transfers")
public class TransferController {

    @GetMapping
    public ModelAndView showListPage(){
        ModelAndView modelAndView = new ModelAndView("/transfer/list");
        return modelAndView;
    }
}
