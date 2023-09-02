package br.fiap.projeto.contexto.pagamento.usecase.exceptions.mensagens;

public enum MensagemDeErro {

    PAGAMENTO_NAO_ENCONTRADO("Pagamento não encontrado! Verifique o código."),
    PEDIDO_PAGAMENTO_NAO_ENCONTRADO("Pagamento para este código de pedido não foi encontrado. Verifique o código."),

    STATUS_INVALIDO("Transição entre estados de pagamento inválida. Verifique e tente novamente."),

    STATUS_INVALIDO_ENVIO_GATEWAY("Pagamento não pode ser enviado ao Gateway. Status de pagamento inválido."),

    PAGAMENTO_EXISTENTE("Já existe um pagamento em processamento para este código de Pedido.")
    ;


    private final String message;

    MensagemDeErro(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
