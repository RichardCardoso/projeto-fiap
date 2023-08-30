package br.fiap.projeto.contexto.pagamento.adapter.controller;

import br.fiap.projeto.contexto.pagamento.adapter.controller.rest.port.IEnviaPagamentoGatewayRestAdapterController;
import br.fiap.projeto.contexto.pagamento.adapter.controller.rest.request.PagamentoAEnviarAoGatewayDTORequest;
import br.fiap.projeto.contexto.pagamento.adapter.controller.rest.response.PagamentoDTOResponse;
import br.fiap.projeto.contexto.pagamento.entity.Pagamento;
import br.fiap.projeto.contexto.pagamento.entity.enums.StatusPagamento;
import br.fiap.projeto.contexto.pagamento.usecase.exceptions.UnprocessablePaymentException;
import br.fiap.projeto.contexto.pagamento.usecase.port.usecase.IAtualizaStatusPagamentoUsecase;
import br.fiap.projeto.contexto.pagamento.usecase.port.usecase.IBuscaPagamentoUseCase;
import br.fiap.projeto.contexto.pagamento.usecase.port.usecase.IEnviaPagamentoAoGatewayPagamentosUseCase;
import br.fiap.projeto.contexto.pagamento.usecase.port.usecase.IProcessaNovoPagamentoUseCase;

public class EnviaPagamentoAoGatewayRestAdapterController implements  IEnviaPagamentoGatewayRestAdapterController {

    private final IEnviaPagamentoAoGatewayPagamentosUseCase enviaPagamentoAoGatewayPagamentosUseCase;

    private final IProcessaNovoPagamentoUseCase processaNovoPagamentoUseCase;
    private final IAtualizaStatusPagamentoUsecase atualizaStatusPagamentoUsecase;

    private final IBuscaPagamentoUseCase buscaPagamentoUseCase;

    public EnviaPagamentoAoGatewayRestAdapterController(IEnviaPagamentoAoGatewayPagamentosUseCase enviaPagamentoAoGatewayPagamentosUseCase, IProcessaNovoPagamentoUseCase processaNovoPagamentoUseCase , IAtualizaStatusPagamentoUsecase atualizaStatusPagamentoUsecase, IBuscaPagamentoUseCase buscaPagamentoUseCase) {
        this.enviaPagamentoAoGatewayPagamentosUseCase = enviaPagamentoAoGatewayPagamentosUseCase;
        this.processaNovoPagamentoUseCase = processaNovoPagamentoUseCase;
        this.atualizaStatusPagamentoUsecase = atualizaStatusPagamentoUsecase;
        this.buscaPagamentoUseCase = buscaPagamentoUseCase;

    }

    @Override //TODO implementar a chamda do enviaPagamentoUseCase onde terá a lógica da chamada do Gateway de Pgto
    public void enviaParaGatewayDePagamento(PagamentoAEnviarAoGatewayDTORequest pagamentoAEnviarAoGatewayDTORequest) {
        System.out.println("RestAdapterController: Enviando request de pagamento ao sistema externo...");
        enviaPagamentoAoGatewayPagamentosUseCase.enviaRequestAoSistemaExternoPagamentos(getByCodigoPedido(pagamentoAEnviarAoGatewayDTORequest));

    }

    @Override
    public PagamentoAEnviarAoGatewayDTORequest preparaParaEnviarPagamentoAoGateway(PagamentoAEnviarAoGatewayDTORequest pagamentoAEnviarAoGatewayDTORequest) {
        if(!processaNovoPagamentoUseCase.verificaSeJaExistePagamentoParaOPedido(getByCodigoPedido(pagamentoAEnviarAoGatewayDTORequest))){
            throw new UnprocessablePaymentException("Não pode ser enviado ao Gateway, status inválido.");
        }
        //INFO atualiza os dados pois a request não possui todos
        PagamentoDTOResponse pagamentoDTOResponse = new PagamentoDTOResponse(getByCodigoPedido(pagamentoAEnviarAoGatewayDTORequest));
        validaStatusAtualDoPagamentoAntesDeEnviarAoGateway(pagamentoDTOResponse);

        atualizaStatusPagamentoUsecase.analisaStatusDoPagamento(
                pagamentoDTOResponse.getStatus(),
                pagamentoAEnviarAoGatewayDTORequest.getStatusPagamento(),
                pagamentoDTOResponse.conversorDePagamentoDTOResponseParaPagamento()
        );

        pagamentoDTOResponse.atualizaDadosRequest(pagamentoAEnviarAoGatewayDTORequest, pagamentoDTOResponse);
        atualizaStatusPagamentoUsecase.salvaStatus(pagamentoDTOResponse.conversorDePagamentoDTOResponseParaPagamento());

        return new PagamentoAEnviarAoGatewayDTORequest(getByCodigoPedido(pagamentoAEnviarAoGatewayDTORequest));
    }

    private static void validaStatusAtualDoPagamentoAntesDeEnviarAoGateway(PagamentoDTOResponse pagamentoDTOResponse) {
        if(!pagamentoDTOResponse.getStatus().equals(StatusPagamento.PENDING)){
            throw new UnprocessablePaymentException("Status inválido para enviar ao Sistema de Pagamentos");
        };
    }

    private Pagamento getByCodigoPedido(PagamentoAEnviarAoGatewayDTORequest pagamentoAEnviarAoGatewayDTORequest) {
        return buscaPagamentoUseCase.findByCodigoPedido(pagamentoAEnviarAoGatewayDTORequest.getCodigoPedido());
    }

}
