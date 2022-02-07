package net.cavitos.documentor.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import net.cavitos.documentor.domain.model.Tenant;

public interface TenantRepository extends PagingAndSortingRepository<Tenant, String> {
    
    Page<Tenant> findAll(Pageable pageable);
    Page<Tenant> findByParentTenantId(@Param("tenantId") String parentTenantId, Pageable pageable);
    Optional<Tenant> findByTenantId(String tenantId);
    Optional<Tenant> findByEmail(String email);
}
