package com.catchup.catchup.controller.FreeBoardController;

import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.RepBoardDTO;
import com.catchup.catchup.service.FreeBoardService;
import com.querydsl.core.Tuple;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class EduBoardController {

    private FreeBoardService freeService;

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

    @GetMapping("/edudetail/{fid}")
    public String comDetail(@PathVariable Long fid, Model model){
        List<FreeBoardDTO> list = freeService.boardDetail(fid);

        /** session ID **/
        model.addAttribute("sessionId", 1);
        model.addAttribute("list", list);
        model.addAttribute("view", "freeBoard/eduDetail");
        return "index";
    }

    @GetMapping("/write")
    public String boardInsert(Model model) {
            model.addAttribute("view", "freeBoard/boardInsert");
        return "index";
    }
////
////    @PostMapping
////    public String comInsertResult(FreeBoardDTO dto){
////
////        return null;
////    }
////
////    @GetMapping
////    public String comUpdate(Long fid){
////
////        return null;
////    }
////
////    @PostMapping
////    public String comUpdateResult(FreeBoardDTO dto){
////
////        return null;
////    }
////
////    @PostMapping
////    public String comDelete(Long fid){
////        return null;
////    }

    @GetMapping("/replist/{fid}")
    public ResponseEntity<List<FreeBoardDTO>> repList(@PathVariable Long fid){
        List<FreeBoardDTO> list = freeService.repList(fid);
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/repinsert")
    public ResponseEntity<List<FreeBoardDTO>> repInsert(@RequestBody RepBoardDTO dto){
        Long result = freeService.repInsert(dto);
        List<FreeBoardDTO> repList = freeService.repList(dto.getFid());
        return ResponseEntity.ok().body(repList);
    }

    @GetMapping ("/repdelete/{frid}")
    @ResponseBody
    public ResponseEntity<String> repDelete(@PathVariable Long frid) {
        Long result = freeService.repDelete(frid);
        return ResponseEntity.ok("Deleted successfully");
    }

}
