package com.catchup.catchup.repository;

import com.catchup.catchup.domain.InfoBoard;
import com.catchup.catchup.dto.InfoBoardDTO;
import com.catchup.catchup.dto.SearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoBoardRepository extends JpaRepository<InfoBoard, Long> , InfoBoardQDSLRepository{


}
