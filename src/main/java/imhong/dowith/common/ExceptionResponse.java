package imhong.dowith.common;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExceptionResponse {

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
