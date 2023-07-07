package br.fiap.projeto.contexto.pedido.infrastructure.integration.port;

import br.fiap.projeto.contexto.pedido.domain.enums.StatusPagamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagamento {
    private String codigoPedido;
    private Date dataPagamento;
    private StatusPagamento statusPagamento;
}
