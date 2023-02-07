package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

  @GetMapping("/hallo")
  public String helloWorld() {
    return "hello world";
  }

  @GetMapping("/index")
  public String helloWorld2() {
    return "hello world2 test";
  }
}
