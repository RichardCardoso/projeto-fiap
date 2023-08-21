package br.fiap.projeto.contexto.pedido.usecase;

import br.fiap.projeto.contexto.pedido.entity.ItemPedido;
import br.fiap.projeto.contexto.pedido.entity.ItemPedidoCodigo;
import br.fiap.projeto.contexto.pedido.entity.Pedido;
import br.fiap.projeto.contexto.pedido.adapter.controller.rest.response.ItemPedidoDTO;
import br.fiap.projeto.contexto.pedido.adapter.controller.rest.request.PedidoCriarDTO;
import br.fiap.projeto.contexto.pedido.adapter.controller.rest.response.PedidoDTO;
import br.fiap.projeto.contexto.pedido.adapter.controller.rest.request.ProdutoPedidoDTO;
import br.fiap.projeto.contexto.pedido.entity.enums.OperacaoProduto;
import br.fiap.projeto.contexto.pedido.entity.enums.StatusPedido;
import br.fiap.projeto.contexto.pedido.usecase.exception.InvalidOperacaoProdutoException;
import br.fiap.projeto.contexto.pedido.usecase.exception.InvalidStatusException;
import br.fiap.projeto.contexto.pedido.usecase.exception.ItemNotFoundException;
import br.fiap.projeto.contexto.pedido.usecase.exception.NoItensException;
import br.fiap.projeto.contexto.pedido.usecase.port.IPedidoRepositoryAdapterGateway;
import br.fiap.projeto.contexto.pedido.usecase.port.PedidoService;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class DomainPedidoService implements PedidoService {
    private final IPedidoRepositoryAdapterGateway IPedidoRepositoryAdapterGateway;
    public DomainPedidoService(IPedidoRepositoryAdapterGateway IPedidoRepositoryAdapterGateway) {
        this.IPedidoRepositoryAdapterGateway = IPedidoRepositoryAdapterGateway;
    }
    //-------------------------------------------------------------------------//
    //                         BASE CRUD
    //-------------------------------------------------------------------------//
    @Override
    public PedidoDTO criaPedido(PedidoCriarDTO pedidoDTO) {
        return IPedidoRepositoryAdapterGateway.salvar(new Pedido(pedidoDTO)).toPedidoDTO();
    }
    @Override
    public PedidoDTO buscaPedido(UUID codigo) {
        return this.buscar(codigo).toPedidoDTO();
    }
    @Override
    public List<PedidoDTO> buscaTodos() {
        List<Pedido> pedidoLista = IPedidoRepositoryAdapterGateway.buscaTodos();
        return pedidoLista.stream().map(Pedido::toPedidoDTO).collect(Collectors.toList());
    }
    @Override
    public void removePedido(UUID codigo) {
        IPedidoRepositoryAdapterGateway.removePedido(codigo);
    }
    //-------------------------------------------------------------------------//
    //                MÉTODOS DE MANUPULAÇÃO DE ITENS DO PEDIDO
    //-------------------------------------------------------------------------//
    @Override
    public PedidoDTO adicionarProduto(UUID codigoPedido, ProdutoPedidoDTO produtoDTO) throws InvalidOperacaoProdutoException {
        Pedido pedido = this.buscar(codigoPedido);
        // TODO - Implementar busca correta do Produto
        ItemPedido itemPedido = new ItemPedido(
                new ItemPedidoCodigo(codigoPedido,
                    produtoDTO.getCodigo()),
                    pedido,
                    1,
                    produtoDTO.getNome(),
                    produtoDTO.getDescricao(),
                    produtoDTO.getPreco(),
                    produtoDTO.getCategoria(),
                    produtoDTO.getImagem(),
                    produtoDTO.getTempoPreparoMin());
        pedido.adicionarItem(itemPedido);
        this.atualizaValorTotal(pedido, itemPedido, OperacaoProduto.ADICIONAR);
        return IPedidoRepositoryAdapterGateway.salvar(pedido).toPedidoDTO();
    }
    @Transactional
    @Override
    public void removerProduto(UUID codigoPedido, UUID codigoProduto) throws InvalidOperacaoProdutoException {
        Pedido pedido = this.buscar(codigoPedido);
        ItemPedido itemPedido = this.getItemPedidoByProduto(codigoProduto,pedido.getItens());
        this.atualizaValorTotal(pedido, itemPedido, OperacaoProduto.REMOVER);
        pedido.getItens().remove(itemPedido);
        IPedidoRepositoryAdapterGateway.salvar(pedido);
    }
    @Override
    public PedidoDTO aumentarQuantidade(UUID codigoPedido, UUID codigoProduto) throws ItemNotFoundException, InvalidOperacaoProdutoException {
        Pedido pedido = this.buscar(codigoPedido);
        ItemPedido itemPedido = this.getItemPedidoByProduto(codigoProduto,pedido.getItens());
        if(itemPedido == null){
            throw new ItemNotFoundException("Item não encontrado na lista");
        }
        itemPedido.adicionarQuantidade();
        this.atualizaValorTotal(pedido, itemPedido, OperacaoProduto.ADICIONAR);
        return IPedidoRepositoryAdapterGateway.salvar(pedido).toPedidoDTO();
    }
    @Transactional
    @Override
    public PedidoDTO reduzirQuantidade(UUID codigoPedido, UUID codigoProduto) throws ItemNotFoundException, InvalidOperacaoProdutoException {
        Pedido pedido = this.buscar(codigoPedido);
        ItemPedido itemPedido = this.getItemPedidoByProduto(codigoProduto,pedido.getItens());
        if(itemPedido == null){
            throw new ItemNotFoundException("Item não encontrado na lista");
        }else{
            this.atualizaValorTotal(pedido, itemPedido, OperacaoProduto.SUBTRAIR);
            if(itemPedido.getQuantidade() <= 1){
                this.removerProduto(codigoPedido, codigoProduto);
            } else {
                itemPedido.reduzirQuantidade();
                return IPedidoRepositoryAdapterGateway.salvar(pedido).toPedidoDTO();
            }
        }
        return null;
    }
    //-------------------------------------------------------------------------//
    //                  RETORNO DOS PEDIDOS POR  STATUS
    //-------------------------------------------------------------------------//
    @Override
    public List<PedidoDTO> buscarTodosRecebido(){
        List<Pedido> pedidosRecebidos = IPedidoRepositoryAdapterGateway.buscaPedidosPorStatus(StatusPedido.RECEBIDO);
        return pedidosRecebidos.stream().map(Pedido::toPedidoDTO).collect(Collectors.toList());
    }
    @Override
    public List<PedidoDTO> buscarTodosPagos(){
        List<Pedido> pedidosRecebidos = IPedidoRepositoryAdapterGateway.buscaPedidosPorStatus(StatusPedido.PAGO);
        return pedidosRecebidos.stream().map(Pedido::toPedidoDTO).collect(Collectors.toList());
    }
    @Override
    public List<PedidoDTO> buscarTodosEmPreparacao(){
        List<Pedido> pedidosRecebidos = IPedidoRepositoryAdapterGateway.buscaPedidosPorStatus(StatusPedido.EM_PREPARACAO);
        return pedidosRecebidos.stream().map(Pedido::toPedidoDTO).collect(Collectors.toList());
    }
    @Override
    public List<PedidoDTO> buscarTodosPronto(){
        List<Pedido> pedidosRecebidos = IPedidoRepositoryAdapterGateway.buscaPedidosPorStatus(StatusPedido.PRONTO);
        return pedidosRecebidos.stream().map(Pedido::toPedidoDTO).collect(Collectors.toList());
    }
    @Override
    public List<PedidoDTO> buscarTodosFinalizado(){
        List<Pedido> pedidosRecebidos = IPedidoRepositoryAdapterGateway.buscaPedidosPorStatus(StatusPedido.FINALIZADO);
        return pedidosRecebidos.stream().map(Pedido::toPedidoDTO).collect(Collectors.toList());
    }
    //-------------------------------------------------------------------------//
    //                       MANIPULAÇÃO DE STATUS
    //-------------------------------------------------------------------------//
    @Override
    public PedidoDTO receber(UUID codigo) throws Exception {
        Pedido pedido = this.buscar(codigo);
        if(pedido.getStatus().equals(StatusPedido.INICIADO)){
            if(pedido.getItens().isEmpty()){
                throw new NoItensException(codigo.toString());
            }else {
                pedido.atualizarStatus(StatusPedido.RECEBIDO);
                // TODO - CHAMADA PARA PAGAMENTO
            }
        }else{
            throw new InvalidStatusException("Status inválido!");
        }
        return IPedidoRepositoryAdapterGateway.salvar(pedido).toPedidoDTO();
    }
    @Override
    public PedidoDTO pagar(UUID codigo) throws Exception {
        Pedido pedido = this.buscar(codigo);
        if(pedido.getStatus().equals(StatusPedido.RECEBIDO)){
            pedido.atualizarStatus(StatusPedido.PAGO);
        }else{
            throw new InvalidStatusException("Status inválido!");
        }
        return IPedidoRepositoryAdapterGateway.salvar(pedido).toPedidoDTO();
    }
    @Override
    public PedidoDTO preparar(UUID codigo) throws Exception {
        Pedido pedido = this.buscar(codigo);
        if(pedido.getStatus().equals(StatusPedido.PAGO)){
            pedido.atualizarStatus(StatusPedido.EM_PREPARACAO);
        }else{
            throw new InvalidStatusException("Status inválido!");
        }
        return IPedidoRepositoryAdapterGateway.salvar(pedido).toPedidoDTO();
    }
    @Override
    public PedidoDTO prontificar(UUID codigo) throws Exception {
        Pedido pedido = this.buscar(codigo);
        if(pedido.getStatus().equals(StatusPedido.EM_PREPARACAO)){
            pedido.atualizarStatus(StatusPedido.PRONTO);
        }else{
            throw new InvalidStatusException("Status inválido!");
        }
        return IPedidoRepositoryAdapterGateway.salvar(pedido).toPedidoDTO();
    }
    @Override
    public PedidoDTO finalizar(UUID codigo) throws Exception {
        Pedido pedido = this.buscar(codigo);
        if(pedido.getStatus().equals(StatusPedido.PRONTO)){
            pedido.atualizarStatus(StatusPedido.FINALIZADO);
        }else{
            throw new InvalidStatusException("Status inválido!");
        }
        return IPedidoRepositoryAdapterGateway.salvar(pedido).toPedidoDTO();
    }
    //-------------------------------------------------------------------------//
    //                          MÉTODOS AUXILIARES
    //-------------------------------------------------------------------------//
    private void atualizaValorTotal(Pedido pedido, ItemPedido itemPedido, OperacaoProduto operacao) throws InvalidOperacaoProdutoException {
        Double valor = pedido.getValorTotal();
        switch (operacao){
            case REMOVER:
                valor -= ( itemPedido.getValorUnitario() * itemPedido.getQuantidade() );
                break;
            case SUBTRAIR:
                valor -= itemPedido.getValorUnitario();
                break;
            case ADICIONAR:
                valor += itemPedido.getValorUnitario();
                break;
            default:
                throw new InvalidOperacaoProdutoException("Operação inválida!");
        }
        pedido.atualizarValorTotal(valor);
    }
    @Override
    public Integer calcularTempoTotalPreparo(UUID codigo) {
        PedidoDTO pedido = this.buscaPedido(codigo);
        Integer retorno = 0;

        //Foreach para percorrer os itens e calcular o valor total
        List<ItemPedido> listaItens = pedido.getItens();
        for (ItemPedido i : listaItens) {
            retorno += i.getTempoPreparoMin() * i.getQuantidade();
        }
        return retorno;
    }
    private ItemPedido getItemPedidoByProduto(UUID codigoProduto, List<ItemPedido> itemPedidos){
        return itemPedidos.stream()
                .filter(itemPedido -> itemPedido.getCodigo().getProdutoCodigo().equals(codigoProduto))
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca pedido pelo código e retorna um objeto de Pedido
     * Utilizar pora tratamentos internos somente para facilitar a busca minimizando a necessidade
     * de alterar o tipo de objeto
     * @param codigo - Codigo do pedido
     * @return retorna um objeto do tipo pedido da camada de domínio
     */
    private Pedido buscar(UUID codigo) {
        Optional<Pedido> optionalPedido = IPedidoRepositoryAdapterGateway.buscaPedido(codigo);
        optionalPedido.orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado!"));
        return optionalPedido.get();
    }
    @Override
    public List<ItemPedidoDTO> listarItens(UUID codigo) {
        return this.buscaPedido(codigo).getItens().stream().map(ItemPedido::toItemPedidoDTO).collect(Collectors.toList());
    }
}