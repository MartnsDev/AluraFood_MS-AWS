package br.com.alurafood.pagamentos.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<RetornoErroAPI> handleApiException(ApiException ex) {

        RetornoErroAPI erro = new RetornoErroAPI(
                false,
                ex.getMessage(),
                ex.getStatus().value(),
                ex.getPath(),
                LocalDateTime.now(),
                ex.getDetalhes() instanceof List<?> lista
                        ? lista.stream().map(Object::toString).toList()
                        : null
        );

        return ResponseEntity.status(ex.getStatus()).body(erro);
    }

    // 2. Erros de validação (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RetornoErroAPI> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.toList());

        RetornoErroAPI retorno = new RetornoErroAPI(
                false,
                "Erro de validação",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                LocalDateTime.now(),
                erros
        );

        return ResponseEntity.badRequest().body(retorno);
    }

    // 3. Recurso não encontrado
    @ExceptionHandler({EntityNotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<RetornoErroAPI> handleNotFoundException(
            Exception ex,
            HttpServletRequest request) {

        RetornoErroAPI retorno = new RetornoErroAPI(
                false,
                ex.getMessage() != null ? ex.getMessage() : "Recurso não encontrado",
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(retorno);
    }

    // 4. Segurança (Auth / Accesso
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<RetornoErroAPI> handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request) {

        RetornoErroAPI retorno = new RetornoErroAPI(
                false,
                "Não autenticado",
                HttpStatus.UNAUTHORIZED.value(),
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(retorno);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RetornoErroAPI> handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request) {

        RetornoErroAPI retorno = new RetornoErroAPI(
                false,
                "Acesso negado",
                HttpStatus.FORBIDDEN.value(),
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(retorno);
    }

    // 5. Erro genérico (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RetornoErroAPI> handleAllExceptions(
            Exception ex,
            HttpServletRequest request) {

        RetornoErroAPI erro = new RetornoErroAPI(
                false,
                "Erro interno no servidor",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}

