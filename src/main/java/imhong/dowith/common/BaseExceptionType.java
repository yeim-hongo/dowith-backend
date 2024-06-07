package imhong.dowith.common;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {

    HttpStatus getHttpStatus();

    String getCode();

    String getMessage();
}
