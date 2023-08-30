package br.fiap.projeto.contexto.pagamento.usecase.port.repository;

import br.fiap.projeto.contexto.pagamento.entity.Pagamento;

public interface IProcessaNovoPagamentoRepositoryAdapterGateway {

    Pagamento salvaNovoPagamento(Pagamento pagamento);

    void verificaCondicoesParaCriarPagamento(Pagamento pagamento);

    Boolean verificaSeJaExistePagamentoParaOPedido(Pagamento pagamento);
}
