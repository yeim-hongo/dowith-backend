package imhong.dowith.common;

import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    private final Logger log = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handleBaseException(HttpServletRequest request,
        BaseException e) {
        log.warn("잘못된 요청이 들어왔습니다. URI: {}, 코드: {}, 내용:  {}", request.getRequestURI(), e.getCode(),
            e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(
            new ExceptionResponse(
                e.getCode(),
                e.getMessage()
            )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(
        HttpServletRequest request,
        MethodArgumentNotValidException e
    ) {
        var globalErrorMessage = e.getGlobalErrors().stream()
            .map(error -> error.getDefaultMessage())
            .collect(Collectors.joining(", ", "[Global Error : ", "], \t"));
        var fieldErrorMessage = e.getFieldErrors().stream()
            .map(error -> error.getField() + " : " + error.getDefaultMessage())
            .collect(Collectors.joining(" ", "[Field Error : ", "]"));
        var errorMessage = globalErrorMessage + fieldErrorMessage;
        log.warn("잘못된 요청이 들어왔습니다. URI: {},  내용:  {}", request.getRequestURI(), errorMessage);
        return ResponseEntity.badRequest().body(
            new ExceptionResponse(
                "G01",
                errorMessage
            )
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionResponse> handleMissingServletRequestParameterException(
        HttpServletRequest request,
        MissingServletRequestParameterException e
    ) {
        var errorMessage = e.getParameterName() + " 값이 누락되었습니다.";
        log.warn("잘못된 요청이 들어왔습니다. URI: {},  내용:  {}", request.getRequestURI(), errorMessage);
        return ResponseEntity.badRequest().body(
            new ExceptionResponse(
                "G02",
                errorMessage
            )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(HttpServletRequest request,
        Exception e) {
        log.error("예상하지 못한 예외가 발생했습니다. URI: {}, {}", request.getRequestURI(), e.getMessage(), e);
        return ResponseEntity.internalServerError().body(
            new ExceptionResponse(
                "G03",
                "서버가 응답할 수 없습니다."
            )
        );
    }
}
