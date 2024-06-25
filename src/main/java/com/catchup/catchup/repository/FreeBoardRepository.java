package com.catchup.catchup.repository;

import com.catchup.catchup.domain.FreeBoard;
import com.catchup.catchup.dto.FreeBoardDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.JoinType.JOIN;

@Repository
public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>
        , FreeBoardQDSLRepository {

    @Query("SELECT u.nickname, u.profile, r.frcontent, r.frCreateDate, r.frUpdateDate, r.frid " +
            " FROM FreeRepBoard r INNER JOIN User u " +
            " ON r.user.uid = u.uid INNER JOIN FreeBoard f " +
            " ON  u.uid = f.user.uid AND f.fid = r.fBoard.fid" +
            " WHERE r.fBoard.fid = :fid")
    List<Object[]> repList(Long fid);

    @Override
    Optional<FreeBoard> findById(Long aLong);
}
