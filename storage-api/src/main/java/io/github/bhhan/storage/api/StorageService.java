package io.github.bhhan.storage.api;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    String store(MultipartFile file);
    List<String> storeAll(List<MultipartFile> files);
    void delete(String fileName);
    void delete(List<String> fileNames);
}
