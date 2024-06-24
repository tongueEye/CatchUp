package com.catchup.catchup.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class MyPageQDSLRepositoryImpl  implements MyPageQDSLRepository{
   private final JPAQueryFactory queryFactory;
}
