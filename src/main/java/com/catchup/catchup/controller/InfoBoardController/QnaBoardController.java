package com.catchup.catchup.controller.InfoBoardController;

import com.catchup.catchup.domain.InfoBoard;
import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.InfoBoardDTO;
import com.catchup.catchup.dto.MyPageDTO;
import com.catchup.catchup.dto.UserDTO;
import com.catchup.catchup.service.InfoBoardService;
import com.catchup.catchup.service.MyPageService;
import com.catchup.catchup.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class QnaBoardController {

    private final InfoBoardService infoService;
    private final UserService userService;

    private final MyPageService myPageService;

    @GetMapping("/qna")
    public String qnaList(
            @RequestParam(required = false, defaultValue = "") String search
            , @RequestParam(required = false, defaultValue = "") String searchtxt
            , @RequestParam(required = false, defaultValue = "") String kind
            , @PageableDefault(size = 5, page = 0, sort = "iid", direction = Sort.Direction.ASC) Pageable pageable
            , HttpServletRequest request
            , Model model
    ){

        //세션으로 uid 가져오기
        Long uid  = 0L;
        HttpSession session = request.getSession(false);
        if(session!=null && session.getAttribute("sessionId")!=null) {
            uid = (Long) session.getAttribute("sessionId");
        }

        Page<InfoBoardDTO> qnaList = infoService.getQnaList(search, searchtxt, kind, pageable);
        int pageSize = 5;
        int startPage = ((int) Math.ceil(pageable.getPageNumber()/pageSize))*pageSize+1;
        int endPage = Math.min(startPage+pageSize-1, qnaList.getTotalPages());

        model.addAttribute("qnaList", qnaList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("search", search);
        model.addAttribute("searchtxt", searchtxt);
        model.addAttribute("uid",uid);
        model.addAttribute("view", "infoboard/qnaboard");
        return "index";
    }

    @GetMapping("/writeQna")
    public String qnaInsert(
            HttpServletRequest request
            , Model model
    ) {

        //세션으로 uid 가져오기
        Long uid  = 0L;
        HttpSession session = request.getSession(false);
        if(session!=null && session.getAttribute("sessionId")!=null) {
            uid = (Long) session.getAttribute("sessionId");
        }

        UserDTO userDTO = userService.findUserById(uid);

        //System.out.println("nickname:"+userDTO.getNickname());

        model.addAttribute("dto", new InfoBoardDTO());
        model.addAttribute("user", userDTO);
        model.addAttribute("uid", uid);
        model.addAttribute("view", "infoboard/qnaInsert");
        return "index";
    }


    @PostMapping("/writeQna")
    public String qnaInsertResult(
        @RequestParam(name = "cate", required = false) String cate
        , @RequestParam(name = "title", required = false) String title
        , @RequestParam(name = "content", required = false) String content
        , @RequestParam(name = "writer", required = false) String writer
        , @RequestParam(name = "kind", required = false) String kind
        , @RequestParam(name = "uid") long uid
    ) {

        InfoBoardDTO dto = InfoBoardDTO.builder()
                .cate(cate)
                .title(title)
                .content(content)
                .writer(writer)
                .kind(kind)
                .uid(uid)
                .createDate(LocalDateTime.now())
                .build();

        infoService.insert(dto);

        return "redirect:/qna";
    }

    @GetMapping("/qnaDetail/{iid}")
    public String qnaDetail(
            @PathVariable Long iid
            , HttpServletRequest request
            , Model model
    ){

        //세션으로 uid 가져오기
        Long uid  = 0L;
        HttpSession session = request.getSession(false);
        if(session!=null && session.getAttribute("sessionId")!=null) {
            uid = (Long) session.getAttribute("sessionId");
        }

        InfoBoardDTO dto = infoService.getDetail(iid);

        MyPageDTO myprofile = myPageService.getProfile(dto.getUid());

        model.addAttribute("dto", dto);
        model.addAttribute("profile", myprofile.getProfile());
        model.addAttribute("uid", uid);
        model.addAttribute("view", "infoBoard/qnaDetail");
        return "index";
    }

    @GetMapping("/qnaDelete/{iid}")
    public String qnaDelete(
            @PathVariable Long iid
            , Model model
    ){
        Long id = infoService.delQna(iid);
        return "redirect:/qna";
    }

    @GetMapping("/qnaUpdate/{iid}")
    public String qnaUpdate(
            @PathVariable Long iid
            , Model model
    ){
        InfoBoardDTO infoDto = infoService.getDetail(iid);

        //세션 받은 후 이부분 수정해야 함.
        UserDTO userDTO = userService.findUserById(101L);

        model.addAttribute("dto", infoDto);
        model.addAttribute("user", userDTO);
        model.addAttribute("view", "infoBoard/qnaUpdate");
        return "index";
    }

    @PostMapping("/qnaUpdate/{iid}")
    public String qnaUpdateResult(
            @PathVariable Long iid
            , @RequestParam(name = "cate", required = false) String cate
            , @RequestParam(name = "title", required = false) String title
            , @RequestParam(name = "content", required = false) String content
            , @RequestParam(name = "writer", required = false) String writer
            , @RequestParam(name = "kind", required = false) String kind
            , @RequestParam(name = "uid") long uid

    ) {

        InfoBoardDTO dto = InfoBoardDTO.builder()
                .cate(cate)
                .title(title)
                .content(content)
                .writer(writer)
                .kind(kind)
                .uid(uid)
                .createDate(LocalDateTime.now())
                .iid(iid)
                .build();


        Long update_iid = infoService.updateQna(dto);

        return "redirect:/qnaDetail/"+update_iid;
    }
}
