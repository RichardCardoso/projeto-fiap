package br.fiap.projeto.contexto.pedido.usecase.port;

import br.fiap.projeto.contexto.pedido.entity.Pedido;

import java.util.UUID;

public interface IPedidoComandaIntegrationUseCase {
    Pedido criaComanda(UUID codigoPedido) throws Exception;
}
