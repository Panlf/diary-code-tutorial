package com.plf.diary.security.rest;

import org.springframework.web.bind.annotation.*;

/**
 * @author panlf
 * @date 2022/4/6
 */
@RestController
@RequestMapping("api")
public class UserResource {

    @GetMapping("greeting")
    public String greeting(){
        return "Hello , Spring Security!";
    }

    @PostMapping("greeting")
    public String greeting(@RequestParam String name){
        return "Hello , Spring Security! -- from "+name;
    }
}
