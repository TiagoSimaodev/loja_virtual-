package br.com.loja.dto;

import java.math.BigDecimal;

import br.com.loja.model.Pessoa;

public class VendaCompraLojaVirtualDTO {
	
	private BigDecimal valorTotal;
	
	private Pessoa pessoa;
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	
}
