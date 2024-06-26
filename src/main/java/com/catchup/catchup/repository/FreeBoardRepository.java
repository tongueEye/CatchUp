package com.catchup.catchup.repository;

import com.catchup.catchup.domain.FreeBoard;
import com.catchup.catchup.dto.FreeBoardDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.JoinType.JOIN;

@Repository
public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>
        , FreeBoardQDSLRepository {

    @Query(" SELECT u.nickname, r.frcontent, r.frid, u.uid " +
           " FROM FreeRepBoard r INNER JOIN User u " +
           " ON r.user.uid = u.uid" +
           " WHERE r.freeBoard.fid=:fid")
    List<Object[]> repList(Long fid);

    @Override
    Optional<FreeBoard> findById(Long aLong);

}
