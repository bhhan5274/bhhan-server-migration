package io.github.bhhan.server.web.api.common.error;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private String message;
    private int status;
    private String code;
    private List<FieldError> errors = new ArrayList<>();

    @Builder
    public ErrorResponse(String message, int status, String code){
        this.message = message;
        this.status = status;
        this.code = code;
    }

    public ErrorResponse setMessage(String message){
        this.message = message;
        return this;
    }

    public ErrorResponse(ErrorCode errorCode){
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
    }

    public ErrorResponse(ErrorCode errorCode, String message){
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.message = message;
    }

    public ErrorResponse(ErrorCode errorCode, List<org.springframework.validation.FieldError> fieldErrors){
        this(errorCode);
        this.errors.addAll(fieldErrors.stream()
                .map(FieldError::new)
                .collect(Collectors.toList()));
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError{
        private String field;
        private String value;
        private String reason;

        @Builder
        public FieldError(org.springframework.validation.FieldError fieldError){
            this.field = fieldError.getField();

            final Object rejectedValue = fieldError.getRejectedValue();
            this.value = Objects.isNull(rejectedValue) ? "null" : rejectedValue.toString();

            this.reason = fieldError.getDefaultMessage();
        }
    }
}
