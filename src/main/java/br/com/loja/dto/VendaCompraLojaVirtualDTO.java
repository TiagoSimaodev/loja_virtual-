package br.com.loja.dto;

import java.math.BigDecimal;

import br.com.loja.model.Endereco;
import br.com.loja.model.Pessoa;

public class VendaCompraLojaVirtualDTO {
	
	private BigDecimal valorTotal;
	
	private Pessoa pessoa;
	
	private Endereco cobranca;
	
	private Endereco entrega;

	
	public Endereco getCobranca() {
		return cobranca;
	}

	public void setCobranca(Endereco cobranca) {
		this.cobranca = cobranca;
	}

	public Endereco getEntrega() {
		return entrega;
	}

	public void setEntrega(Endereco entrega) {
		this.entrega = entrega;
	}

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
