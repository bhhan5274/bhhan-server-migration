package io.github.bhhan.storage.local.service;

import io.github.bhhan.storage.api.StorageService;
import io.github.bhhan.storage.api.exception.StorageException;
import io.github.bhhan.storage.local.web.FileDownloadApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class FileSystemStorageServiceImpl implements StorageService {
    private final Path rootLocation;

    public FileSystemStorageServiceImpl() {
        this.rootLocation = Paths.get("upload-dir");
    }

    @PostConstruct
    public void init(){
        try{
            Files.createDirectories(rootLocation);
        }catch(IOException e){
            throw new StorageException("Could not initialize directory");
        }
    }

    public Resource loadAsResource(String fileName){
        try{
            Path file = rootLocation.resolve(fileName);
            UrlResource resource = new UrlResource(file.toUri());

            if(resource.exists() && resource.isReadable()){
                return resource;
            }else {
                throw new StorageException("Could not read file: " + fileName);
            }
        }catch(Exception e){
            throw new StorageException("Could not read file: " + fileName, e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String fileName = "";

        try{
            if(Objects.isNull(file)){
                throw new StorageException("failed to store file");
            }

            fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

            if(file.isEmpty()){
                throw new StorageException("failed to store empty file " + fileName);
            }

            if(fileName.contains("..")){
                throw new StorageException("Cannot store file with relative path outside current directory " + fileName);
            }

            Path path = this.rootLocation.resolve(fileName);

            try(InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path,
                        StandardCopyOption.REPLACE_EXISTING);
            }

            return FileDownloadApi.getFileDownloadUrl(path.getFileName().toString());

        }catch(IOException e){
            throw new StorageException("Failed to store file " + fileName, e);
        }
    }

    @Override
    public List<String> storeAll(List<MultipartFile> files) {
        List<String> storeFileList = new ArrayList<>();

        for (MultipartFile file : files) {
            storeFileList.add(store(file));
        }

        return storeFileList;
    }

    @Override
    public void delete(String fileName) {
        FileSystemUtils.deleteRecursively(rootLocation.resolve(fileName).toFile());
    }

    @Override
    public void delete(List<String> fileNames) {
        for (String fileName : fileNames) {
            delete(fileName);
        }
    }
}
