package com.catchup.catchup.controller;

import com.catchup.catchup.dto.UserDTO;
import com.catchup.catchup.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/join")
    public String join(Model model){
        model.addAttribute("dto", new UserDTO());
        model.addAttribute("view", "user/join");
        return "index";
    }

    @PostMapping("/join")
    public String joinResult(@Valid @ModelAttribute("dto") UserDTO dto, BindingResult bindingResult, Model model){

        if (bindingResult.hasErrors()){
            return "/join";
        } else {
            //sibal...
            Long id = userService.save(dto);
            model.addAttribute("id", id);
            return "/login";
        }

    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("view", "user/login");

        return "index";
    }

/*    @PostMapping("/loginresult")
    public String loginResult(@RequestParam String id, @RequestParam String password, HttpSession session){
        if (id!=null && password!=null){
            boolean login = userService.loginCkeck(id, password);
        }else {

        }
    }*/


}
