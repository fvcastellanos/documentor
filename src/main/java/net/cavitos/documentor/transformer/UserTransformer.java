package net.cavitos.documentor.transformer;

import net.cavitos.documentor.domain.model.UserDocument;
import net.cavitos.documentor.domain.model.status.ActiveStatus;
import net.cavitos.documentor.domain.web.User;
import net.cavitos.documentor.web.controller.TenantController;
import org.apache.commons.lang3.StringUtils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public final class UserTransformer {

    public static UserDocument toModel(final String tenantId, final User user) {

        final var userDocument = new UserDocument();
        userDocument.setUserId(user.getUserId());
        userDocument.setProvider(user.getProvider());
        userDocument.setTenantId(tenantId);
        userDocument.setStatus(ActiveStatus.ACTIVE.getValue());

        if (StringUtils.isNotBlank(user.getStatus())) {

            final var status = ActiveStatus.of(user.getStatus())
                    .getValue();

            userDocument.setStatus(status);
        }

        return userDocument;
    }

    public static User toWeb(final String tenantId, final UserDocument userDocument) {

        final var selfLink = linkTo(methodOn(TenantController.class)
                .getUserById(tenantId, userDocument.getUserId()))
                .withSelfRel();

        final var status = ActiveStatus.of(userDocument.getStatus())
                .getValue();

        final var user = new User();
        user.setUserId(userDocument.getUserId());
        user.setTenant(userDocument.getTenantId());
        user.setProvider(userDocument.getProvider());
        user.setStatus(status);
        user.add(selfLink);

        return user;
    }

}
