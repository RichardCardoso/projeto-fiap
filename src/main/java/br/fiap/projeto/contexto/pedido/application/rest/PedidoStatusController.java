package br.fiap.projeto.contexto.pedido.application.rest;

import br.fiap.projeto.contexto.pedido.application.rest.response.PedidoDTO;
import br.fiap.projeto.contexto.pedido.domain.port.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
public class PedidoStatusController {
    private final PedidoService pedidoService;
    @Autowired
    public PedidoStatusController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    //-------------------------------------------------------------------------//
    //                        BUSCA POR STATUS
    //-------------------------------------------------------------------------//
    @GetMapping("busca-recebidos")
    @ResponseBody
    public ResponseEntity<List<PedidoDTO>> getProdutosRecebidos() {
        List<PedidoDTO> lista = this.pedidoService.buscarTodosRecebido();
        return ResponseEntity.ok().body(lista);
    }
    @GetMapping("busca-em-preparacao")
    @ResponseBody
    public ResponseEntity<List<PedidoDTO>> getProdutosEmPreparacao() {
        List<PedidoDTO> lista = this.pedidoService.buscarTodosEmPreparacao();
        return ResponseEntity.ok().body(lista);
    }
    @GetMapping("busca-prontos")
    @ResponseBody
    public ResponseEntity<List<PedidoDTO>> getProdutosProntos() {
        List<PedidoDTO> lista = this.pedidoService.buscarTodosPronto();
        return ResponseEntity.ok().body(lista);
    }
    @GetMapping("busca-finalizados")
    @ResponseBody
    public ResponseEntity<List<PedidoDTO>> getProdutosFinalizados() {
        List<PedidoDTO> lista = this.pedidoService.buscarTodosFinalizado();
        return ResponseEntity.ok().body(lista);
    }
}