package com.catchup.catchup.controller.InfoBoardController;

import com.catchup.catchup.dto.InfoBoardDTO;
import com.catchup.catchup.dto.UserDTO;
import com.catchup.catchup.service.InfoBoardService;
import com.catchup.catchup.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class NoticeBoardController {

    private final InfoBoardService infoService;

    private final UserService userService;

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


    @GetMapping("/writeNotice")
    public String noticeInsert(Model model) {

        //세션 받은 후 이부분 수정해야 함.
        UserDTO userDTO = userService.findUserById(101L);

        //System.out.println("nickname:"+userDTO.getNickname());

        model.addAttribute("dto", new InfoBoardDTO());
        model.addAttribute("user", userDTO);
        model.addAttribute("view", "infoboard/noticeInsert");
        return "index";
    }

    @PostMapping("/writeNotice")
    public String noticeInsertResult(
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

        return "redirect:/notice";
    }


    @GetMapping("/noticeDetail/{iid}")
    public String noticeDetail(
            @PathVariable Long iid
            , Model model
    ){
        InfoBoardDTO dto = infoService.getDetail(iid);
        model.addAttribute("dto", dto);
        model.addAttribute("view", "infoBoard/noticeDetail");
        return "index";
    }

    @GetMapping("/noticeDelete/{iid}")
    public String noticeDelete(
            @PathVariable Long iid
            , Model model
    ){
        Long id = infoService.delQna(iid);
        return "redirect:/notice";
    }

    @GetMapping("/noticeUpdate/{iid}")
    public String qnaUpdate(
            @PathVariable Long iid
            , Model model
    ){
        InfoBoardDTO infoDto = infoService.getDetail(iid);

        //세션 받은 후 이부분 수정해야 함.
        UserDTO userDTO = userService.findUserById(101L);

        model.addAttribute("dto", infoDto);
        model.addAttribute("user", userDTO);
        model.addAttribute("view", "infoBoard/noticeUpdate");
        return "index";
    }

    @PostMapping("/noticeUpdate/{iid}")
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

        return "redirect:/noticeDetail/"+update_iid;
    }



}
