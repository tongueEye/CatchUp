package com.catchup.catchup.service;

import com.catchup.catchup.dto.*;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FreeBoardService {
    Page<FreeBoardDTO> boardList(String search, String searchTxt, String kind, Pageable pageable);
    List<FreeBoardDTO> mostView();
    List<FreeBoardDTO> mostLike();

    FreeBoardDTO boardDetail(Long fid);

    Long boardInsert(FreeBoardDTO dto);

    List<FreeBoardDTO> repList(Long fid);

    Long repInsert(RepBoardDTO dto, Long sessionId);

    Long repDelete(Long frid);

    void addLove(LoveDTO dto);

    void delLove(LoveDTO dto);

    boolean loveCheck(LoveDTO dto);

    Long boardDelete(Long fid);

    Long boardUpdate(FreeBoardDTO dto);

}
