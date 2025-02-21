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
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLTimeoutException;
import java.util.Date;


import static org.springframework.http.HttpStatus.*;


@RestControllerAdvice
@Slf4j
@Hidden
public class GlobalExceptionHandler {

    private static final String URI_PREFIX = "uri=";

    // Utility method to extract path
    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace(URI_PREFIX, "");
    }

    // Utility method to create base error response
    private ErrorResponse createBaseErrorResponse(WebRequest request, HttpStatus status) {
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setPath(extractPath(request));
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        return error;
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({InvalidDataException.class,
            ConstraintViolationException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentNotValidException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            MissingPathVariableException.class
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Handle invalid data exceptions",
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
                            ))
                    })
    })
    public ErrorResponse handleValidationExceptions(Exception ex, WebRequest request) {
        ErrorResponse response = createBaseErrorResponse(request, BAD_REQUEST);
        if (ex instanceof MethodArgumentNotValidException methodEx) {
            response.setError("Invalid Payload");
            response.setMessage(extractMethodArgumentErrorMessage(methodEx));
        } else if (ex instanceof MissingServletRequestParameterException) {
            response.setError("Invalid Parameter");
            response.setMessage(ex.getMessage());
        } else if (ex instanceof ConstraintViolationException constraintEx) {
            response.setError("Invalid Parameter");
            response.setMessage(extractConstraintViolationMessage(constraintEx));
        } else if (ex instanceof TypeMismatchException typeEx) {
            response.setError("Invalid Type");
            response.setMessage("Type mismatch for parameter: " + typeEx.getPropertyName());
        } else if (ex instanceof HttpMessageNotReadableException) {
            response.setError("Malformed Request");
            response.setMessage("The request body is invalid or malformed");
        } else {
            response.setError("Invalid Data");
            response.setMessage(ex.getMessage());
        }

        log.error("Validation error", ex);
        return response;
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class, FileNotFoundException.class})
    public ErrorResponse handleNotFoundException(Exception ex, WebRequest request) {
        ErrorResponse error = createBaseErrorResponse(request, NOT_FOUND);
        error.setError("Resource Not Found");
        error.setMessage(ex instanceof FileNotFoundException ?
                "The requested file could not be found" : ex.getMessage());
        log.error("Resource not found", ex);
        return error;
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler({
            DuplicateKeyException.class,
            SQLIntegrityConstraintViolationException.class,
            AlreadyExistedException.class
    })
    public ErrorResponse handleConflictExceptions(Exception ex, WebRequest request) {
        ErrorResponse error = createBaseErrorResponse(request, CONFLICT);
        error.setError("Resource Conflict");
        error.setMessage(ex.getMessage());
        log.error("Conflict error", ex);
        return error;
    }

    @ExceptionHandler({SQLException.class, NullPointerException.class, IOException.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleServerErrors(Exception ex, WebRequest request) {
        ErrorResponse error = createBaseErrorResponse(request, INTERNAL_SERVER_ERROR);

        if (ex instanceof SQLException) {
            error.setError("Database Error");
            error.setMessage("A database error occurred. Please try again later.");
        } else if (ex instanceof NullPointerException) {
            error.setError("Server Error");
            error.setMessage("An unexpected error occurred. Please contact support.");
        } else {
            error.setError("I/O Error");
            error.setMessage("A file processing error occurred.");
        }

        log.error("Server error", ex);
        return error;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
    public ErrorResponse handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        ErrorResponse error = createBaseErrorResponse(request, UNSUPPORTED_MEDIA_TYPE);
        error.setMessage("Content-Type not supported: " + ex.getContentType());
        log.error("Unsupported media type", ex);
        return error;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(PAYLOAD_TOO_LARGE)
    public ErrorResponse handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex, WebRequest request) {
        ErrorResponse error = createBaseErrorResponse(request, PAYLOAD_TOO_LARGE);
        error.setMessage("The file size exceeds the maximum allowed limit");
        log.error("File size limit exceeded", ex);
        return error;
    }

    @ExceptionHandler(SQLTimeoutException.class)
    @ResponseStatus(GATEWAY_TIMEOUT)
    public ErrorResponse handleSQLTimeout(SQLTimeoutException ex, WebRequest request) {
        ErrorResponse error = createBaseErrorResponse(request, GATEWAY_TIMEOUT);
        error.setError("Database Timeout");
        error.setMessage("The database request timed out. Please try again.");
        log.error("Database timeout", ex);
        return error;
    }

    @ExceptionHandler(FileStorageException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleFileStorageException(FileStorageException ex, WebRequest request) {
        ErrorResponse error = createBaseErrorResponse(request, INTERNAL_SERVER_ERROR);
        error.setError("File Storage Error");
        error.setMessage("Failed to store file in MinIO. Please try again later.");
        log.error("File storage error", ex);
        return error;
    }

    // Fallback handler for any unhandled exceptions
    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpectedExceptions(Exception ex, WebRequest request) {
        ErrorResponse error = createBaseErrorResponse(request, INTERNAL_SERVER_ERROR);
        error.setError("Unexpected Error");
        error.setMessage("An unexpected error occurred. Please contact support.");
        log.error("Unexpected error", ex, ex);
        return error;
    }

    // Utility methods
    private String extractMethodArgumentErrorMessage(MethodArgumentNotValidException ex) {
        String message = ex.getMessage();
        int start = message.lastIndexOf("[") + 1;
        int end = message.lastIndexOf("]");
        return end > start ? message.substring(start, end) : "Invalid input parameters";
    }

    private String extractConstraintViolationMessage(ConstraintViolationException ex) {
        String message = ex.getMessage();
        return message.contains(" ") ? message.substring(message.indexOf(" ") + 1) : message;
    }


//    @ExceptionHandler(MailConnectException.class)
//    @ResponseStatus(SERVICE_UNAVAILABLE)
//    public ErrorResponse handleMailConnectException(MailConnectException ex, WebRequest request) {
//        String path = request.getDescription(false).replace("uri=", "");
//        ErrorResponse error = new ErrorResponse();
//        error.setTimestamp(new Date());
//        error.setPath(path);
//        error.setStatus(SERVICE_UNAVAILABLE.value());
//        error.setError("Mail Server Connection Error");
//        error.setMessage("Unable to connect to the email server. Please try again later.");
//        log.error("MailConnectException: ", ex);
//        return error;
//    }


//import org.springframework.mail.MailSendException;
//
//    @ExceptionHandler(MailSendException.class)
//    @ResponseStatus(INTERNAL_SERVER_ERROR)
//    public ErrorResponse handleMailSendException(MailSendException ex, WebRequest request) {
//        String path = request.getDescription(false).replace("uri=", "");
//        ErrorResponse error = new ErrorResponse();
//        error.setTimestamp(new Date());
//        error.setPath(path);
//        error.setStatus(INTERNAL_SERVER_ERROR.value());
//        error.setError("Email Send Failed");
//        error.setMessage("Failed to send the email. Please try again later.");
//        log.error("MailSendException: ", ex);
//        return error;
//    }


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
