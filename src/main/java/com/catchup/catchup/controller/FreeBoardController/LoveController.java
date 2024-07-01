package com.catchup.catchup.controller.FreeBoardController;

import com.catchup.catchup.dto.LoveDTO;
import com.catchup.catchup.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoveController {

    private final FreeBoardService service;

    @PostMapping("/love")
    public ResponseEntity<Boolean> insert(@RequestBody LoveDTO dto) throws Exception{

        /** 좋아요 여부 확인 **/
        boolean isChecked = service.loveCheck(dto);
        log.info("boolean.....{}", isChecked);
        return ResponseEntity.ok().body(isChecked);
//        if(isChecked) {
//            service.delLove(dto);
//            return ResponseEntity.ok().body(isChecked);
//        }else{
//            service.addLove(dto);
//            return ResponseEntity.ok().body(isChecked);
//        }
    }

    @PostMapping("/loveupdate")
    public ResponseEntity<?> likeAction(@RequestBody LoveDTO dto) throws Exception{

        /** 좋아요 여부 확인 **/
        boolean isChecked = service.loveCheck(dto);
        log.info("update boolean.....{}", isChecked);

        if(isChecked) {
            service.delLove(dto);
        }else{
            service.addLove(dto);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
