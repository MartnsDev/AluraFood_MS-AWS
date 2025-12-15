package br.com.alurafood.pedidos.api.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDoPedidoDto {

    private Long id;
    @NotNull(message = "A quantidade precisa ser preenchida")
    @Positive(message = "Não pode ser negativo ou igual a 0")
    private Integer quantidade;
    private String descricao;
}
