package net.cavitos.documentor.transformer;

import net.cavitos.documentor.domain.model.Upload;
import net.cavitos.documentor.domain.web.FileUpload;
import net.cavitos.documentor.web.controller.DocumentController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public final class FileUploadTransformer {

    public static FileUpload toWeb(final String documentId, final Upload upload) {

        final var selfLink = linkTo(methodOn(DocumentController.class)
                .getUploadById(documentId, upload.getId(), null))
                .withSelfRel();

        final var fileUpload = new FileUpload();
        fileUpload.setUploadName(upload.getUploadName());
        fileUpload.setUrl(upload.getUrl());
        fileUpload.setFileName(upload.getFileName());
        fileUpload.add(selfLink);

        return fileUpload;
    }
}
