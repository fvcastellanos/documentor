package net.cavitos.documentor.repository;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import net.cavitos.documentor.domain.ImageDocument;

@RepositoryRestResource(collectionResourceRel = "documents", path = "documents")
public interface DocumentRepository extends PagingAndSortingRepository<ImageDocument, String> {
    
    Page<ImageDocument> findAll();
}
