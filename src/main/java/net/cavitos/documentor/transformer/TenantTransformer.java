package net.cavitos.documentor.transformer;

import net.cavitos.documentor.domain.model.TenantDocument;
import net.cavitos.documentor.domain.model.status.ActiveStatus;
import net.cavitos.documentor.domain.web.Tenant;
import net.cavitos.documentor.web.controller.DocumentController;
import net.cavitos.documentor.web.controller.TenantController;
import org.apache.commons.lang3.StringUtils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public final class TenantTransformer {

    public static TenantDocument toModel(final Tenant tenant) {

        final var tenantDocument = new TenantDocument();
        tenantDocument.setTenantId(tenant.getTenantId());
        tenantDocument.setName(tenant.getName());
        tenantDocument.setEmail(tenant.getEmail());
        tenantDocument.setStatus(ActiveStatus.ACTIVE.getValue());

        if (!StringUtils.isBlank(tenant.getStatus())) {

            final var status = ActiveStatus.of(tenant.getStatus())
                    .getValue();

            tenantDocument.setStatus(status);
        }

        return tenantDocument;
    }

    public static Tenant toWeb(final TenantDocument tenantDocument) {

        final var selfLink = linkTo(methodOn(TenantController.class)
                .getTenantById(tenantDocument.getId()))
                .withSelfRel();

        final var status = ActiveStatus.of(tenantDocument.getStatus())
                .getValue();

        final var tenant = new Tenant();
        tenant.setTenantId(tenantDocument.getTenantId());
        tenant.setName(tenantDocument.getName());
        tenant.setEmail(tenantDocument.getEmail());
        tenant.setStatus(status);
        tenant.add(selfLink);

        return tenant;
    }
    
}
