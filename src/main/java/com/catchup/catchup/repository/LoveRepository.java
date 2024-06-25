package com.catchup.catchup.repository;

import com.catchup.catchup.domain.FreeBoard;
import com.catchup.catchup.domain.Love;
import com.catchup.catchup.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LoveRepository extends JpaRepository<Love, Long> {

    Optional<Object> findByUserAndFreeBoard(User uid, FreeBoard fid);

    @Query(" select l from Love l where l.user.uid=:uid and l.freeBoard.fid=:fid")
    Love findUserBoardId(Long uid, Long fid);
}
