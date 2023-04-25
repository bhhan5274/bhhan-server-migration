package io.github.bhhan.server.service.storage.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class S3Uploader {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;

    public String upload(MultipartFile multipartFile, String dirName) {
        final String contentType = multipartFile.getContentType();
        final File uploadFile = convert(multipartFile)
                .orElseThrow(IllegalArgumentException::new);

        return upload(uploadFile, dirName, contentType);
    }

    public void delete(String key) {
        amazonS3Client.deleteObject(bucket, key);
    }

    private String upload(File uploadFile, String dirName, String contentType) {
        final String fileName = makeFileName(uploadFile, dirName);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);

        String uploadImageUrl = putS3(uploadFile, fileName, metadata);

        removeNewFile(uploadFile);

        return uploadImageUrl;
    }

    private String makeFileName(File uploadFile, String dirName) {
        String fileName = uploadFile.getName().split("_")[0];
        return dirName + "/" + fileName + "_" + LocalDateTime.now().atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli();
    }

    private String putS3(File uploadFile, String fileName, ObjectMetadata objectMetadata) {
        final PutObjectRequest request = new PutObjectRequest(bucket, fileName, uploadFile);
        request.setMetadata(objectMetadata);
        amazonS3Client.putObject(request.withCannedAcl(CannedAccessControlList.PublicRead));
        return fileName;
    }

    private void removeNewFile(File targetFile) {
        if (!targetFile.delete()) {
            log.info("파일 삭제에 실패했습니다");
        }
    }

    private Optional<File> convert(MultipartFile file) {
        final File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if (convertFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(file.getBytes());
                }
                return Optional.of(convertFile);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("파일 변환에 실패했습니다.");
        }

        return Optional.empty();
    }
}
