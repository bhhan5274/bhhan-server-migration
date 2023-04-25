package io.github.bhhan.storage.local;

import io.github.bhhan.storage.local.service.FileSystemStorageServiceImpl;
import io.github.bhhan.storage.local.web.FileDownloadApi;
import org.springframework.context.annotation.Bean;

public class FileSystemStorageConfiguration {
    @Bean
    public FileSystemStorageServiceImpl storageService(){
        return new FileSystemStorageServiceImpl();
    }

    @Bean
    public FileDownloadApi fileDownloadApi(FileSystemStorageServiceImpl storageService){
        return new FileDownloadApi(storageService);
    }
}
