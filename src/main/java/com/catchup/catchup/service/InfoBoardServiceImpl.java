package com.catchup.catchup.service;

import com.catchup.catchup.dto.InfoBoardDTO;
import com.catchup.catchup.dto.SearchCondition;
import com.catchup.catchup.repository.InfoBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InfoBoardServiceImpl implements InfoBoardService{

    private final InfoBoardRepository infoRepository;

    @Override
    public Page<InfoBoardDTO> getQnaList(String search, String searchtxt, String kind, Pageable pageable) {

        SearchCondition condition = new SearchCondition();

        condition.setCate(null);
        condition.setTitle(null);
        condition.setContent(null);
        condition.setKind("q");

        if("cate".equals(search) && search!=null && !"".equals(search)){
            condition.setCate(searchtxt);
        } else if("title".equals(search) && search!=null && !"".equals(search)){
            condition.setTitle(searchtxt);
        } else if("content".equals(search) && search!=null && !"".equals(search)){
                condition.setContent(searchtxt);
        }

        Page<InfoBoardDTO> list = infoRepository.search(condition, pageable);

        return list;
    }

}
