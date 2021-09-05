package models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OrdemPedido implements Serializable {
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

    public OrdemPedido() {
    }

    public OrdemPedido(Long id, Cliente cliente, Usuario operador, String entregador, String forma_pagamento,
                       String fonte_pedido, String caixa_responsavel, int status) {
        this.id = id;
        this.cliente = cliente;
        this.operador = operador;
        this.entregador = entregador;
        this.forma_pagamento = forma_pagamento;
        this.fonte_pedido = fonte_pedido;
        this.caixa_responsavel = caixa_responsavel;
        this.status = status;
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

    public String getForma_pagamento() {
        return forma_pagamento;
    }

    public void setForma_pagamento(String forma_pagamento) {
        this.forma_pagamento = forma_pagamento;
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

    public String getFonte_pedido() {
        return fonte_pedido;
    }

    public void setFonte_pedido(String fonte_pedido) {
        this.fonte_pedido = fonte_pedido;
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

    public String getCaixa_responsavel() {
        return caixa_responsavel;
    }

    public void setCaixa_responsavel(String caixa_responsavel) {
        this.caixa_responsavel = caixa_responsavel;
    }

    public Date getEntradaDate() {
        Date strr = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            strr = format.parse(entradaDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return strr;
    }

    public void setEntradaDate(Date entradaDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.entradaDate = format.format(entradaDate);
    }

    public Date getEntradaHora() {
        Date strr = null;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            strr = format.parse(entradaHora);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return strr;
    }

    public void setEntradaHora(Date entradaHora) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        this.entradaHora = format.format(entradaHora);
    }

    public Date getTriagemHora() {
        Date strr = null;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            strr = format.parse(triagemHora);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return strr;
    }

    public void setTriagemHora(Date triagemHora) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        this.triagemHora = format.format(triagemHora);
    }

    public Date getCheckoutHora() {
        Date strr = null;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            strr = format.parse(checkoutHora);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return strr;
    }

    public void setCheckoutHora(Date checkoutHora) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        this.checkoutHora = format.format(checkoutHora);
    }

    public Date getFinalizadoHora() {
        Date strr = null;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            strr = format.parse(finalizadoHora);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return strr;
    }

    public void setFinalizadoHora(Date finalizadoHora) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        this.finalizadoHora = format.format(finalizadoHora);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdemPedido that = (OrdemPedido) o;
        return status == that.status && Objects.equals(id, that.id) && Objects.equals(cliente, that.cliente) && Objects.equals(operador, that.operador) && Objects.equals(entregador, that.entregador) && Objects.equals(forma_pagamento, that.forma_pagamento) && Objects.equals(entradaDate, that.entradaDate) && Objects.equals(entradaHora, that.entradaHora) && Objects.equals(triagemHora, that.triagemHora) && Objects.equals(checkoutHora, that.checkoutHora) && Objects.equals(finalizadoHora, that.finalizadoHora) && Objects.equals(fonte_pedido, that.fonte_pedido) && Objects.equals(caixa_responsavel, that.caixa_responsavel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cliente, operador, entregador, forma_pagamento, entradaDate, entradaHora, triagemHora, checkoutHora, finalizadoHora, fonte_pedido, caixa_responsavel, status);
    }
}