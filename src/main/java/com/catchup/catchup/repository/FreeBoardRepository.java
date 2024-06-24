package com.catchup.catchup.repository;

import com.catchup.catchup.domain.FreeBoard;
import com.catchup.catchup.dto.FreeBoardDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.JoinType.JOIN;

@Repository
public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>
        ,FreeBoardQDSLRepository
{

    @Query(" SELECT r.frcontent, u.nickname, u.profile, r.frCreateDate, r.frUpdateDate " +
           " FROM FreeRepBoard r INNER JOIN User u " +
           " ON r.user.uid = u.uid INNER JOIN FreeBoard f " +
           " ON  u.uid = f.user.uid " +
           " WHERE r.fBoard.fid = :fid")
    List<Object[]> repList(Long fid);
}
