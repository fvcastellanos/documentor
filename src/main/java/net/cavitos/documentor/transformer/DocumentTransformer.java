package net.cavitos.documentor.transformer;

import net.cavitos.documentor.domain.model.ImageDocument;
import net.cavitos.documentor.web.model.request.NewDocumentRequest;

public final class DocumentTransformer {
    
    public static ImageDocument toModel(NewDocumentRequest request) {

        var document = new ImageDocument();
        document.setName(request.getName());
        document.setDescription(request.getDescription());
        document.setTenantId(request.getTenantId());
        document.setTags(request.getTags());

        return document;
    }
}
