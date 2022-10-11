package net.cavitos.documentor.transformer;

import net.cavitos.documentor.domain.model.ImageDocument;
import net.cavitos.documentor.domain.web.Document;
import net.cavitos.documentor.web.controller.DocumentController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public final class DocumentTransformer {
    
    public static ImageDocument toModel(final String tenantId, final Document request) {

        var document = new ImageDocument();
        document.setName(request.getName());
        document.setDescription(request.getDescription());
        document.setTenantId(tenantId);
        document.setTags(request.getTags());

        return document;
    }

    public static Document toWeb(final ImageDocument imageDocument) {

        final var selfLink = linkTo(methodOn(DocumentController.class)
                .getById(imageDocument.getId(), null))
                .withSelfRel();

//        final var carBrandLines = linkTo(methodOn(CarBrandController.class)
//                .getById(carBrandEntity.getId(), null))
//                .slash("lines")
//                .withRel("carBrandLines");


        var document = new Document();
        document.setName(imageDocument.getName());
        document.setDescription(imageDocument.getDescription());
        document.setTags(imageDocument.getTags());
        document.add(selfLink);

        return document;
    }
}
