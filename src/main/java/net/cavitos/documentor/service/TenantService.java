package net.cavitos.documentor.service;

import net.cavitos.documentor.builder.BusinessExceptionBuilder;
import net.cavitos.documentor.domain.model.TenantDocument;
import net.cavitos.documentor.domain.model.UserDocument;
import net.cavitos.documentor.domain.model.status.ActiveStatus;
import net.cavitos.documentor.domain.web.Tenant;
import net.cavitos.documentor.domain.web.UpdateUser;
import net.cavitos.documentor.domain.web.User;
import net.cavitos.documentor.repository.TenantRepository;
import net.cavitos.documentor.repository.UserRepository;
import net.cavitos.documentor.transformer.TenantTransformer;
import net.cavitos.documentor.transformer.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TenantService {
    
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    @Autowired
    public TenantService(final TenantRepository tenantRepository,
                         final UserRepository userRepository) {

        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
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

    // ------------------------------------------------------------------------------------------------------

    public Page<UserDocument> findUsersByTenant(final String tenantId, final int size, final int page) {

        final var tenantDocument = tenantRepository.findById(tenantId)
                .orElseThrow(() -> BusinessExceptionBuilder.notFoundException("Tenant not found"));

        final var pageable = PageRequest.of(page, size);
        return userRepository.findUsersByTenantId(tenantDocument.getTenantId(), pageable);
    }

    public UserDocument getUserById(final String tenantId, final String userId) {

        final var tenantDocument = tenantRepository.findById(tenantId)
                .orElseThrow(() -> BusinessExceptionBuilder.notFoundException("Tenant not found"));

        return userRepository.findByUserIdAndTenantId(userId, tenantDocument.getTenantId())
                .orElseThrow(() -> BusinessExceptionBuilder.notFoundException("User not found"));
    }

    public UserDocument addUser(final String tenantId, final User user) {

        final var tenantDocument = tenantRepository.findById(tenantId)
                .orElseThrow(() -> BusinessExceptionBuilder.notFoundException("Tenant not found"));

        final var existingUserHolder = userRepository.findByUserId(user.getUserId());

        if (existingUserHolder.isPresent()) {

            throw BusinessExceptionBuilder.unprocessableException("User already exists");
        }

        return userRepository.save(UserTransformer.toModel(tenantDocument.getTenantId(), user));
    }

    public UserDocument updateUser(final String tenantId, final String userId, UpdateUser updateUser) {

        tenantRepository.findById(tenantId)
                .orElseThrow(() -> BusinessExceptionBuilder.notFoundException("Tenant not found"));

        final var userDocument = userRepository.findByUserId(userId)
                .orElseThrow(() -> BusinessExceptionBuilder.notFoundException("User not found"));

        userDocument.setProvider(updateUser.getProvider());
        userDocument.setStatus(updateUser.getStatus());
        userDocument.setUpdated(Instant.now());

        return userRepository.save(userDocument);
    }

    public void deleteUser(final String tenantId, final String userId) {

        tenantRepository.findById(tenantId)
                .orElseThrow(() -> BusinessExceptionBuilder.notFoundException("Tenant not found"));

        userRepository.findByUserId(userId)
                .ifPresent(userRepository::delete);
    }
}
