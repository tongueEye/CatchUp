package com.catchup.catchup.controller.InfoBoardController;

import com.catchup.catchup.domain.InfoBoard;
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
public class QnaBoardController {

    private final InfoBoardService infoService;

    @GetMapping("/qnaboard")
    public String qnaList(
            @RequestParam(required = false, defaultValue = "") String search
            , @RequestParam(required = false, defaultValue = "") String searchTxt
            , @PageableDefault(size = 5, page = 0, sort = "iid", direction = Sort.Direction.ASC) Pageable pageable
            , Model model
    ){

        Page<InfoBoardDTO> qnaList = infoService.getQnaList(search, searchTxt, pageable);
        int pageSize = 5;
        int startPage = ((int) Math.ceil(pageable.getPageNumber()/pageSize))*pageSize+1;
        int endPage = Math.min(startPage+pageSize-1, qnaList.getTotalPages());

        model.addAttribute("qnaList", qnaList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("search", search);
        model.addAttribute("searchTxt", searchTxt);

        model.addAttribute("view", "freeboard/qnaboard");
        return "index";
    }
}
