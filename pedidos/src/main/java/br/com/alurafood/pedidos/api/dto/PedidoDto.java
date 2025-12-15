package br.com.alurafood.pedidos.api.dto;

import br.com.alurafood.pedidos.domain.model.Status;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDto {

    private Long id;
    @NotNull(message = "Data e Hora precisa ser preenchida.")
    private LocalDateTime dataHora;
    @NotNull(message = "Status precisa ser preenchido.")
    private Status status;
    private List<ItemDoPedidoDto> itens = new ArrayList<>();



}
