package imhong.dowith.common;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {

    private BaseExceptionType baseExceptionType;

    public BaseException(BaseExceptionType baseExceptionType) {
        super(baseExceptionType.getMessage());
        this.baseExceptionType = baseExceptionType;
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
