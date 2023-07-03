package br.fiap.projeto.contexto.pedido.application.rest;

import br.fiap.projeto.contexto.pedido.domain.dto.PedidoCriarDTO;
import br.fiap.projeto.contexto.pedido.domain.dto.PedidoDTO;
import br.fiap.projeto.contexto.pedido.domain.dto.ProdutoPedidoDTO;
import br.fiap.projeto.contexto.pedido.domain.port.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;
    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }
    //-------------------------------------------------------------------------//
    //                         BASE CRUD
    //-------------------------------------------------------------------------//
    @PostMapping
    @ResponseBody
    public PedidoDTO criaPedido(@RequestBody PedidoCriarDTO pedido) {
        return this.pedidoService.criaPedido(pedido);
    }
    //-------------------------------------------------------------------------//
    //                        ATUALIZA STATUS
    //-------------------------------------------------------------------------//
    @PatchMapping("/{codigo}/receber")
    @ResponseBody
    public PedidoDTO receberPedido(@PathVariable("codigo") UUID codigo) throws Exception {
        return this.pedidoService.aprovar(codigo);
    }
    @PatchMapping("/{codigo}/aprovar")
    @ResponseBody
    public PedidoDTO aprovarPedido(@PathVariable("codigo") UUID codigo) throws Exception {
        return this.pedidoService.aprovar(codigo);
    }
    @PatchMapping("/{codigo}/prontificar")
    @ResponseBody
    public PedidoDTO prontificarPedido(@PathVariable("codigo") UUID codigo) throws Exception {
        return this.pedidoService.prontificar(codigo);
    }
    @PatchMapping("/{codigo}/finalizar")
    @ResponseBody
    public PedidoDTO finalizarPedido(@PathVariable("codigo") UUID codigo) throws Exception {
        return this.pedidoService.finalizar(codigo);
    }
    //-------------------------------------------------------------------------//
    //                MÉTODOS DE MANUPULAÇÃO DE ITENS DO PEDIDO
    //-------------------------------------------------------------------------//
    @PostMapping("/{codigo}/adicionar-produto")
    @ResponseBody
    public PedidoDTO adicionarProduto(@PathVariable("codigo") UUID codigo, @RequestBody ProdutoPedidoDTO produtoPedidoDTO) throws Exception {
        return this.pedidoService.adicionarProduto(codigo,produtoPedidoDTO);
    }
    @PostMapping("/{codigo}/remover-produto/{produto_codigo}")
    public void removerProduto(@PathVariable("codigo") UUID codigo, @PathVariable("produto_codigo") UUID produto_codigo) throws Exception {
        this.pedidoService.removerProduto(codigo,produto_codigo);
    }
    @PostMapping("/{codigo}/aumentar-qtde-produto")
    @ResponseBody
    public PedidoDTO adicionarQuantidadeProduto(@PathVariable("codigo") UUID codigo, @RequestBody ProdutoPedidoDTO produtoPedidoDTO) throws Exception {
        return this.pedidoService.aumentarQuantidade(codigo,produtoPedidoDTO);
    }
    @Transactional
    @PostMapping("/{codigo}/reduzir-qtde-produto")
    @ResponseBody
    public PedidoDTO reduzirQuantidadeProduto(@PathVariable("codigo") UUID codigo, @RequestBody ProdutoPedidoDTO produtoPedidoDTO) throws Exception {
        return this.pedidoService.reduzirQuantidade(codigo,produtoPedidoDTO);
    }

//    @GetMapping("/{codigo}")
//    @ResponseBody
//    public PedidoDTO getPedidos(@PathVariable("codigo") UUID codigo) {
//        return this.pedidoService.buscaPedido(codigo);
//    }
//
//    @Transactional
//    @DeleteMapping("/{codigo}")
//    public void removerPedido(@PathVariable("codigo") UUID codigo){
//        this.pedidoService.removePedido(codigo);
//    }
}