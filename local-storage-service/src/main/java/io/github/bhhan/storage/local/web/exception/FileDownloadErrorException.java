package io.github.bhhan.storage.local.web.exception;

public class FileDownloadErrorException extends RuntimeException{
    public FileDownloadErrorException(String message) {
        super(message);
    }

    public FileDownloadErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
