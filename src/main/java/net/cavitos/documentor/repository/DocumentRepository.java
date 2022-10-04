package net.cavitos.documentor.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import net.cavitos.documentor.domain.model.ImageDocument;

public interface DocumentRepository extends PagingAndSortingRepository<ImageDocument, String> {
    
    Page<ImageDocument> findByTenantId(String tenantId, Pageable pageable);

    @Query(value = "{ 'tenantId': ?0, $text: { $search: ?1 } }, { score: { $meta: 'textScore' } }", 
        sort = "{ 'score': 1 }")
    Page<ImageDocument> findByTenantIdAndText(String tenantId, String text, Pageable pageable);

    Optional<ImageDocument> findByTenantIdAndName(String tenantId, String name);
}
