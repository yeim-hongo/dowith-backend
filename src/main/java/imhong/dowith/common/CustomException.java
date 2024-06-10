package imhong.dowith.common;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private final BaseExceptionType baseExceptionType;

    public CustomException(BaseExceptionType baseExceptionType) {
        super(baseExceptionType.getMessage());
        this.baseExceptionType = baseExceptionType;
    }

    public BaseExceptionType getExceptionType() {
        return baseExceptionType;
    }

    public HttpStatus getHttpStatus() {
        return baseExceptionType.getHttpStatus();
    }

    public String getCode() {
        return baseExceptionType.getCode();
    }

    public String getMessage() {
        return baseExceptionType.getMessage();
    }
}
