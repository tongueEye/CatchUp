package com.catchup.catchup.controller.FreeBoardController;

import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.InfoBoardDTO;
import com.catchup.catchup.dto.RepBoardDTO;
import com.catchup.catchup.dto.UserDTO;
import com.catchup.catchup.service.FreeBoardService;
import com.catchup.catchup.service.UserService;
import com.querydsl.core.Tuple;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class EduBoardController {

    private FreeBoardService freeService;
    private final UserService userService;

    /** 게시글 목록 **/
    @GetMapping("/eduboard")
    public String comList(
            @RequestParam(required = false, defaultValue = "") String search
            , @RequestParam(required = false, defaultValue = "") String searchTxt
            , @RequestParam(required = false, defaultValue = "") String kind
            , @PageableDefault(size = 5, page = 0, sort = "fid", direction = Sort.Direction.ASC) Pageable pageable
            , Model model
    ){
        Page<FreeBoardDTO> boardList = freeService.boardList(search, searchTxt, kind, pageable);
        int pageSize = 5;
        int startPage = ((int)(Math.ceil(pageable.getPageNumber()/pageSize))) *pageSize+1;
        int endPage=Math.min(startPage+ pageSize-1, boardList.getTotalPages());

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("search", search);
        model.addAttribute("searchTxt", searchTxt);
        model.addAttribute("view", "freeBoard/eduBoard");
        return "index";
    }
    
    /** 게시글 세부 **/
    @GetMapping("/edudetail/{fid}")
    public String comDetail(@PathVariable Long fid, Model model){
        FreeBoardDTO dto = freeService.boardDetail(fid);

        /** session ID **/
        model.addAttribute("sessionId", 100);
        model.addAttribute("dto", dto);
        model.addAttribute("view", "freeBoard/eduDetail");
        return "index";
    }

    /** 게시글 작성 페이지 **/
    @GetMapping("/writeFree")
    public String boardInsert(Model model) {
        //세션 받은 후 이부분 수정해야 함.
        UserDTO userDTO = userService.findUserById(100L);

        model.addAttribute("dto", new FreeBoardDTO());
        model.addAttribute("user", userDTO);
            model.addAttribute("view", "freeBoard/boardInsert");
        return "index";
    }
    /** 게시글 작성 폼 **/
    @PostMapping("/writeFree")
    public String boardInsertResult(
            @RequestParam(name = "cate", required = false) String cate
            , @RequestParam(name = "title", required = false) String title
            , @RequestParam(name = "content", required = false) String content
            , @RequestParam(name = "writer", required = false) String writer
            , @RequestParam(name = "kind", required = false) String kind
            , @RequestParam(name = "cnt", required = false) Integer cnt
            , @RequestParam(name = "uid") long uid
    ) {

        FreeBoardDTO dto = FreeBoardDTO.builder()
                .cate(cate)
                .title(title)
                .content(content)
                .writer(writer)
                .kind(kind)
                .cnt(cnt)
                .uid(uid)
                .createDate(LocalDateTime.now())
                .build();

        freeService.boardInsert(dto);

        return "redirect:/eduboard";
    }

    /** 게시글 수정 페이지 **/
    @GetMapping("/boardUpdate/{fid}")
    public String qnaUpdate(@PathVariable Long fid, Model model){
        FreeBoardDTO dto = freeService.boardDetail(fid);

        //세션 받은 후 이부분 수정해야 함.
        UserDTO userDTO = userService.findUserById(101L);

        model.addAttribute("dto", dto);
        model.addAttribute("user", userDTO);
        model.addAttribute("view", "freeBoard/boardUpdate");
        return "index";
    }

    /** 게시글 수정 폼 **/
    @PostMapping("/boardUpdate/{fid}")
    public String qnaUpdateResult(
            @PathVariable Long fid
            , @RequestParam(name = "cate", required = false) String cate
            , @RequestParam(name = "title", required = false) String title
            , @RequestParam(name = "content", required = false) String content
            , @RequestParam(name = "writer", required = false) String writer
            , @RequestParam(name = "kind", required = false) String kind
            , @RequestParam(name = "cnt", required = false) Integer cnt
            , @RequestParam(name = "uid") long uid

    ) {

        FreeBoardDTO dto = FreeBoardDTO.builder()
                .cate(cate)
                .title(title)
                .content(content)
                .writer(writer)
                .kind(kind)
                .cnt(cnt)
                .uid(uid)
                .createDate(LocalDateTime.now())
                .fid(fid)
                .build();


        Long update_fid = freeService.boardUpdate(dto);

        return "redirect:/edudetail/"+update_fid;
    }

    /** 게시글 삭제 **/
    @GetMapping("/boardDelete/{fid}")
    public String qnaDelete(@PathVariable Long fid, Model model){

        Long id = freeService.boardDelete(fid);
        return "redirect:/eduboard";
    }


    /** 댓글 리스트 **/
    @GetMapping("/replist/{fid}")
    public ResponseEntity<List<FreeBoardDTO>> repList(@PathVariable Long fid){
        List<FreeBoardDTO> list = freeService.repList(fid);
        return ResponseEntity.ok().body(list);
    }
    
    /** 댓글 추가 **/
    @PostMapping("/repinsert")
    public ResponseEntity<List<FreeBoardDTO>> repInsert(@RequestBody RepBoardDTO dto, HttpServletRequest request){
        // sessionID 후에 수정
        Long sessionId = 100L;
//        log.info("Session ID: " + sessionId);

        Long result = freeService.repInsert(dto, sessionId);
        List<FreeBoardDTO> repList = freeService.repList(dto.getFid());
        return ResponseEntity.ok().body(repList);
    }
    
    /** 댓글 삭제 **/
    @GetMapping ("/repdelete/{frid}")
    @ResponseBody
    public ResponseEntity<String> repDelete(@PathVariable Long frid) {
        Long result = freeService.repDelete(frid);
        return ResponseEntity.ok("Deleted successfully");
    }

}
