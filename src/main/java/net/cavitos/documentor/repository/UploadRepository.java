package net.cavitos.documentor.repository;

import net.cavitos.documentor.domain.Upload;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UploadRepository extends CrudRepository<Upload, String> {

    List<Upload> findByStored(boolean stored);
}
