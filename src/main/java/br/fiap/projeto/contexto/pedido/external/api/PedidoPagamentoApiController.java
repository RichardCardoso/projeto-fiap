package br.fiap.projeto.contexto.pedido.external.api;

import br.fiap.projeto.contexto.pedido.adapter.controller.rest.response.PedidoDTO;
import br.fiap.projeto.contexto.pedido.entity.enums.StatusPagamento;
import br.fiap.projeto.contexto.pedido.usecase.port.PedidoService;
import br.fiap.projeto.contexto.pedido.external.integration.PedidoComandaIntegration;
import br.fiap.projeto.contexto.pedido.external.integration.PedidoPagamentoIntegration;
import br.fiap.projeto.contexto.pedido.external.integration.port.Pagamento;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
public class PedidoPagamentoApiController {
    private final PedidoService pedidoService;
    private final PedidoPagamentoIntegration pedidoPagamentoIntegration;
    private final PedidoComandaIntegration pedidoComandaIntegration;
    @Autowired
    public PedidoPagamentoApiController(PedidoService pedidoService, PedidoPagamentoIntegration pedidoPagamentoIntegration, PedidoComandaIntegration pedidoComandaIntegration) {
        this.pedidoService = pedidoService;
        this.pedidoPagamentoIntegration = pedidoPagamentoIntegration;
        this.pedidoComandaIntegration = pedidoComandaIntegration;
    }
    //-------------------------------------------------------------------------//
    //                        INTEGRAÇÃO PAGAMENTO
    //-------------------------------------------------------------------------//
    @PatchMapping("/{codigo}/verificar-pagamento")
    @ResponseBody
    public ResponseEntity<PedidoDTO> verificarPagamento(@ApiParam(value="Código do Pedido") @PathVariable("codigo") UUID codigo) throws Exception {
        PedidoDTO pedidoDTO = null;
        try {
            Pagamento pagamento = pedidoPagamentoIntegration.buscaStatusPagamentoPorCodigoPedido(codigo.toString());
            // Verifica se encontrou o pagamento e se ele está aprovado
            if(pagamento != null && pagamento.getStatusPagamento().getDescricao().equals(StatusPagamento.APPROVED.getDescricao())){
                pedidoDTO = this.pedidoService.pagar(codigo);
            }else{
                //TODO: tratar erro aqui
                System.out.println("Pedido não encontrado ou não aprovado!");
                throw new Exception("Pedido não encontrado ou não aprovado!");
            }
        }catch(Exception e){
            //TODO: tratar erro aqui
            System.out.println("Erro ao realizar integração!");
            throw new Exception("Erro ao realizar integração!",e);
        }
        return ResponseEntity.ok().body(pedidoDTO);
    }
}