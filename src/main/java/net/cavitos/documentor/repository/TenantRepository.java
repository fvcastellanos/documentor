package net.cavitos.documentor.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import net.cavitos.documentor.domain.model.TenantDocument;

public interface TenantRepository extends PagingAndSortingRepository<TenantDocument, String> {
    
    Page<TenantDocument> findAll(Pageable pageable);
    Page<TenantDocument> findByParentTenantId(@Param("tenantId") String parentTenantId, Pageable pageable);
    Optional<TenantDocument> findByTenantId(String tenantId);
    Optional<TenantDocument> findByEmail(String email);
}
