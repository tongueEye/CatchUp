package com.catchup.catchup.controller.mypage;

import com.catchup.catchup.domain.User;
import com.catchup.catchup.dto.*;
import com.catchup.catchup.service.MyPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Slf4j
public class MyPageController {

    private final MyPageService service;
    /* user 정보 끌고 와야 함 */

    @GetMapping("/home")
    public String mypage(Model model) {

        /* 프로필 사진 뽑아내기 */
        // MyPageDTO getid = service.getid(); // 세션 받으면 그걸로
        MyPageDTO result = service.getProfile();
        model.addAttribute("result", result);

        /* 내가 문의한 내역 뽑아오기 */
        List<InfoBoardDTO> qna = service.getQnaList();
        model.addAttribute("qna", qna);

        model.addAttribute("view", "mypage/home");
        return "index";
    }

    /** 정보 수정 */
    @GetMapping("/info")
    public String changeInfo(Model model) {
        /* 프로필 사진 보여주는 곳 */
        MyPageDTO result = service.getProfile();
        model.addAttribute("result", result);

        /* 기존 정보 불러오기 */
        UserDTO dto = service.getInfo();
        model.addAttribute("dto", dto);
        model.addAttribute("view", "mypage/info");
        return "index";
    }

    @PostMapping("/info")
    public String changeInfoResult(@Valid @ModelAttribute("dto") UserDTO dto
            , BindingResult bindingResult,  Model model) {

        MyPageDTO result = service.getProfile();
        model.addAttribute("result", result);

        if(bindingResult.hasErrors()) {
            /* 실패했을 때 */
            model.addAttribute("dto", dto);
            model.addAttribute("view", "mypage/info");
            return "index";
        } else {
            /* 성공했을 떄 */
            long id = service.modifyInfo(dto);
            model.addAttribute("dto", dto);
            model.addAttribute("view", "mypage/home");
            return "index";
        }
    }


    /** 내가 쓴 글 보기*/
    @GetMapping("wlist")
    public String writeList(
            @PageableDefault(size = 10, page = 0, sort = "boardId", direction = Sort.Direction.ASC) Pageable pageable
            , Model model) {
        /* 프로필 사진 보여주는 곳 */
        MyPageDTO result = service.getProfile();
        model.addAttribute("result", result);

        /** 내가 쓴 글 보기 */
        Page<FreeBoardDTO> boardlist = service.boardlist(pageable);

        // 페이징
        int pageSize= 5;
        int startPage = ((int) Math.ceil(pageable.getPageNumber() / pageSize)) * pageSize + 1;
        int endPage = Math.min(startPage + pageSize - 1, boardlist.getTotalPages()); // 넘쳤는데 나오면 안 되니까

        model.addAttribute("boardlist", boardlist);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("view", "mypage/writelist");
        return "index";
    }


    /** 내가 쓴 댓글 보기 */
    @GetMapping("rlist")
    public String repBoardList(
            @PageableDefault(size = 10, page = 0, sort = "boardId", direction = Sort.Direction.ASC) Pageable pageable
            , Model model) {

        /* 프로필 사진 보여주는 곳 */
        MyPageDTO result = service.getProfile();
        model.addAttribute("result", result);

        /** 댓글 DTO 생기면 수정하기 */
        /* 댓글 리스트 불러오기 */
        // Page<RepBoardDTO> list = service.getRepList(pageable);
        // model.addAttribute("list", list);

        model.addAttribute("view", "mypage/repboardlist");
        return "index";
    }


    /** 모달창에서 프로필 수정하기 */
    /*@GetMapping("/modal")
    public String modal(Model model) {

        model.addAttribute("view", "mypage/home");
        return "index";
    }

    @PostMapping("/modal")
    public String modalResult(@Valid @ModelAttribute("dto") UserDTO dto, BindingResult bindingResult
                              ,Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("view", "mypage/home");
            return "index";
        } else {
            // 닉네임 업데이트 코드
            long id = service.modifyProfile(dto);
            model.addAttribute("view", "mypage/home");
            return "index";
        }
    }*/




}
