package br.fiap.projeto.contexto.comanda.adapter.controller;

import java.util.UUID;

import br.fiap.projeto.contexto.comanda.adapter.controller.port.IAtualizaComandaControllerAdapter;
import br.fiap.projeto.contexto.comanda.adapter.controller.rest.dto.ComandaDTO;
import br.fiap.projeto.contexto.comanda.external.exception.ExceptionMessage;
import br.fiap.projeto.contexto.comanda.usecase.port.interfaces.IAtualizarComandaUseCase;

public class PreparaComandaControllerAdapter implements IAtualizaComandaControllerAdapter {

    private final IAtualizarComandaUseCase preparaComandaUseCase;

    public PreparaComandaControllerAdapter(IAtualizarComandaUseCase preparaComandaUseCase) {
        this.preparaComandaUseCase = preparaComandaUseCase;
    }

    @Override
    public ComandaDTO atualizaStatusComanda(UUID codigoComanda) throws ExceptionMessage {
        return ComandaDTO.newInstanceFromComanda(preparaComandaUseCase.atualizar(codigoComanda));
    }

}
