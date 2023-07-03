package br.fiap.projeto.contexto.pedido.infrastructure.entity;

import br.fiap.projeto.contexto.pedido.domain.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="pedido")
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID codigo;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemPedidoEntity> itens;
    @Column(nullable = false)
    private UUID cliente;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    @Column(nullable = false, precision = 2)
    private Double valorTotal;
    public PedidoEntity(PedidoEntity pedido){
        this.codigo = pedido.getCodigo();
        this.itens = pedido.getItens();
        this.cliente = pedido.getCliente();
        this.status = pedido.getStatus();
        this.valorTotal = pedido.getValorTotal();
    }
}