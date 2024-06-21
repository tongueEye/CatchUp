package com.catchup.catchup.controller.InfoBoardController;

import com.catchup.catchup.dto.InfoBoardDTO;
import com.catchup.catchup.service.InfoBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class NoticeBoardController {

    private final InfoBoardService infoService;

    @GetMapping("/notice")
    public String qnaList(
            @RequestParam(required = false, defaultValue = "") String search
            , @RequestParam(required = false, defaultValue = "") String searchtxt
            , @RequestParam(required = false, defaultValue = "") String kind
            , @PageableDefault(size = 5, page = 0, sort = "iid", direction = Sort.Direction.ASC) Pageable pageable
            , Model model
    ){

        Page<InfoBoardDTO> noticeList = infoService.getNoticeList(search, searchtxt, kind, pageable);
        int pageSize = 5;
        int startPage = ((int) Math.ceil(pageable.getPageNumber()/pageSize))*pageSize+1;
        int endPage = Math.min(startPage+pageSize-1, noticeList.getTotalPages());
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("search", search);
        model.addAttribute("searchtxt", searchtxt);

        model.addAttribute("view", "infoboard/noticeboard");
        return "index";
    }
}
