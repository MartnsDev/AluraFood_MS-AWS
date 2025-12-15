package br.com.alurafood.pedidos.api.dto;

import br.com.alurafood.pedidos.domain.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusDto {
    private Status status;
}
