package com.catchup.catchup.controller.mypage;

import com.catchup.catchup.dto.UserDTO;
import com.catchup.catchup.service.AWSService;
import com.catchup.catchup.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ModalController {

    private final MyPageService service;
    private final AWSService S3Service;

    @PostMapping("/profileupdate")
    public ResponseEntity<String> updateProfile(@RequestBody UserDTO dto, Model model) {
        // 세션 처리 완료되면 변경하기
        Long id = 101L;

        log.info("nickname >>>>>>>>>>>>>>> {}", dto.getNickname());
        log.info("profile >>>>>>>>>>>> {} ", dto.getProfile());

        String profile = service.updateNickname(id, dto.getNickname(), dto.getProfile());
        return ResponseEntity.ok(profile);
    }
}
