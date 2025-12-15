package br.com.alurafood.pagamentos.domain.service;

import br.com.alurafood.pagamentos.api.dto.PagamentoDto;
import br.com.alurafood.pagamentos.domain.model.Pagamento;
import br.com.alurafood.pagamentos.domain.model.Status;
import br.com.alurafood.pagamentos.domain.repository.PagamentoRepositoy;
import br.com.alurafood.pagamentos.infra.exception.ApiException;
import br.com.alurafood.pagamentos.infra.http.PedidoClient;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class PagamentoService {

    private final PagamentoRepositoy repository;
    private final ModelMapper modelMapper;
    private final PedidoClient pedidoClient;

    public PagamentoService(PagamentoRepositoy repository,
                            ModelMapper modelMapper,
                            PedidoClient pedidoClient) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.pedidoClient = pedidoClient;
    }

    public Page<PagamentoDto> obterTodos(Pageable paginacao) {
        return repository.findAll(paginacao)
                .map(pagamento -> modelMapper.map(pagamento, PagamentoDto.class));
    }

    public PagamentoDto obterPorId(Long id) {
        Pagamento pagamento = buscarPagamentoPorId(id);
        return modelMapper.map(pagamento, PagamentoDto.class);
    }

    public PagamentoDto criarPagamento(PagamentoDto dto) {
        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setStatus(Status.CRIADO);
        pagamento = repository.save(pagamento);
        return modelMapper.map(pagamento, PagamentoDto.class);
    }

    public PagamentoDto atualizarPagamento(Long id, PagamentoDto dto) {
        Pagamento pagamento = buscarPagamentoPorId(id);

        // atualiza somente os campos vindos do DTO
        modelMapper.map(dto, pagamento);

        pagamento = repository.save(pagamento);
        return modelMapper.map(pagamento, PagamentoDto.class);
    }

    public void excluirPagamento(Long id) {
        if (!repository.existsById(id)) {
            throw new ApiException(
                    "Pagamento não encontrado",
                    HttpStatus.NOT_FOUND,
                    "/pagamentos/" + id
            );
        }
        repository.deleteById(id);
    }

    public void confirmarPagamento(Long id) {
        Pagamento pagamento = buscarPagamentoPorId(id);

        pagamento.setStatus(Status.CONFIRMADO);
        repository.save(pagamento);
        // integração com pedido
        pedidoClient.atualizaPagamento(pagamento.getPedidoId());
    }

    public void confirmarSemIntegracao(Long id) {
        Pagamento pagamento = buscarPagamentoPorId(id);

        pagamento.setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
        repository.save(pagamento);
    }

    
    private Pagamento buscarPagamentoPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApiException(
                        "Pagamento não encontrado",
                        HttpStatus.NOT_FOUND,
                        "/pagamentos/" + id
                ));
    }

    public void alteraStatus(Long id) {
        Optional<Pagamento> pagamento = repository.findById(id);
        if (!pagamento.isPresent()) {
            throw new EntityNotFoundException();
        }
        pagamento.get().setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
        repository.save(pagamento.get());
    }
}
