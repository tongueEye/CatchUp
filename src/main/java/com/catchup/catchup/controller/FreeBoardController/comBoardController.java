package com.catchup.catchup.controller.FreeBoardController;

import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class comBoardController {

    private final  FreeBoardService freeService;

    public comBoardController(FreeBoardService freeService) {
        this.freeService = freeService;
    }

    @GetMapping
    public String comList(){

        return null;
    }

    @GetMapping
    public String comDetail(Long fid){

        return null;
    }

    @GetMapping
    public String comInsert() {

        return null;
    }

    @PostMapping
    public String comInsertResult(FreeBoardDTO dto){

        return null;
    }

    @GetMapping
    public String comUpdate(Long fid){

        return null;
    }

    @PostMapping
    public String comUpdateResult(FreeBoardDTO dto){

        return null;
    }

    @PostMapping
    public String comDelete(Long fid){
        return null;
    }
}
