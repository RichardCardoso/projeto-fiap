package br.fiap.projeto.contexto.comanda.usecase.port.repositoryInterface;

import java.util.List;

import br.fiap.projeto.contexto.comanda.entity.Comanda;
import br.fiap.projeto.contexto.comanda.entity.enums.StatusComanda;
import br.fiap.projeto.contexto.comanda.external.exception.ExceptionMessage;

public interface IBuscarPorStatusRecebidoComandaRepositoryUseCase {
    List<Comanda> buscaComandaPorStatus(StatusComanda status) throws ExceptionMessage, Exception;
}
