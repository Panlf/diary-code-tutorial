package com.plf.diary.drools.controller;

import com.plf.diary.drools.service.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @Autowired
    private HelloWorldService helloWorldService;

    @GetMapping("rule")
    public String rule(){
        helloWorldService.rule();
        return "Success";
    }
}
