package net.cavitos.documentor.transformer;

import net.cavitos.documentor.domain.model.ImageDocument;
import net.cavitos.documentor.web.model.request.DocumentRequest;

public final class DocumentTransformer {
    
    public static ImageDocument toModel(final String tenantId, final DocumentRequest request) {

        var document = new ImageDocument();
        document.setName(request.getName());
        document.setDescription(request.getDescription());
        document.setTenantId(tenantId);
        document.setTags(request.getTags());

        return document;
    }
}
