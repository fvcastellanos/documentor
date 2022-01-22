package net.cavitos.documentor.repository;

import org.springframework.data.repository.CrudRepository;

import net.cavitos.documentor.domain.model.Upload;

import java.util.List;

public interface UploadRepository extends CrudRepository<Upload, String> {

    List<Upload> findByStored(boolean stored);
}
