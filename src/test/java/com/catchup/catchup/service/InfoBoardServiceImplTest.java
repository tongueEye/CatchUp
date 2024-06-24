package com.catchup.catchup.service;


import com.catchup.catchup.dto.InfoBoardDTO;
import com.catchup.catchup.dto.SearchCondition;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InfoBoardServiceImplTest {

    @Autowired
    InfoBoardService infoBoardService;

    @Test
    public void test1(){

//        SearchCondition condition = new SearchCondition();
//
//
//        Pageable page = PageRequest.of(0, 10);
//
//        //Page<InfoBoardDTO> list = infoBoardService.getQnaList("cate","일", page);
//
//        //List<InfoBoardDTO> data = list.getContent();
//        for (InfoBoardDTO dto:data){
//            System.out.println(dto);
//        }
    }



}