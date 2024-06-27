package com.catchup.catchup.repository;

import com.catchup.catchup.domain.FreeRepBoard;
import com.catchup.catchup.dto.LoveDTO;
import com.catchup.catchup.dto.RepBoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FreeRepBoardRepository extends JpaRepository<FreeRepBoard, Long>, FreeRepBoardQDSLRepository {
}
