package com.catchup.catchup.controller;

import com.catchup.catchup.dto.UserDTO;
import com.catchup.catchup.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
//@RequiredArgsConstructor
public class UserController {

    private UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }

    @GetMapping("/join")
    public String join(Model model){
        model.addAttribute("dto", new UserDTO());
        model.addAttribute("view", "user/join");
        return "index";
    }

    @PostMapping("/joinresult")
    public String joinResult(@Valid @ModelAttribute("dto") UserDTO dto, BindingResult bindingResult, Model model){

        if (bindingResult.hasErrors()){
            model.addAttribute("view", "user/join");
            return "index";
        } else {
            //sibal...
            Long id = userService.save(dto);
            model.addAttribute("view", "user/login");
            return "index";
        }
    }

    //회원가입 아이디 중복체크
    @PostMapping("/IdCheck")
    public @ResponseBody Long IdCheck(@RequestBody HashMap<String,Object> hm) {
        String id = (String) hm.get("id");
        System.out.println("hello......"+id);
        Long cnt = userService.idCheck(id);
        return cnt;
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("view", "user/login");

        return "index";
    }

    @GetMapping("/loginalert") //로그인 실패시
    public String loginalert(Model model){
        model.addAttribute("view", "user/loginalert.html");
        return "index";
    }

    @PostMapping("/loginresult")
    public String loginResult(@RequestParam String id, @RequestParam String password, HttpSession session){

        if (id!=null && password!=null){
            UserDTO dto = userService.loginCheck(id);

            boolean result = false;

            if(password.equals(dto.getPassword())){
                result = true;
            }

            if(result==true){
                Long uid = dto.getUid();
                session.setAttribute("sessionId", uid);
                return "redirect:index";
            }else {
                return "redirect:loginalert";
            }
        }else {
            return "redirect:loginalert";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session!=null && session.getAttribute("sessionId")!=null)
            session.invalidate();

        return "redirect:index";
    }

}
