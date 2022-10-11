package net.cavitos.documentor.service;

import net.cavitos.documentor.builder.BusinessExceptionBuilder;
import net.cavitos.documentor.domain.model.TenantDocument;
import net.cavitos.documentor.domain.model.status.ActiveStatus;
import net.cavitos.documentor.domain.web.Tenant;
import net.cavitos.documentor.repository.TenantRepository;
import net.cavitos.documentor.transformer.TenantTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class TenantService {
    
    private final TenantRepository tenantRepository;

    @Autowired
    public TenantService(final TenantRepository tenantRepository) {

        this.tenantRepository = tenantRepository;
    }

    public Page<TenantDocument> getTenants(final int size, final int page) {

        final var pageable = PageRequest.of(page, size);
        return tenantRepository.findAll(pageable);
    }

    public TenantDocument getTenantById(final String id) {

        return tenantRepository.findById(id)
                .orElseThrow(() -> BusinessExceptionBuilder.notFoundException("Tenant: %s not found", id));
    }

    public TenantDocument newTenant(final Tenant tenant) {

        final var tenantId = tenant.getTenantId();
        var tenantHolder = tenantRepository.findByTenantId(tenantId);
        
        if(tenantHolder.isPresent()) {

            throw BusinessExceptionBuilder.unprocessableException("Tenant: %s already exists", tenantId);
        }

        final var email = tenant.getEmail();
        tenantHolder = tenantRepository.findByEmail(email);

        if (tenantHolder.isPresent()) {

            throw BusinessExceptionBuilder.unprocessableException("Email: %s already exists", email);
        }

        return tenantRepository.save(TenantTransformer.toModel(tenant));
    }

    public TenantDocument updateTenant(final String id, final Tenant tenant) {

        var tenantDocument = tenantRepository.findById(id)
                .orElseThrow(() -> BusinessExceptionBuilder.notFoundException("Tenant: %s not found", id));

        tenantRepository.findByEmail(tenant.getEmail())
                .ifPresent(existingTenant -> {

                    if (!existingTenant.getId().equalsIgnoreCase(tenantDocument.getId())) {

                        throw BusinessExceptionBuilder.unprocessableException("Email belongs to another tenant");
                    }
                });

        final var status = ActiveStatus.of(tenant.getStatus())
                        .getValue();

        tenantDocument.setName(tenant.getName());
        tenantDocument.setEmail(tenant.getEmail());
        tenantDocument.setStatus(status);

        return tenantRepository.save(tenantDocument);
    }

//    public void deleteTenant(final String tenantId) {
//
//        var tenantHolder = tenantRepository.findByTenantId(tenantId);
//
//        tenantHolder.ifPresent(tenant -> {
//
//            tenantRepository.delete(tenant);
//        });
//    }
}
