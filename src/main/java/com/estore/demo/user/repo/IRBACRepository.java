package com.estore.demo.user.repo;

import com.estore.demo.user.domain.RoleBasedCapabilityAccess;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
Spring JPA repository to query RoleBasedCapabilityAccess collection
 */
@Repository
public interface IRBACRepository extends PagingAndSortingRepository<RoleBasedCapabilityAccess, Long> {
    List<RoleBasedCapabilityAccess> findByRole(String role);
}
