package com.catchup.catchup.service;

import com.catchup.catchup.domain.FreeBoard;
import com.catchup.catchup.domain.FreeRepBoard;
import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.RepBoardDTO;
import com.catchup.catchup.dto.SearchCondition;
import com.catchup.catchup.repository.FreeBoardRepository;
import com.catchup.catchup.repository.FreeRepBoardRepository;
import com.querydsl.core.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FreeBoardServiceImpl implements FreeBoardService{

    private final FreeBoardRepository freeRepository;
    private final FreeRepBoardRepository repRepository;
    private final ModelMapper modelMapper;

    public FreeBoardServiceImpl(FreeBoardRepository freeRepository, FreeRepBoardRepository repRepository, ModelMapper modelMapper) {
        this.freeRepository = freeRepository;
        this.repRepository = repRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<FreeBoardDTO> boardList(String search, String searchTxt, String kind, Pageable pageable) {

        SearchCondition condition = new SearchCondition();
        condition.setTitle(null);
        condition.setContent(null);
        condition.setWriter(null);
        condition.setKind("e");

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

    @Override
    public List<FreeBoardDTO> repList(Long fid) {
        List<Object[]> objects = freeRepository.repList(fid);
        List<FreeBoardDTO> list = objects.stream().map(item ->
                FreeBoardDTO.builder()
                        .frcontent((String) item[0])
                        .nickname((String) item[1])
                        .profile((String) item[2])
                        .frCreateDate((LocalDateTime) item[3])
                        .frUpdateDate((LocalDateTime) item[4])
                        .build()
        ).collect(Collectors.toList());
//        QueryDSL
//        List<FreeBoardDTO> list = freeRepository.repList(fid);
        return list;
    }

    @Override
    public Long repInsert(RepBoardDTO dto) {

        Optional<FreeBoard> rid = freeRepository.findById(dto.getFid());
        FreeBoard freeBoard = rid.orElseThrow(() -> new RuntimeException("error"));
        FreeRepBoard repBoard = FreeRepBoard.builder()
                .frcontent(dto.getFrcontent())
                .fBoard(freeBoard)
                .build();
        FreeRepBoard save = repRepository.save(repBoard);
        return save.getFrid();
    }

    @Override
    public Long repDelete(Long frid) {
        repRepository.deleteById(frid);
        return frid;
    }
}
