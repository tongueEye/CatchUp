package com.catchup.catchup.service;

import com.catchup.catchup.dto.InfoBoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InfoBoardService {
    Page<InfoBoardDTO> getQnaList(String search, String searchTxt, String kind, Pageable pageable);

    Page<InfoBoardDTO> getNoticeList(String search, String searchtxt, String kind, Pageable pageable);

    Long insert(InfoBoardDTO dto);
}
