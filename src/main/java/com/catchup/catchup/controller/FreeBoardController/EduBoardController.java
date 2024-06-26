package com.catchup.catchup.controller.FreeBoardController;

import com.catchup.catchup.dto.*;
import com.catchup.catchup.service.FreeBoardService;
import com.catchup.catchup.service.MyPageService;
import com.catchup.catchup.service.UserService;
import com.querydsl.core.Tuple;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class EduBoardController {

    private final FreeBoardService freeService;
    private final UserService userService;
    private final MyPageService myService;

    /**
     * 게시글 목록
     **/
    @GetMapping("/eduboard")
    public String comList(
            @RequestParam(required = false, defaultValue = "") String search
            , @RequestParam(required = false, defaultValue = "") String searchTxt
            , @RequestParam(required = false, defaultValue = "") String kind
            , @PageableDefault(size = 5, page = 0, sort = "fid", direction = Sort.Direction.ASC) Pageable pageable
            , HttpServletRequest request
            , Model model
    ) {
        Long sessionId = 0L;
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("sessionId") != null) {
            sessionId = (Long) session.getAttribute("sessionId");
        }

        Page<FreeBoardDTO> boardList = freeService.boardList(search, searchTxt, kind, pageable);
        int pageSize = 5;
        int startPage = ((int) (Math.ceil(pageable.getPageNumber() / pageSize))) * pageSize + 1;
        int endPage = Math.min(startPage + pageSize - 1, boardList.getTotalPages());

        List<FreeBoardDTO> hotList = freeService.mostView();
        List<FreeBoardDTO> likeList = freeService.mostLike();

        model.addAttribute("sessionId", sessionId);
        model.addAttribute("hotList", hotList);
        model.addAttribute("likeList", likeList);
        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("search", search);
        model.addAttribute("searchTxt", searchTxt);
        model.addAttribute("view", "freeBoard/eduBoard");
        return "index";
    }

    /**
     * 게시글 세부
     **/
    @GetMapping("/edudetail/{fid}")
    public String comDetail(@PathVariable Long fid, HttpServletRequest request, Model model) {

        Long sessionId = 0L;
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("sessionId") != null) {
            sessionId = (Long) session.getAttribute("sessionId");
        }

        List<FreeBoardDTO> writerInfo = freeService.getWriterInfo(fid);
        FreeBoardDTO dto = freeService.boardDetail(fid);
        Long repCount = freeService.repCount(fid);

        model.addAttribute("sessionId", sessionId);
        model.addAttribute("dto", dto);
        model.addAttribute("writerInfo", writerInfo);
        model.addAttribute("repCount", repCount);
        model.addAttribute("view", "freeBoard/eduDetail");
        return "index";
    }

    /**
     * 게시글 작성 페이지
     **/
    @GetMapping("/writeEdu")
    public String boardInsert(HttpServletRequest request, Model model) {

        Long sessionId = 0L;
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("sessionId") != null) {
            sessionId = (Long) session.getAttribute("sessionId");
        }

        UserDTO userDTO = userService.findUserById(sessionId);

        model.addAttribute("dto", new FreeBoardDTO());
        model.addAttribute("user", userDTO);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("view", "freeBoard/eduBoardInsert");
        return "index";
    }

    /**
     * 게시글 작성 폼
     **/
    @PostMapping("/writeEdu")
    public String boardInsertResult(
            @RequestParam(name = "cate", required = false) String cate
            , @RequestParam(name = "title", required = false) String title
            , @RequestParam(name = "content", required = false) String content
            , @RequestParam(name = "writer", required = false) String writer
            , @RequestParam(name = "kind", required = false) String kind
            , @RequestParam(name = "count", required = false) Integer count
            , @RequestParam(name = "sessionId") long sessionId
    ) {

        FreeBoardDTO dto = FreeBoardDTO.builder()
                .cate(cate)
                .title(title)
                .content(content)
                .writer(writer)
                .kind(kind)
                .count(0)
                .uid(sessionId)
                .createDate(LocalDateTime.now())
                .build();

        freeService.boardInsert(dto);
        return "redirect:/eduboard";

    }

    /**
     * 게시글 수정 페이지
     **/
    @GetMapping("/boardUpdate/{fid}")
    public String boardUpdate(@PathVariable Long fid, HttpServletRequest request, Model model) {

        Long sessionId = 0L;
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("sessionId") != null) {
            sessionId = (Long) session.getAttribute("sessionId");
        }

        FreeBoardDTO dto = freeService.boardDetail(fid);
        UserDTO userDTO = userService.findUserById(sessionId);

        model.addAttribute("dto", dto);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("user", userDTO);
        model.addAttribute("view", "freeBoard/eduBoardUpdate");
        return "index";
    }

    /**
     * 게시글 수정 폼
     **/
    @PostMapping("/boardUpdate/{fid}")
    public String boardUpdateResult(
            @PathVariable Long fid
            , @RequestParam(name = "cate", required = false) String cate
            , @RequestParam(name = "title", required = false) String title
            , @RequestParam(name = "content", required = false) String content
            , @RequestParam(name = "writer", required = false) String writer
            , @RequestParam(name = "kind", required = false) String kind
            , @RequestParam(name = "count", required = false) Integer count
            , @RequestParam(name = "sessionId") long sessionId

    ) {

        FreeBoardDTO dto = FreeBoardDTO.builder()
                .cate(cate)
                .title(title)
                .content(content)
                .writer(writer)
                .kind(kind)
                .count(count)
                .uid(sessionId)
                .createDate(LocalDateTime.now())
                .fid(fid)
                .build();


        Long update_fid = freeService.boardUpdate(dto);

        return "redirect:/edudetail/" + update_fid;
    }

    /**
     * 게시글 삭제
     **/
    @GetMapping("/boardDelete/{fid}")
    public String qnaDelete(@PathVariable Long fid, Model model) {

        Long id = freeService.boardDelete(fid);
        return "redirect:/eduboard";
    }


    /**
     * 댓글 리스트
     **/
    @GetMapping("/replist/{fid}")
    public ResponseEntity<List<FreeBoardDTO>> repList(@PathVariable Long fid) {
        List<FreeBoardDTO> list = freeService.repList(fid);
        return ResponseEntity.ok().body(list);
    }

    /**
     * 댓글 추가
     **/
    @PostMapping("/repinsert")
    public ResponseEntity<List<FreeBoardDTO>> repInsert(@RequestBody RepBoardDTO dto, HttpServletRequest request, Model model) {

        Long sessionId = 0L;
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("sessionId") != null) {
            sessionId = (Long) session.getAttribute("sessionId");
        }

        Long result = freeService.repInsert(dto, sessionId);
        List<FreeBoardDTO> repList = freeService.repList(dto.getFid());
        return ResponseEntity.ok().body(repList);
    }

    /**
     * 댓글 삭제
     **/
    @GetMapping("/repdelete/{frid}")
    @ResponseBody
    public ResponseEntity<String> repDelete(@PathVariable Long frid) {
        Long result = freeService.repDelete(frid);
        return ResponseEntity.ok("Deleted successfully");
    }


    @GetMapping("/repcount/{fid}")
    @ResponseBody
    public ResponseEntity<Long> repCount(@PathVariable Long fid) {
        Long result = freeService.repCount(fid);
        return ResponseEntity.ok().body(result);
    }
}
