package br.fiap.projeto.contexto.pagamento.application.rest;

import br.fiap.projeto.contexto.pagamento.domain.dto.PagamentoDTO;
import br.fiap.projeto.contexto.pagamento.domain.dto.PagamentoStatusDTO;
import br.fiap.projeto.contexto.pagamento.domain.enums.StatusPagamento;
import br.fiap.projeto.contexto.pagamento.domain.port.service.PagamentoServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/v1/pagamentos")
public class PagamentoController {


    private final PagamentoServicePort pagamentoServicePort;

    @Autowired
    public PagamentoController(PagamentoServicePort pagamentoServicePort){
        this.pagamentoServicePort = pagamentoServicePort;
    }


    @GetMapping(value="/{codigo}")
    @Transactional
    public ResponseEntity<PagamentoDTO> buscaPagamentoPorCodigo(@PathVariable("codigo") UUID codigo){
        PagamentoDTO pagamentoDTO = pagamentoServicePort.findByCodigo(codigo);
        return ResponseEntity.ok().body(pagamentoDTO);
    }

    @GetMapping(value="/todos")
    @Transactional
    public ResponseEntity<Page<PagamentoDTO>> listaPagamentos(Pageable pageable){
        Page<PagamentoDTO> paginaComPagamentos = pagamentoServicePort.buscaListaDePagamentos(pageable);
        return ResponseEntity.ok().body(paginaComPagamentos);

    }

    @PostMapping(value="/processa-pagamento")
    @Transactional
    public ResponseEntity<PagamentoDTO> iniciaPagamento(@RequestBody PagamentoDTO pagamentoDTO) throws Exception {

        pagamentoServicePort.verificaNumeroDoPedido(pagamentoDTO);

        pagamentoServicePort.criaPagamento(pagamentoDTO);
        //boa prática: devolver o novo recurso criado utilizando o código correto (created 201)
        URI novoRecursoDePagamentoCriadoUri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/codigo}").buildAndExpand(pagamentoDTO.getCodigoPedido()).toUri();
        return ResponseEntity.created(novoRecursoDePagamentoCriadoUri).body(pagamentoDTO);
    }

    @PostMapping(value="/url-do-gateway")
    @Transactional
    public ResponseEntity<Void> enviaCompraParaGateway(){

        System.out.println("Enviando CompraAPagarDTO para o gateway de pagamento");
        pagamentoServicePort.enviaGatewayDePagamento();

        return ResponseEntity.ok().build();
    }

    @PatchMapping(value="/processa-pagamento/{codigo}")
    @Transactional
    public ResponseEntity<Void> atualizaPagamento(@PathVariable("codigo") UUID codigo, @RequestBody PagamentoStatusDTO pagamentoStatusDTO){
        pagamentoServicePort.findByCodigo(codigo);
        pagamentoServicePort.processaPagamento(codigo, pagamentoStatusDTO.getStatus());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value="/por-status/{status}")
    @Transactional
    public ResponseEntity <Page<PagamentoDTO>> listaDePagamentos(@PathVariable ("status") StatusPagamento status, Pageable pageable){
        Page<PagamentoDTO> paginaPagamentoPorStatus = pagamentoServicePort.findByStatus(status, pageable);
        return ResponseEntity.ok().body(paginaPagamentoPorStatus);

    }

}