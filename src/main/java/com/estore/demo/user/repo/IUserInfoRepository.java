package com.estore.demo.user.repo;

import com.estore.demo.common.domain.UserInfo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/*
Spring JPA repository to query UserInfo collection
 */
@Repository
public interface IUserInfoRepository extends PagingAndSortingRepository<UserInfo, String> {
    UserInfo findByUsername(String username);
}
