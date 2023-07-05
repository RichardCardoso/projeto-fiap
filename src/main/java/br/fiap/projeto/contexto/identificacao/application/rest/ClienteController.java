package br.fiap.projeto.contexto.identificacao.application.rest;

import br.fiap.projeto.contexto.identificacao.application.rest.request.ClienteRequestDTO;
import br.fiap.projeto.contexto.identificacao.application.rest.response.ClienteDTO;
import br.fiap.projeto.contexto.identificacao.domain.port.service.ClienteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
@Log4j2
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ClienteDTO busca(String codigo) {

        return ClienteDTO.fromCliente(clienteService.busca(codigo));
    }

    @GetMapping("/cpf")
    public ClienteDTO buscaPorCpf(@RequestParam String cpf) {

        return ClienteDTO.fromCliente(clienteService.buscaPorCpf(cpf));
    }

    @GetMapping("/todos")
    public List<ClienteDTO> buscaTodos() {

        return clienteService.buscaTodos().stream()
                .map(ClienteDTO::fromCliente)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> insere(@RequestBody ClienteRequestDTO cliente) {

        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteDTO.fromCliente(clienteService.insere(cliente)));
    }

    @PutMapping
    public ClienteDTO edita(@RequestBody ClienteDTO cliente) {

        return ClienteDTO.fromCliente(clienteService.edita(cliente.toCliente()));
    }

    @DeleteMapping
    public void remove(@RequestParam String codigo) {

        clienteService.remove(codigo);
    }
}
