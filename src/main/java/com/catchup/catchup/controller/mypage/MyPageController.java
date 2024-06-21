package com.catchup.catchup.controller.mypage;

import com.catchup.catchup.dto.MyPageDTO;
import com.catchup.catchup.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService service;
    /* user 정보 끌고 와야 함 */

    @GetMapping("/mypage")
    public String mypage(Model model) {

        /* 프로필 사진 뽑아내기 */
        // MyPageDTO getid = service.getid(); // 세션 받으면 그걸로
        MyPageDTO result = service.getProfile();
        model.addAttribute("result", result);

        model.addAttribute("view", "mypage/home");
        return "index";
    }
}
