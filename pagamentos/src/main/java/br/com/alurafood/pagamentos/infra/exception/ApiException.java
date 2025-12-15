package br.com.alurafood.pagamentos.infra.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception personalizada para padronizar erros da API.
 */
public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final String path;
    private final Object detalhes;

    public ApiException(String mensagem, HttpStatus status, String path) {
        super(mensagem);
        this.status = status;
        this.path = path;
        this.detalhes = null;
    }

    public ApiException(String mensagem, HttpStatus status, String path, Object detalhes) {
        super(mensagem);
        this.status = status;
        this.path = path;
        this.detalhes = detalhes;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getPath() {
        return path;
    }

    public Object getDetalhes() {
        return detalhes;
    }
}
