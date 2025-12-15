package br.com.alurafood.pagamentos.infra.exception;

import java.time.LocalDateTime;
import java.util.List;


public record RetornoErroAPI(

        boolean sucesso,
        String mensagem,
        int status,
        String path,
        LocalDateTime timestamp,
        List<String> erros

) {
}
