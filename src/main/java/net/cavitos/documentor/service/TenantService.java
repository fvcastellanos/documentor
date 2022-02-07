package net.cavitos.documentor.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import net.cavitos.documentor.builder.BusinessExceptionBuilder;
import net.cavitos.documentor.domain.model.Tenant;
import net.cavitos.documentor.repository.TenantRepository;

@Service
public class TenantService {
    
    private final TenantRepository tenantRepository;

    public TenantService(final TenantRepository tenantRepository) {

        this.tenantRepository = tenantRepository;
    }

    public Page<Tenant> getTenants(final int size, final int page) {

        final var pageable = PageRequest.of(page, size);
        return tenantRepository.findAll(pageable);
    }

    public Tenant newTenant(final Tenant tenant) {

        final var tenantId = tenant.getTenantId();
        final var email = tenant.getEmail();
        
        var tenantHolder = tenantRepository.findByTenantId(tenantId);
        
        if(tenantHolder.isPresent()) {

            throw BusinessExceptionBuilder.unprocessableException("Tenant: %s already exists", tenantId);
        }

        tenantHolder = tenantRepository.findByEmail(email);

        if (tenantHolder.isPresent()) {

            throw BusinessExceptionBuilder.unprocessableException("Email: %s already exists", email);
        }

        return tenantRepository.save(tenant);
    }

    public Tenant updateTenant(final Tenant tenant) {

        final var tenantId = tenant.getTenantId();
        var tenantHolder = tenantRepository.findByTenantId(tenantId);
        
        if(tenantHolder.isEmpty()) {

            throw BusinessExceptionBuilder.notFoundException("Tenant: %s not found", tenantId);
        }

        var storedTenant = tenantHolder.get();

        // Evaluate the logic to update email
        storedTenant.setName(tenant.getName());

        return tenantRepository.save(storedTenant);
    }
}
