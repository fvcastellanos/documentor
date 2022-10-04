package net.cavitos.documentor.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import net.cavitos.documentor.builder.BusinessExceptionBuilder;
import net.cavitos.documentor.domain.model.Tenant;
import net.cavitos.documentor.repository.TenantRepository;
import net.cavitos.documentor.transformer.TenantTransformer;
import net.cavitos.documentor.web.model.request.NewTenantRequest;
import net.cavitos.documentor.web.model.request.UpdateTenantRequest;

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

    public Tenant getTenantById(final String tenantId) {

        var tenantHolder = tenantRepository.findByTenantId(tenantId);

        if(tenantHolder.isEmpty()) {

            throw BusinessExceptionBuilder.notFoundException("Tenant: %s not found", tenantId);
        }

        return tenantHolder.get();
    }

    public Tenant newTenant(final NewTenantRequest tenantRequest) {

        final var tenantId = tenantRequest.getTenantId();
        final var email = tenantRequest.getEmail();
        
        var tenantHolder = tenantRepository.findByTenantId(tenantId);
        
        if(tenantHolder.isPresent()) {

            throw BusinessExceptionBuilder.unprocessableException("Tenant: %s already exists", tenantId);
        }

        tenantHolder = tenantRepository.findByEmail(email);

        if (tenantHolder.isPresent()) {

            throw BusinessExceptionBuilder.unprocessableException("Email: %s already exists", email);
        }

        return tenantRepository.save(TenantTransformer.toModel(tenantRequest));
    }

    public Tenant updateTenant(final String tenantId, final UpdateTenantRequest updateTenantRequest) {

        var tenantHolder = tenantRepository.findByTenantId(tenantId);
        
        if(tenantHolder.isEmpty()) {

            throw BusinessExceptionBuilder.notFoundException("Tenant: %s not found", tenantId);
        }

        var storedTenant = tenantHolder.get();

        // Evaluate the logic to update email
        storedTenant.setName(updateTenantRequest.getName());

        return tenantRepository.save(storedTenant);
    }

    public void deleteTenant(final String tenantId) {

        var tenantHolder = tenantRepository.findByTenantId(tenantId);

        tenantHolder.ifPresent(tenant -> {

            tenantRepository.delete(tenant);
        });
    }
}
