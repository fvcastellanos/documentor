package net.cavitos.documentor.repository;

import net.cavitos.documentor.domain.model.UserDocument;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<UserDocument, String> {

    Optional<UserDocument> findByUserIdAndProvider(String userId, String provider);
}
