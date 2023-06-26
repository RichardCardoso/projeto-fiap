package br.fiap.projeto.contexto.identificacao.infrastructure.entity;

import br.fiap.projeto.contexto.identificacao.domain.entity.Cliente;
import br.fiap.projeto.contexto.identificacao.domain.vo.Cpf;
import br.fiap.projeto.contexto.identificacao.domain.vo.Email;
import lombok.SneakyThrows;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Entity
@Table(name = "clientes")
public class ClienteEntity {

    @Id
    private UUID codigo;

    private String nome;

    private String cpf;

    private String email;

    private LocalDateTime dataExclusao;

    public ClienteEntity() {
    }

    public ClienteEntity(UUID codigo, String nome, String cpf, String email) {

        this.codigo = codigo;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
    }

    public ClienteEntity(UUID codigo, String nome, Cpf cpf, Email email) {

        this(codigo, nome, cpf.getNumero(), email.getEndereco());
    }

    @SneakyThrows
    public Cliente toCliente() {

        return new Cliente(
                Optional.ofNullable(codigo).map(UUID::toString).orElse(null),
                nome,
                cpf,
                email
        );
    }

    public static ClienteEntity fromCliente(Cliente cliente) {

        return new ClienteEntity(
                Optional.ofNullable(cliente.getCodigo()).map(UUID::fromString).orElse(null),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail()
        );
    }

    public UUID getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getDataExclusao() {
        return dataExclusao;
    }

    public void setDataExclusao(LocalDateTime dataExclusao) {
        this.dataExclusao = dataExclusao;
    }
}
