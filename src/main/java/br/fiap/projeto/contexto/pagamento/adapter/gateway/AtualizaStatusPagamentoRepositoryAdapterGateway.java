package br.fiap.projeto.contexto.pagamento.adapter.gateway;

import br.fiap.projeto.contexto.pagamento.entity.Pagamento;
import br.fiap.projeto.contexto.pagamento.external.repository.entity.PagamentoEntity;
import br.fiap.projeto.contexto.pagamento.external.repository.postgres.SpringPagamentoRepository;
import br.fiap.projeto.contexto.pagamento.usecase.port.repository.IAtualizaStatusPagamentoRepositoryAdapterGateway;



public class AtualizaStatusPagamentoRepositoryAdapterGateway implements IAtualizaStatusPagamentoRepositoryAdapterGateway {

    private final SpringPagamentoRepository springPagamentoRepository;

    public AtualizaStatusPagamentoRepositoryAdapterGateway(SpringPagamentoRepository springPagamentoRepository) {
        this.springPagamentoRepository = springPagamentoRepository;
    }

    public void atualizaStatusPagamento(Pagamento pagamento){
        PagamentoEntity pagamentoStatusAtualizado = springPagamentoRepository.findByCodigoPedido(pagamento.getCodigoPedido());
        this.springPagamentoRepository.save(pagamentoStatusAtualizado);
    }
}
