package io.github.bhhan.storage.local.web;

import io.github.bhhan.storage.local.service.FileSystemStorageServiceImpl;
import io.github.bhhan.storage.local.web.exception.FileDownloadErrorException;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/")
@ResponseBody
public class FileDownloadApi {
    private static final String FILE_DOWNLOAD_HOST = "http://localhost:8080/files/%s";
    private final FileSystemStorageServiceImpl storageService;
    private final Tika tika = new Tika();

    public FileDownloadApi(FileSystemStorageServiceImpl storageService) {
        this.storageService = storageService;
    }

    public static String getFileDownloadUrl(String fileName){
        return String.format(FILE_DOWNLOAD_HOST, fileName);
    }

    @GetMapping("/files/{fileName}")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName){
        String mimeType;
        Resource resource = storageService.loadAsResource(fileName);

        try{
            mimeType = tika.detect(resource.getFile());
        }catch(Exception e){
            throw new FileDownloadErrorException("파일 다운로드에 실패했습니다.");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .body(resource);
    }
}
