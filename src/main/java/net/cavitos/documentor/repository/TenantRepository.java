package net.cavitos.documentor.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import net.cavitos.documentor.domain.Tenant;

@RepositoryRestResource(collectionResourceRel = "tenants", path = "tenants")
public interface TenantRepository extends PagingAndSortingRepository<Tenant, String> {
    
    Page<Tenant> findAll(Pageable pageable);
    Page<Tenant> findByParentTenantId(@Param("tenantId") String parentTenantId, Pageable pageable);
}
