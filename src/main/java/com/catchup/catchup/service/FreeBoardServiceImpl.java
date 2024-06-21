package com.catchup.catchup.service;

import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.SearchCondition;
import com.catchup.catchup.repository.FreeBoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FreeBoardServiceImpl implements FreeBoardService{

    private final FreeBoardRepository freeRepository;
    private final ModelMapper modelMapper;

    public FreeBoardServiceImpl(FreeBoardRepository freeRepository, ModelMapper modelMapper) {
        this.freeRepository = freeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<FreeBoardDTO> boardList(String search, String searchTxt, Pageable pageable) {

        SearchCondition condition = new SearchCondition();
        condition.setTitle(null);
        condition.setContent(null);
        condition.setWriter(null);

        if("title".equals(search)){
            condition.setTitle(searchTxt);
        }else if("content".equals(search)){
            condition.setContent(searchTxt);
        }else if("writer".equals(search)){
            condition.setWriter(searchTxt);
        }

        Page<FreeBoardDTO> searchList = freeRepository.search(condition, pageable);

        return searchList;
    }

    @Override
    public List<FreeBoardDTO> boardDetail(Long fid) {
        List<FreeBoardDTO> detail = freeRepository.detail(fid);
        return detail;
    }
}
