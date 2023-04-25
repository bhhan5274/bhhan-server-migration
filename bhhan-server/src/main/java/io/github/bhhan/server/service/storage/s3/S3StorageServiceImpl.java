package io.github.bhhan.server.service.storage.s3;

import io.github.bhhan.storage.api.exception.StorageException;
import io.github.bhhan.storage.api.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class S3StorageServiceImpl implements StorageService {
    @Value("${cloud.aws.cloudFront.url}")
    private String cloudFrontUrl;
    private final S3Uploader s3Uploader;
    private static final String DEFAULT_DIR = "bhhan";

    public S3StorageServiceImpl(S3Uploader s3Uploader) {
        this.s3Uploader = s3Uploader;
    }

    @Override
    public String store(MultipartFile file) {
        try{
            return String.format("%s/%s", cloudFrontUrl, s3Uploader.upload(file, DEFAULT_DIR));
        }catch (Exception ex){
            throw new StorageException("S3 store file error!!!");
        }
    }

    @Override
    public List<String> storeAll(List<MultipartFile> files) {
        try{
            List<String> storeFiles = new ArrayList<>();

            for (MultipartFile file : files) {
                storeFiles.add(store(file));
            }

            return storeFiles;
        }catch (Exception ex){
            throw new StorageException("S3 storeAll file error!!!");
        }
    }

    @Override
    public void delete(String fileName) {
        s3Uploader.delete(fileName);
    }

    @Override
    public void delete(List<String> fileNames) {
        for (String fileName : fileNames) {
            s3Uploader.delete(fileName);
        }
    }
}
