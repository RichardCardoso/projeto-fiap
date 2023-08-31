package br.fiap.projeto.contexto.pedido.infrastructure.mapper;

import br.fiap.projeto.contexto.pedido.domain.ItemPedido;
import br.fiap.projeto.contexto.pedido.infrastructure.entity.ItemPedidoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemPedidoMapper {
    @Autowired
    private static PedidoMapper pedidoMapper;

    public static ItemPedidoEntity toEntity(ItemPedido itemPedido) {

        return new ItemPedidoEntity(itemPedido.getCodigo(),
                pedidoMapper.toEntityWithoutItens(itemPedido.getPedido()),
                itemPedido.getQuantidade(),
                itemPedido.getProdutoNome(),
                itemPedido.getProdutoDescricao(),
                itemPedido.getValorUnitario(),
                itemPedido.getCategoriaProduto(),
                itemPedido.getImagem(),
                itemPedido.getTempoPreparoMin());
    }

    public static ItemPedido toDomain(ItemPedidoEntity itemPedidoEntity) {
        return new ItemPedido(itemPedidoEntity.getCodigo(),
                pedidoMapper.toDomainWithoutItens(itemPedidoEntity.getPedido()),
                itemPedidoEntity.getQuantidade(),
                itemPedidoEntity.getProdutoNome(),
                itemPedidoEntity.getProdutoDescricao(),
                itemPedidoEntity.getValorUnitario(),
                itemPedidoEntity.getCategoriaProduto(),
                itemPedidoEntity.getImagem(),
                itemPedidoEntity.getTempoPreparoMin());
    }
}