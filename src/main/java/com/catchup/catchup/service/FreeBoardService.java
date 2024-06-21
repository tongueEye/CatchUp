package com.catchup.catchup.service;

import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.SearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FreeBoardService {
    Page<FreeBoardDTO> boardList(String search, String searchTxt, String kind, Pageable pageable);


    List<FreeBoardDTO> boardDetail(Long fid);
}
