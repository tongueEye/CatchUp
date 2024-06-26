package com.catchup.catchup.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.catchup.catchup.domain.*;
import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.LoveDTO;
import com.catchup.catchup.dto.RepBoardDTO;
import com.catchup.catchup.dto.SearchCondition;
import com.catchup.catchup.repository.FreeBoardRepository;
import com.catchup.catchup.repository.FreeRepBoardRepository;
import com.catchup.catchup.repository.LoveRepository;
import com.catchup.catchup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements FreeBoardService {

    private final FreeBoardRepository freeRepository;
    private final FreeRepBoardRepository repRepository;
    private final UserRepository userRepository;
    private final LoveRepository loveRepository;
    private final ModelMapper modelMapper;
    private final AWSService awsService;


    /** 게시판 **/
    @Override
    public Page<FreeBoardDTO> boardList(String search, String searchTxt, String kind, Pageable pageable) {

        SearchCondition condition = new SearchCondition();
        condition.setTitle(null);
        condition.setContent(null);
        condition.setWriter(null);
        condition.setCate(null);
        condition.setKind("e");


        if ("title".equals(search)) {
            condition.setTitle(searchTxt);
        } else if ("content".equals(search)) {
            condition.setContent(searchTxt);
        } else if ("writer".equals(search)) {
            condition.setWriter(searchTxt);
        } else if ("cate".equals(search)){
            condition.setCate(searchTxt);
        }

        Page<FreeBoardDTO> searchList = freeRepository.search(condition, pageable);

        return searchList;
    }

    @Override
    public List<FreeBoardDTO> mostView() {
        List<FreeBoardDTO> hotList = freeRepository.mostView();
        return hotList;
    }

    @Override
    public List<FreeBoardDTO> mostLike() {
        List<FreeBoardDTO> likeList = freeRepository.mostLike();
        return likeList;
    }

    /** 게시글 세부 **/
    @Override
    public FreeBoardDTO boardDetail(Long fid) {
        Optional<FreeBoard> freeBoard = freeRepository.findById(fid);
        FreeBoardDTO dto = modelMapper.map(freeBoard, FreeBoardDTO.class);
        freeRepository.updateCount(fid);
        return dto;
    }

    /** 게시글 작성 **/
    @Override
    public Long boardInsert(FreeBoardDTO dto) {
        FreeBoard freeBoard = modelMapper.map(dto, FreeBoard.class);
        User user = new User();
        user.setUid(dto.getUid());
        freeBoard.setUser(user);
        FreeBoard save = freeRepository.save(freeBoard);

        return save.getFid();
    }

    /** 게시글 수정 **/
    @Override
    @Transactional
    public Long boardUpdate(FreeBoardDTO dto) {
        Optional<FreeBoard> getEntityById = freeRepository.findById(dto.getFid());

        FreeBoard freeBoard = getEntityById.orElseThrow(()->{
            throw new RuntimeException("+++NO DATA+++");
        });

        freeBoard.setCate(dto.getCate());
        freeBoard.setTitle(dto.getTitle());
        freeBoard.setContent(dto.getContent());

        return dto.getFid();
    }


    /** 게시글 삭제 **/
    @Override
    public Long boardDelete(Long fid) {
        //DB에 저장된 board의 content값 string으로 가져옴
        FreeBoard board = freeRepository.findById(fid).orElseThrow(()-> new RuntimeException());
        String content = board.getContent();

        // HTML 콘텐츠에서 이미지 URL을 추출
        Document doc = Jsoup.parse(content);
        Elements imgs = doc.select("img");

        for (Element img : imgs) {
            String imageUrl = img.attr("src");

            // S3에서 파일을 삭제
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // imageUrl에서 버킷 내 경로를 추출합니다.
                String key = extractKeyFromUrl(imageUrl, board.getKind());
                awsService.deleteFile(key);
            }
        }

        //S3에서 데이터 삭제 후, DB에서 데이터 삭제
        freeRepository.deleteById(fid);
        return fid;
    }

    private String extractKeyFromUrl(String imageUrl, String boardKind) {
        // imageUrl에서 S3 버킷 내의 경로를 추출하는 코드
        // "%2F" 이후의 부분을 추출
        //"https://버킷명.s3.지역.amazonaws.com/폴더명%2F/경로에 저장된 이미지 이름" 일 때, 경로에 저장된 이미지 이름 추출

        int startIndex = imageUrl.indexOf("%2F") + 3;

        if (startIndex < 3) { // 파일이 없다면 예외 던짐
            throw new IllegalArgumentException("Invalid S3 URL");
        }
        //System.out.println("test0625 이미지url 추출:"+imageUrl.substring(startIndex));

        String delete_file = imageUrl.substring(startIndex);

        if (boardKind.equals("e")){
            delete_file = "edu/"+imageUrl.substring(startIndex);
        } else if (boardKind.equals("c")) {
            delete_file = "comu/"+imageUrl.substring(startIndex);
        }

        //System.out.println("test0625 이미지url 경로:"+delete_file);

        return delete_file; //파싱한 이미지 이름값 리턴
    }


    /** 댓글 리스트 **/
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
        return list;
    }

    /** 댓글 작성 **/
    @Override
    public Long repInsert(RepBoardDTO dto, Long sessionId) {

        Optional<FreeBoard> rid = freeRepository.findById(dto.getFid());
        FreeBoard freeBoard = rid.orElseThrow(() -> new RuntimeException("error"));

        User userId = userRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Could not find uid"));

        FreeRepBoard repBoard = FreeRepBoard.builder()
                .frcontent(dto.getFrcontent())
                .freeBoard(freeBoard)
                .user(userId)
                .build();
        FreeRepBoard save = repRepository.save(repBoard);
        return save.getFrid();
    }

    /** 댓글 삭제 **/
    @Override
    public Long repDelete(Long frid) {
        repRepository.deleteById(frid);
        return frid;
    }

    /** 좋아요 여부 확인 **/
    @Override
    public boolean loveCheck(LoveDTO dto) {
        log.info("uid ....{}", dto.getUid());
        log.info("fid ....{}", dto.getFid());
        Love userBoardId = loveRepository.findUserBoardId(dto.getUid(), dto.getFid());
//        있으면 true 없으면 false return
        return userBoardId != null;
    }


    /** 좋아요 추가  **/
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
    }

    /** 좋아요 삭제  **/
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
    }
    @Override
    public Page<FreeBoardDTO> comboardList(String search, String searchTxt, String kind, Pageable pageable) {
        SearchCondition condition = new SearchCondition();
        condition.setTitle(null);
        condition.setContent(null);
        condition.setWriter(null);
        condition.setCate(null);
        condition.setKind("c");


        if ("title".equals(search)) {
            condition.setTitle(searchTxt);
        } else if ("content".equals(search)) {
            condition.setContent(searchTxt);
        } else if ("writer".equals(search)) {
            condition.setWriter(searchTxt);
        } else if ("cate".equals(search)){
            condition.setCate(searchTxt);
        }

        Page<FreeBoardDTO> searchList = freeRepository.search(condition, pageable);

        return searchList;
    }

    @Override
    public List<FreeBoardDTO> mostViewC() {
        List<FreeBoardDTO> hotList = freeRepository.mostViewC();
        return hotList;
    }

    @Override
    public List<FreeBoardDTO> mostLikeC() {
        List<FreeBoardDTO> likeList = freeRepository.mostLikeC();
        return likeList;
    }

}