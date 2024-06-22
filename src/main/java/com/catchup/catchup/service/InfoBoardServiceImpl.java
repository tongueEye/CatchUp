package com.catchup.catchup.service;

import com.catchup.catchup.domain.InfoBoard;
import com.catchup.catchup.domain.User;
import com.catchup.catchup.dto.InfoBoardDTO;
import com.catchup.catchup.dto.SearchCondition;
import com.catchup.catchup.repository.InfoBoardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InfoBoardServiceImpl implements InfoBoardService{

    private final InfoBoardRepository infoRepository;
    private final ModelMapper modelMapper;

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

}
