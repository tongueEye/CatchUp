package com.catchup.catchup.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.catchup.catchup.domain.FreeBoard;
import com.catchup.catchup.domain.FreeRepBoard;
import com.catchup.catchup.domain.Love;
import com.catchup.catchup.domain.User;
import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.LoveDTO;
import com.catchup.catchup.dto.RepBoardDTO;
import com.catchup.catchup.dto.SearchCondition;
import com.catchup.catchup.repository.FreeBoardRepository;
import com.catchup.catchup.repository.FreeRepBoardRepository;
import com.catchup.catchup.repository.LoveRepository;
import com.catchup.catchup.repository.UserRepository;
import com.querydsl.core.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FreeBoardServiceImpl implements FreeBoardService {

    private final FreeBoardRepository freeRepository;
    private final FreeRepBoardRepository repRepository;
    private final UserRepository userRepository;
    private final LoveRepository loveRepository;
    private final ModelMapper modelMapper;

    public FreeBoardServiceImpl(FreeBoardRepository freeRepository, FreeRepBoardRepository repRepository
            , UserRepository userRepository, LoveRepository loveRepository, ModelMapper modelMapper) {
        this.freeRepository = freeRepository;
        this.repRepository = repRepository;
        this.userRepository = userRepository;
        this.loveRepository = loveRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<FreeBoardDTO> boardList(String search, String searchTxt, String kind, Pageable pageable) {

        SearchCondition condition = new SearchCondition();
        condition.setTitle(null);
        condition.setContent(null);
        condition.setWriter(null);
        condition.setKind("e");

        if ("title".equals(search)) {
            condition.setTitle(searchTxt);
        } else if ("content".equals(search)) {
            condition.setContent(searchTxt);
        } else if ("writer".equals(search)) {
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
                        .nickname((String) item[0])
                        .profile((String) item[1])
                        .frcontent((String) item[2])
                        .frCreateDate((LocalDateTime) item[3])
                        .frUpdateDate((LocalDateTime) item[4])
                        .frid((Long) item[5])
                        .build()
        ).collect(Collectors.toList());
//        QueryDSL
//        List<FreeBoardDTO> list = freeRepository.repList(fid);
        return list;
    }

    @Override
    public Long repInsert(RepBoardDTO dto, Long sessionId) {

        Optional<FreeBoard> rid = freeRepository.findById(dto.getFid());
        FreeBoard freeBoard = rid.orElseThrow(() -> new RuntimeException("error"));

        User userId = userRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Could not find uid"));

        FreeRepBoard repBoard = FreeRepBoard.builder()
                .frcontent(dto.getFrcontent())
                .fBoard(freeBoard)
                .user(userId)
                .build();
        FreeRepBoard save = repRepository.save(repBoard);
        return save.getFrid();
    }

    @Override
    public Long repDelete(Long frid) {
        repRepository.deleteById(frid);
        return frid;
    }

    @Override
    public boolean loveCheck(LoveDTO dto) {
        log.info("uid ....{}", dto.getUid());
        log.info("fid ....{}", dto.getFid());
        Love userBoardId = loveRepository.findUserBoardId(dto.getUid(), dto.getFid());
//        있으면 true 없으면 false return
        return userBoardId != null;
    }

    @Override
    @Transactional
    public void addLove(LoveDTO dto) {
        User user = userRepository.findById(dto.getUid())
                .orElseThrow(() -> new NotFoundException("Could not found uid"));
        FreeBoard freeBoard = freeRepository.findById(dto.getFid())
                .orElseThrow(() -> new NotFoundException("Could not found fid"));
        if(loveRepository.findByUserAndFreeBoard(user, freeBoard).isPresent()){
            throw new RuntimeException();
        }
        Love love = Love.builder()
                .user(user)
                .freeBoard(freeBoard)
                .build();
        loveRepository.save(love);
        freeRepository.addLove(dto.getFid());
    }

    @Override
    @Transactional
    public void delLove(LoveDTO dto) {
        User user = userRepository.findById(dto.getUid())
                .orElseThrow(() -> new NotFoundException("Could not found uid"));
        FreeBoard freeBoard = freeRepository.findById(dto.getFid())
                .orElseThrow(() -> new NotFoundException("Could not found fid"));
        Love love = (Love) loveRepository.findByUserAndFreeBoard(user, freeBoard)
                .orElseThrow(() -> new NotFoundException("Could not found lid"));

        loveRepository.delete(love);
        freeRepository.delLove(dto.getFid());
    }



}
