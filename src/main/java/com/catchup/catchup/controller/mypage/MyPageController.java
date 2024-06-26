package com.catchup.catchup.controller.mypage;

import com.catchup.catchup.domain.User;
import com.catchup.catchup.dto.*;
import com.catchup.catchup.service.MyPageService;
import com.catchup.catchup.service.UserService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Slf4j
public class MyPageController {

    private final MyPageService service;
    private final UserService userService;
    /* user 정보 끌고 와야 함 */

    @GetMapping("/home")
    public String mypage(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        if(session!=null && session.getAttribute("sessionId")!=null) {
            Long uid = (Long) session.getAttribute("sessionId");

            System.out.println(uid + "  >>>>>>>>>>>>>>>>>>>>>>> id ");

            if (uid != null) {
                /* 프로필 사진 뽑아내기 */
                MyPageDTO result = service.getProfile(uid);
                model.addAttribute("result", result);

                /* 내가 문의한 내역 뽑아오기 */
                List<InfoBoardDTO> qna = service.getQnaList(uid);
                model.addAttribute("qna", qna);
                model.addAttribute("uid", uid);

                /*급식 정보 뽑아오기*/
                UserDTO bap = service.getBap(uid);
                model.addAttribute("bap", bap);

                /* 좋아요 정보 불러오기 */
                List<LoveDTO> love = service.getLove(uid);
                model.addAttribute("love", love);

                model.addAttribute("view", "mypage/home");
            } else {
                model.addAttribute("view", "user/login");
            }
        }
        return "index";
    }

    /** 정보 수정 */
    @GetMapping("/info")
    public String changeInfo(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        Long uid = (Long) session.getAttribute("sessionId");

        /* 프로필 사진 보여주는 곳 */
        MyPageDTO result = service.getProfile(uid);
        model.addAttribute("result", result);

        /* 기존 정보 불러오기 */
        UserDTO dto = service.getInfo(uid);
        model.addAttribute("dto", dto);
        model.addAttribute("view", "mypage/info");
        return "index";
    }

    @PostMapping("/info")
    public String changeInfoResult(HttpServletRequest request
            , @Valid @ModelAttribute("dto") UserDTO dto
            , BindingResult bindingResult,  Model model) {

        HttpSession session = request.getSession(false);
        Long uid = (Long) session.getAttribute("sessionId");

        MyPageDTO result = service.getProfile(uid);
        model.addAttribute("result", result);

        if(bindingResult.hasErrors()) {
            /* 실패했을 때 */
            model.addAttribute("dto", dto);
            model.addAttribute("view", "mypage/info");
            return "index";
        } else {
            /* 성공했을 떄 */
            long id = service.modifyInfo(dto, uid);
            model.addAttribute("dto", dto);
            model.addAttribute("view", "mypage/home");
            return "index";
        }
    }


    /** 내가 쓴 글 보기*/
    @GetMapping("/wlist")
    public String writeList(HttpServletRequest request
            , @PageableDefault(size = 10, page = 0, sort = "boardId", direction = Sort.Direction.ASC) Pageable pageable
            , Model model) {
        /* 프로필 사진 보여주는 곳 */

        HttpSession session = request.getSession(false);
        Long uid = (Long) session.getAttribute("sessionId");

        MyPageDTO result = service.getProfile(uid);
        model.addAttribute("result", result);

        /** 내가 쓴 글 보기 */
        Page<FreeBoardDTO> boardlist = service.boardlist(pageable, uid);

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
    @GetMapping("/rlist")
    public String repBoardList(HttpServletRequest request
            , @PageableDefault(size = 10, page = 0, sort = "boardId", direction = Sort.Direction.ASC) Pageable pageable
            , Model model) {

        HttpSession session = request.getSession(false);
        Long uid = (Long) session.getAttribute("sessionId");

        /* 프로필 사진 보여주는 곳 */
        MyPageDTO result = service.getProfile(uid);
        model.addAttribute("result", result);

        /** 댓글 DTO 생기면 수정하기 */
        Page<RepBoardDTO> list = service.getRepList(pageable, uid);
        model.addAttribute("list", list);

        model.addAttribute("view", "mypage/repboardlist");
        return "index";
    }

    @GetMapping("/api")
    public String api(Model model) {
        model.addAttribute("view", "mypage/repboardlist");
        return "index";
    }

    @PostMapping("/api")
    public String loadAndSave(@RequestBody UserDTO dto, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long uid = (Long) session.getAttribute("sessionId");

        String schoolName = dto.getSchoolName();
        String schoolSido = dto.getSidoCode();

        System.out.println(schoolName);
        System.out.println(schoolSido);
        // 추가적인 처리 로직 수행
        // ...

        model.addAttribute("view", "mypage/home");
        return "index";
    }
}





