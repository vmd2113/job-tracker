package com.duongw.common.exception;


import com.duongw.common.model.dto.response.ErrorResponse;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


import java.util.Date;


import static org.springframework.http.HttpStatus.*;


@RestControllerAdvice
@Slf4j
@Hidden
public class GlobalExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({InvalidDataException.class, ConstraintViolationException.class,
            MissingServletRequestParameterException.class, MethodArgumentNotValidException.class})


    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Handle exception when the data invalid. (@RequestBody, @RequestParam, @PathVariable)",
                                    summary = "Handle Bad Request",
                                    value = """
                                            {
                                                 "timestamp": "2024-04-07T11:38:56.368+00:00",
                                                 "status": 400,
                                                 "path": "/api/v1/...",
                                                 "error": "Invalid Payload",
                                                 "message": "{data} must be not blank"
                                             }
                                            """
                            ))})
    })
    public ErrorResponse handleValidationException(Exception ex, WebRequest request) {

        String path = request.getDescription(false).replace("uri=", "");


        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(new Date());
        response.setStatus(BAD_REQUEST.value());
        response.setMessage(ex.getMessage());
        response.setPath(path);


        String message = ex.getMessage();
        if (ex instanceof MethodArgumentNotValidException) {
            int start = message.lastIndexOf("[") + 1;
            int end = message.lastIndexOf("]") - 1;
            message = message.substring(start, end);
            response.setError("Invalid Payload");
            response.setMessage(message);
        } else if (ex instanceof MissingServletRequestParameterException) {
            response.setError("Invalid Parameter");
            response.setMessage(message);
        } else if (ex instanceof ConstraintViolationException) {
            response.setError("Invalid Parameter");
            response.setMessage(message.substring(message.indexOf(" ") + 1));
        } else {
            response.setError("Invalid Data");
            response.setMessage(message);
        }
        log.error("Error", ex);
        return response;

    }


    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setPath(path);
        error.setStatus(NOT_FOUND.value());
        error.setError(NOT_FOUND.getReasonPhrase());
        error.setMessage(ex.getMessage());
        log.error("Error", ex);
        return error;
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleNullPointerException(NullPointerException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setPath(path);
        error.setStatus(INTERNAL_SERVER_ERROR.value());
        error.setError("Unexpected Error");
        error.setMessage("An unexpected error occurred. Please contact support.");
        log.error("NullPointerException: ", ex);
        return error;
    }



    @ExceptionHandler({DuplicateKeyException.class, AlreadyExistedException.class})
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleDuplicateKeyException(DuplicateKeyException ex, WebRequest request) {

        String path = request.getDescription(false).replace("uri=", "");

        // Kiểm tra nếu đường dẫn là của Swagger, thì bỏ qua việc xử lý lỗi

        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setPath(request.getDescription(false).replace("uri=", ""));
        error.setStatus(CONFLICT.value());
        error.setError(CONFLICT.getReasonPhrase());
        error.setMessage(ex.getMessage());
        log.error("Error", ex);
        return error;
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)


    public ErrorResponse handleException(Exception ex, WebRequest request, HttpServletResponse response) {

        String path = request.getDescription(false).replace("uri=", "");


        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setPath(path);
        error.setStatus(INTERNAL_SERVER_ERROR.value());
        error.setError(INTERNAL_SERVER_ERROR.getReasonPhrase());
        error.setMessage(ex.getMessage());
        response.setStatus(INTERNAL_SERVER_ERROR.value());
        log.error("Error", ex);
        return error;


    }


//    @ExceptionHandler(AuthenticationException.class)
//    @ResponseStatus(UNAUTHORIZED)
//    public ErrorResponse handleAuthenticationException(AuthenticationException ex, WebRequest request) {
//        ErrorResponse error = new ErrorResponse();
//        error.setTimestamp(new Date());
//        error.setPath(request.getDescription(false).replace("uri=", ""));
//        error.setStatus(UNAUTHORIZED.value());
//        error.setError(UNAUTHORIZED.getReasonPhrase());
//        error.setMessage("Authentication failed. Please check your credentials");
//        log.error("Authentication Error: ", ex);
//        return error;
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    @ResponseStatus(FORBIDDEN)
//    public ErrorResponse handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
//        ErrorResponse error = new ErrorResponse();
//        error.setTimestamp(new Date());
//        error.setPath(request.getDescription(false).replace("uri=", ""));
//        error.setStatus(FORBIDDEN.value());
//        error.setError(FORBIDDEN.getReasonPhrase());
//        error.setMessage("You do not have permission to access this resource");
//        log.error("Access Denied Error: ", ex);
//        return error;
//    }


}
