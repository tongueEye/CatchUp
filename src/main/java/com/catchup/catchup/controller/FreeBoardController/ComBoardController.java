package com.catchup.catchup.controller.FreeBoardController;

import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.service.FreeBoardService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class ComBoardController {

    private FreeBoardService freeService;

    @GetMapping("/comboard")
    public String comList(
            @RequestParam(required = false, defaultValue = "") String search
            , @RequestParam(required = false, defaultValue = "") String searchTxt
            , @PageableDefault(size = 5, page = 0, sort = "fid", direction = Sort.Direction.ASC) Pageable pageable
            , Model model
    ){
//        Page<FreeBoardDTO> boardList = freeService.boardList(search, searchTxt, pageable);
//        int pageSize = 5;
//        int startPage
//                = ((int)(Math.ceil(pageable.getPageNumber()/pageSize)))
//                *pageSize+1;
//        int endPage=Math.min(startPage+ pageSize-1
//                , boardList.getTotalPages());
//
//        model.addAttribute("boardList", boardList);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//        model.addAttribute("search", search);
//        model.addAttribute("searchTxt", searchTxt);
        model.addAttribute("view", "freeboard/comboard");
        return "index";
    }

//    @GetMapping
////    public String comDetail(Long fid){
////
////        return null;
////    }
////
////    @GetMapping
////    public String comInsert() {
////
////        return null;
////    }
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
}
