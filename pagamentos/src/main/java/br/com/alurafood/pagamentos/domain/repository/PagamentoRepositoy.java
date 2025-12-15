package br.com.alurafood.pagamentos.domain.repository;

import br.com.alurafood.pagamentos.domain.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepositoy extends JpaRepository<Pagamento, Long> {
}
