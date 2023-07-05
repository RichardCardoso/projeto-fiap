package br.fiap.projeto.contexto.comanda.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import br.fiap.projeto.contexto.comanda.domain.Comanda;
import br.fiap.projeto.contexto.comanda.domain.dto.ComandaDTO;
import br.fiap.projeto.contexto.comanda.domain.dto.CriarComandaDTO;
import br.fiap.projeto.contexto.comanda.domain.enums.StatusComanda;
import br.fiap.projeto.contexto.comanda.domain.exception.InvalidStatusException;
import br.fiap.projeto.contexto.comanda.domain.port.repository.ComandaRepositoryPort;
import br.fiap.projeto.contexto.comanda.domain.port.service.ComandaServicePort;

public class DomainComandaService implements ComandaServicePort {

    private final ComandaRepositoryPort comandaRepositoryPort;

    public DomainComandaService(ComandaRepositoryPort comandaRepositoryPort) {
        this.comandaRepositoryPort = comandaRepositoryPort;
    }

    // ---------------------------------------------------------------------------------
    // CRUD BASE
    // ---------------------------------------------------------------------------------
    @Override
    public ComandaDTO criarComanda(CriarComandaDTO criarComandaDTO) {
        return (comandaRepositoryPort.salvar(new Comanda(criarComandaDTO))).toComandaDTO();
    }

    // @Override
    // public void atualizaComanda(ComandaDTO comanda) {
    // comandaRepositoryPort.atualizaComanda(new Comanda(comanda));
    // }

    // ---------------------------------------------------------------------------------
    // METODO DE BUSCAS POR STATUS
    // ---------------------------------------------------------------------------------

    @Override
    public List<ComandaDTO> buscaComandaRecebido() {
        List<Comanda> comanda = comandaRepositoryPort.buscaPorStatus(StatusComanda.RECEBIDO);
        return comanda.stream().map(Comanda::toComandaDTO).collect(Collectors.toList());
    }

    @Override
    public List<ComandaDTO> buscaComandaPreparacao() {
        List<Comanda> comanda = comandaRepositoryPort.buscaPorStatus(StatusComanda.EM_PREPARACAO);
        return comanda.stream().map(Comanda::toComandaDTO).collect(Collectors.toList());
    }

    @Override
    public List<ComandaDTO> buscaComandaFinalizado() {
        List<Comanda> comanda = comandaRepositoryPort.buscaPorStatus(StatusComanda.FINALIZADO);
        return comanda.stream().map(Comanda::toComandaDTO).collect(Collectors.toList());
    }

    // ---------------------------------------------------------------------------------
    // ATUALIZAR STATUS
    // ---------------------------------------------------------------------------------
    @Override
    public ComandaDTO preparar(UUID codigoPedido) throws InvalidStatusException {
        Comanda comanda = this.buscar(codigoPedido);
        if (comanda.getStatus().equals(StatusComanda.RECEBIDO)) {
            comanda.atualizaStatus(StatusComanda.EM_PREPARACAO);
        } else {
            throw new InvalidStatusException(comanda.getStatus().toString());
        }
        return comandaRepositoryPort.salvar(comanda).toComandaDTO();
    }

    @Override
    public ComandaDTO finalizar(UUID codigoPedido) throws InvalidStatusException {
        Comanda comanda = this.buscar(codigoPedido);
        if (comanda.getStatus().equals(StatusComanda.EM_PREPARACAO)) {
            comanda.atualizaStatus(StatusComanda.FINALIZADO);
        } else {
            throw new InvalidStatusException(comanda.getStatus().toString());
        }
        return comandaRepositoryPort.salvar(comanda).toComandaDTO();
    }
    // ---------------------------------------------------------------------------------
    // MÉTODOS AUXILAIRES
    // ---------------------------------------------------------------------------------

    private Comanda buscar(UUID codigoComanda) {
        Optional<Comanda> optionalComanda = comandaRepositoryPort.buscar(codigoComanda);
        optionalComanda.orElseThrow(() -> new EntityNotFoundException("Comanda não encontrada!"));
        return optionalComanda.get();
    }

}
