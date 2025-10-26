package br.com.loja.dto;

import br.com.loja.model.Produto;

public class ItemVendaLojaDTO {

	private Double quantidade;
	
	private ProdutoDTO produto;

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public ProdutoDTO getProduto() {
		return produto;
	}
	
	public void setProduto(ProdutoDTO produto) {
		this.produto = produto;
	}
	
	
}
