package com.catchup.catchup.service;

import com.catchup.catchup.dto.InfoBoardDTO;
import com.catchup.catchup.dto.SearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class InfoBoardServiceImpl implements InfoBoardService{

    @Override
    public Page<InfoBoardDTO> getQnaList(String search, String searchTxt, Pageable pageable) {

        SearchCondition condition = new SearchCondition();

        condition.setCate(null);
        condition.setTitle(null);
        condition.setContent(null);

        if("cate".equals(search) && search!=null && !"".equals(search)){

        }

        return null;
    }



}
