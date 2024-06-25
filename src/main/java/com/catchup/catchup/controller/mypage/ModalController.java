package com.catchup.catchup.controller.mypage;

import com.catchup.catchup.dto.UserDTO;
import com.catchup.catchup.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ModalController {

    private final MyPageService service;

    @PostMapping("/profileupdate")
    public ResponseEntity<String> updateProfile(@RequestBody UserDTO dto, Model model) {
        // 세션 처리 완료되면 변경하기
        Long id = 101L;

        log.info("nickname >>>>>>>>>>>> {} ", dto.getNickname());

        String profile = service.updateNickname(id, dto.getNickname());
        return ResponseEntity.ok(profile);
    }

}
