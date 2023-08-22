package com.mapping.mapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class testController {

    @GetMapping("test")
    public String Hello(){
        return "test";
    }
}