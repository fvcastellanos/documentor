package net.cavitos.documentor.security.service;

import net.cavitos.documentor.domain.exception.AuthenticationException;
import net.cavitos.documentor.domain.model.status.ActiveStatus;
import net.cavitos.documentor.repository.TenantRepository;
import net.cavitos.documentor.repository.UserRepository;
import net.cavitos.documentor.security.domain.UserProfile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoDbUserService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDbUserService.class);

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;

    public MongoDbUserService(final UserRepository userRepository,
                              final TenantRepository tenantRepository) {

        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
    }

    @Override
    public UserProfile getUserProfile(final String username) {

        final var userIdentities = StringUtils.split(username, '|');

        if (userIdentities.length < 2) {

            LOGGER.error("unable to get tenant information from principal");
            throw new AuthenticationException("Unable to get tenant information");
        }

        final var user = userRepository.findByUserIdAndProvider(userIdentities[1], userIdentities[0])
                .orElseThrow(() -> {
                    LOGGER.error("user={} not found", username);
                    throw new AuthenticationException("User not found");
                });

        final var tenant = tenantRepository.findByTenantId(user.getTenantId())
                .orElseThrow(() -> {

                    LOGGER.error("tenant: {} not found", user.getTenantId());
                    throw new AuthenticationException("Tenant not found");
                });

        final var userActive = ActiveStatus.of(user.getStatus());
        final var tenantActive = ActiveStatus.of(tenant.getStatus());

        if ((userActive == ActiveStatus.INACTIVE) || (tenantActive == ActiveStatus.INACTIVE)) {

            LOGGER.error("user or tenant is not active - user_active={}, tenant_active={}", userActive, tenantActive);
            throw new AuthenticationException("User or Tenant is not active");
        }

        LOGGER.info("Profile information loaded for user={}", username);

        final var userProfile = new UserProfile();
        userProfile.setUsername(username);
        userProfile.setUserId(userIdentities[1]);
        userProfile.setProvider(userIdentities[0]);
        userProfile.setTenant(tenant.getTenantId());

        return userProfile;
    }
}
