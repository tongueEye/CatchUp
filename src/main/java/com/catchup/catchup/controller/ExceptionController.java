package com.catchup.catchup.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//에러페이지 넘기기
@ControllerAdvice
public class ExceptionController {

    //개발 마지막에 추가하기

//    @ExceptionHandler(Exception.class)
//    public String except(Exception ex, Model model){
//        model.addAttribute("error_msg", ex);
//        model.addAttribute("view", "error/except404");
//        return "index";
//    }
}
