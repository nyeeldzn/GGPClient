package DTO;

import models.Cliente;
import models.OrdemPedido;
import models.Produto;
import models.Usuario;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OrdemPedidoDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    private Long id;

    private Cliente cliente;

    private Usuario operador;

    private String entregador;

    private String forma_pagamento;

    private String entradaDate;

    private String entradaHora;

    private String triagemHora;

    private String checkoutHora;

    private String finalizadoHora;

    private String fonte_pedido;
    private String caixa_responsavel;

    private int status;
    //1 -- pendende
    //2 -- triagem
    //3 -- chekout
    //4 -- enviado
    //5 -- finalizado



    private List<Produto> produtos = new ArrayList<>();

    public OrdemPedidoDTO() {
    }

    public OrdemPedidoDTO(Long id, Cliente cliente, Usuario operador, String entregador, String forma_pagamento, String entradaDate, String entradaHora, String triagemHora, String checkoutHora, String finalizadoHora, String fonte_pedido, String caixa_responsavel, int status, List<Produto> produtos) {
        this.id = id;
        this.cliente = cliente;
        this.operador = operador;
        this.entregador = entregador;
        this.forma_pagamento = forma_pagamento;
        this.entradaDate = entradaDate;
        this.entradaHora = entradaHora;
        this.triagemHora = triagemHora;
        this.checkoutHora = checkoutHora;
        this.finalizadoHora = finalizadoHora;
        this.fonte_pedido = fonte_pedido;
        this.caixa_responsavel = caixa_responsavel;
        this.status = status;
        this.produtos = produtos;
    }

    private OrdemPedido toOrdemPedido(OrdemPedidoDTO pedidoDTO){
        OrdemPedido pedido = new OrdemPedido(pedidoDTO.getId(), pedidoDTO.getCliente(),
                pedidoDTO.getOperador(), pedidoDTO.entregador,pedidoDTO.getForma_pagamento(),
                pedidoDTO.fonte_pedido,pedidoDTO.getCaixa_responsavel(),pedidoDTO.getStatus());

        pedido.setEntradaDate(toDate(pedidoDTO.entradaDate));
        pedido.setEntradaHora(toTime(pedidoDTO.entradaHora));
        pedido.setTriagemHora(toTime(pedidoDTO.triagemHora));
        pedido.setCheckoutHora(toTime(pedidoDTO.checkoutHora));
        pedido.setFinalizadoHora(toTime(pedidoDTO.finalizadoHora));

        return pedido;
    }

    public Date toDate(String sDate){
        Date date = null;
        try {
            SimpleDateFormat fDate = new SimpleDateFormat("yyyy-MM-dd");
            date = fDate.parse(sDate);
        }catch (ParseException pe){
            pe.printStackTrace();
        }
        return date;
    }

    public Date toTime(String sTime){
        Date time = null;
        try {
            SimpleDateFormat fTime = new SimpleDateFormat("HH:mm:ss");
            time = fTime.parse(sTime);
        }catch (ParseException pe){
            pe.printStackTrace();
        }
        return time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getOperador() {
        return operador;
    }

    public void setOperador(Usuario operador) {
        this.operador = operador;
    }

    public String getEntregador() {
        return entregador;
    }

    public void setEntregador(String entregador) {
        this.entregador = entregador;
    }

    public String getForma_pagamento() {
        return forma_pagamento;
    }

    public void setForma_pagamento(String forma_pagamento) {
        this.forma_pagamento = forma_pagamento;
    }

    public String getEntradaDate() {
        return entradaDate;
    }

    public void setEntradaDate(String entradaDate) {
        this.entradaDate = entradaDate;
    }

    public String getEntradaHora() {
        return entradaHora;
    }

    public void setEntradaHora(String entradaHora) {
        this.entradaHora = entradaHora;
    }

    public String getTriagemHora() {
        return triagemHora;
    }

    public void setTriagemHora(String triagemHora) {
        this.triagemHora = triagemHora;
    }

    public String getCheckoutHora() {
        return checkoutHora;
    }

    public void setCheckoutHora(String checkoutHora) {
        this.checkoutHora = checkoutHora;
    }

    public String getFinalizadoHora() {
        return finalizadoHora;
    }

    public void setFinalizadoHora(String finalizadoHora) {
        this.finalizadoHora = finalizadoHora;
    }

    public String getFonte_pedido() {
        return fonte_pedido;
    }

    public void setFonte_pedido(String fonte_pedido) {
        this.fonte_pedido = fonte_pedido;
    }

    public String getCaixa_responsavel() {
        return caixa_responsavel;
    }

    public void setCaixa_responsavel(String caixa_responsavel) {
        this.caixa_responsavel = caixa_responsavel;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdemPedidoDTO that = (OrdemPedidoDTO) o;
        return status == that.status && Objects.equals(id, that.id) && Objects.equals(cliente, that.cliente) && Objects.equals(operador, that.operador) && Objects.equals(entregador, that.entregador) && Objects.equals(forma_pagamento, that.forma_pagamento) && Objects.equals(entradaDate, that.entradaDate) && Objects.equals(entradaHora, that.entradaHora) && Objects.equals(triagemHora, that.triagemHora) && Objects.equals(checkoutHora, that.checkoutHora) && Objects.equals(finalizadoHora, that.finalizadoHora) && Objects.equals(fonte_pedido, that.fonte_pedido) && Objects.equals(caixa_responsavel, that.caixa_responsavel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cliente, operador, entregador, forma_pagamento, entradaDate, entradaHora, triagemHora, checkoutHora, finalizadoHora, fonte_pedido, caixa_responsavel, status);
    }
}
