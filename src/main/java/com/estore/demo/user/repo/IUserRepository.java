package com.estore.demo.user.repo;

import com.estore.demo.user.domain.Seller;
import com.estore.demo.user.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/*
Spring JPA repository to query User collection
 */
@Repository
public interface IUserRepository extends PagingAndSortingRepository<User, String> {
    Seller findById(String userId);
}
