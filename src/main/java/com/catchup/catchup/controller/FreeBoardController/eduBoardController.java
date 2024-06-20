package com.catchup.catchup.controller.FreeBoardController;

import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.service.FreeBoardService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class eduBoardController {

    private final FreeBoardService freeService;

    public eduBoardController(FreeBoardService freeService) {
        this.freeService = freeService;
    }


    @GetMapping
    public String eduList(){
        return "";
    }

    @GetMapping
    public String comList(){

        return null;
    }

    @GetMapping
    public String eduDetail(Long fid){

        return null;
    }

    @GetMapping
    public String eduInsert() {

        return null;
    }

    @PostMapping
    public String eduInsertResult(FreeBoardDTO dto){

        return null;
    }

    @GetMapping
    public String eduUpdate(Long fid){

        return null;
    }

    @PostMapping
    public String eduUpdateResult(FreeBoardDTO dto){

        return null;
    }

    @PostMapping
    public String eduDelete(Long fid){

        return null;
    }
}
