package net.cavitos.documentor.transformer;

import net.cavitos.documentor.domain.model.Tenant;
import net.cavitos.documentor.web.model.request.NewTenantRequest;

public final class TenantTransformer {

    public static Tenant toModel(final NewTenantRequest newTenantRequest) {

        var tenant = new Tenant();
        tenant.setTenantId(newTenantRequest.getTenantId());
        tenant.setParentTenantId(newTenantRequest.getParentTenantId());
        tenant.setName(newTenantRequest.getName());
        tenant.setEmail(newTenantRequest.getEmail());

        return tenant;
    }
    
}
