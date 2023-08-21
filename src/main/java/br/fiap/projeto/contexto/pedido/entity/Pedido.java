package br.fiap.projeto.contexto.pedido.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.fiap.projeto.contexto.pedido.adapter.controller.rest.request.PedidoCriarDTO;
import br.fiap.projeto.contexto.pedido.adapter.controller.rest.response.PedidoDTO;
import br.fiap.projeto.contexto.pedido.entity.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
 
	private UUID codigo;
	private List<ItemPedido> itens = new ArrayList<>();
	private UUID cliente;
	private StatusPedido status;
	private Double valorTotal;
    public Pedido(PedidoDTO pedidoDTO) {
		this.codigo = pedidoDTO.getCodigo();
		this.itens = pedidoDTO.getItens();
		this.cliente = pedidoDTO.getCliente();
		this.status = pedidoDTO.getStatus();
		this.valorTotal = pedidoDTO.getValorTotal();
    }
	public Pedido(PedidoCriarDTO pedidoCriarDTO){
		if(pedidoCriarDTO != null) {
			this.cliente = pedidoCriarDTO.getCliente();
		}
		this.status = StatusPedido.INICIADO;
		this.valorTotal = 0d;
	}
	public void atualizarValorTotal(Double novoValor){
		this.valorTotal = novoValor;
	}
	public void adicionarItem(ItemPedido itemPedido) {
		this.itens.add(itemPedido);
	}
	public void removerProduto(UUID produto) {}
	public List<ItemPedido> listarItens() {
		return null;
	}
	public void atualizarStatus(StatusPedido status){
		this.status = status;
	}
	public PedidoDTO toPedidoDTO() {
		return new PedidoDTO( codigo, itens, cliente, status, valorTotal);
	}
}
 