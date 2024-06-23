package com.catchup.catchup.controller.InfoBoardController;

import com.catchup.catchup.domain.InfoBoard;
import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.InfoBoardDTO;
import com.catchup.catchup.dto.UserDTO;
import com.catchup.catchup.service.InfoBoardService;
import com.catchup.catchup.service.UserService;
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

    @GetMapping("/qna")
    public String qnaList(
            @RequestParam(required = false, defaultValue = "") String search
            , @RequestParam(required = false, defaultValue = "") String searchtxt
            , @RequestParam(required = false, defaultValue = "") String kind
            , @PageableDefault(size = 5, page = 0, sort = "iid", direction = Sort.Direction.ASC) Pageable pageable
            , Model model
    ){

        Page<InfoBoardDTO> qnaList = infoService.getQnaList(search, searchtxt, kind, pageable);
        int pageSize = 5;
        int startPage = ((int) Math.ceil(pageable.getPageNumber()/pageSize))*pageSize+1;
        int endPage = Math.min(startPage+pageSize-1, qnaList.getTotalPages());
        model.addAttribute("qnaList", qnaList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("search", search);
        model.addAttribute("searchtxt", searchtxt);

        model.addAttribute("view", "infoboard/qnaboard");
        return "index";
    }

    @GetMapping("/writeQna")
    public String qnaInsert(Model model) {

        //세션 받은 후 이부분 수정해야 함.
        UserDTO userDTO = userService.findUserById(101L);

        System.out.println("nickname:"+userDTO.getNickname());

        model.addAttribute("dto", new InfoBoardDTO());
        model.addAttribute("user", userDTO);
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
            , Model model
    ){
        InfoBoardDTO dto = infoService.getDetail(iid);
        model.addAttribute("dto", dto);
        model.addAttribute("view", "infoBoard/qnaDetail");
        return "index";
    }
}
