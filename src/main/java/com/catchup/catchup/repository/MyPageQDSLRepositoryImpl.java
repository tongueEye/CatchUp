package com.catchup.catchup.repository;


import com.catchup.catchup.dto.UserDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.catchup.catchup.domain.QUser.user;

@RequiredArgsConstructor
@Slf4j
public class MyPageQDSLRepositoryImpl  implements MyPageQDSLRepository{
   private final JPAQueryFactory queryFactory;

   @Override
   public void schoolInsert(UserDTO dto, Long uid) {
     //

   }
}
