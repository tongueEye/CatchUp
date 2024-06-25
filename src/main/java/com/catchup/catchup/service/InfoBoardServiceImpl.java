package com.catchup.catchup.service;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.catchup.catchup.domain.InfoBoard;
import com.catchup.catchup.domain.User;
import com.catchup.catchup.dto.InfoBoardDTO;
import com.catchup.catchup.dto.SearchCondition;
import com.catchup.catchup.repository.InfoBoardRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InfoBoardServiceImpl implements InfoBoardService{

    private final InfoBoardRepository infoRepository;
    private final ModelMapper modelMapper;

    private final AWSService awsService;

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

    @Override
    public Page<InfoBoardDTO> getNoticeList(String search, String searchtxt, String kind, Pageable pageable) {
        SearchCondition condition = new SearchCondition();

        condition.setCate(null);
        condition.setTitle(null);
        condition.setContent(null);
        condition.setKind("n");

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

    @Override
    public Long insert(InfoBoardDTO dto) {
        InfoBoard info = modelMapper.map(dto, InfoBoard.class);
        User user = new User();
        user.setUid(dto.getUid());

        info.setUser(user);
        InfoBoard newInfo = infoRepository.save(info);

        System.out.println("service_test>>>> newInfo: "+newInfo);
        System.out.println("service_test>>>> newInfo: "+newInfo.getCate());
        System.out.println("service_test>>>> newInfo: "+newInfo.getUser().getUid());

        return newInfo.getIid();
    }

    @Override
    public InfoBoardDTO getDetail(Long iid) {
//        Optional<InfoBoard> info = infoRepository.findById(iid);
//
//        InfoBoardDTO dto = info.stream().map(
//                item->modelMapper.map(item, InfoBoardDTO.class)).findAny()
//                .orElseThrow(()->new RuntimeException()
//                );

        InfoBoardDTO dto = infoRepository.findInfoById(iid);
        System.out.println("uid 가져오는지 테스트: "+dto.getUid());
        System.out.println("uid 가져오는지 테스트: "+dto.getCate());
        return dto;
    }

    /*댓글 하나 가져오기*/
    @Override
    public List<String> getRep(Long iid) {

        List<String> rep = new ArrayList<>();

        Optional<InfoBoard> data = infoRepository.findById(iid);
        InfoBoardDTO infoDTO = data.stream().map(
                item -> modelMapper.map(item, InfoBoardDTO.class))
                .findAny().orElseThrow(
                        ()->new RuntimeException()
                );

        rep.add(infoDTO.getRepContent());
        System.out.println("rep_test: "+infoDTO.getRepContent());
        System.out.println("rep_test: "+infoDTO.getTitle());
        return rep;
    }

    @Override
    public InfoBoardDTO updateRep(InfoBoardDTO infoBoardDTO) {
        InfoBoard infoBoard = infoRepository.findById(infoBoardDTO.getIid())
                .orElseThrow(() -> new RuntimeException());
        infoBoard.setRepContent(infoBoardDTO.getRepContent());
        InfoBoard updatedInfoBoard = infoRepository.save(infoBoard);
        return modelMapper.map(updatedInfoBoard, InfoBoardDTO.class);
    }

    @Override
    public Long delQna(Long iid) {

        //DB에 저장된 infoboard의 content값 string으로 가져옴
        InfoBoard info = infoRepository.findById(iid).orElseThrow(()-> new RuntimeException());
        String content = info.getContent();

        // HTML 콘텐츠에서 이미지 URL을 추출
        Document doc = Jsoup.parse(content);
        Elements imgs = doc.select("img");

        for (Element img : imgs) {
            String imageUrl = img.attr("src");

            // S3에서 파일을 삭제
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // imageUrl에서 버킷 내 경로를 추출합니다.
                String key = extractKeyFromUrl(imageUrl, info.getKind());
                awsService.deleteFile(key);
            }
        }

        //S3에서 데이터 삭제 후, DB에서 데이터 삭제
        infoRepository.deleteById(iid);
        return iid;
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

        if (boardKind.equals("q")){
            delete_file = "qna/"+imageUrl.substring(startIndex);
        } else if (boardKind.equals("n")) {
            delete_file = "notice/"+imageUrl.substring(startIndex);
        }

        //System.out.println("test0625 이미지url 경로:"+delete_file);

        return delete_file; //파싱한 이미지 이름값 리턴
    }

    @Override
    @Transactional
    public Long updateQna(InfoBoardDTO dto) {
        Optional<InfoBoard> getEntityById = infoRepository.findById(dto.getIid());

        InfoBoard infoBoard = getEntityById.orElseThrow(()->{
            throw new RuntimeException("+++NO DATA+++");
        });

        infoBoard.setCate(dto.getCate());
        infoBoard.setTitle(dto.getTitle());
        infoBoard.setContent(dto.getContent());

        return dto.getIid();
    }

}
