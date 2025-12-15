package br.com.alurafood.pagamentos.api.dto;

import br.com.alurafood.pagamentos.domain.model.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
public class PagamentoDto {

    private Long id;

    @NotNull(message = "O valor do pagamento é obrigatório")
    @Positive(message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    @NotBlank(message = "O nome do titular é obrigatório")
    @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "O número do cartão é obrigatório")
    @Size(max = 19, message = "O número do cartão deve ter no máximo 19 caracteres")
    private String numero;

    @NotBlank(message = "A data de expiração é obrigatória")
    @Size(max = 7, message = "A expiração deve estar no formato MM/AAAA")
    private String expiracao;

    @NotBlank(message = "O código de segurança é obrigatório")
    @Size(min = 3, max = 3, message = "O código de segurança deve ter 3 dígitos")
    private String codigo;

    private Status status;

    @NotNull(message = "A forma de pagamento é obrigatória")
    private Long formaDePagamentoId;

    @NotNull(message = "O pedidoId é obrigatório")
    private Long pedidoId;
}
