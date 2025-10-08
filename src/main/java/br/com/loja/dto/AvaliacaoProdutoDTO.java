package br.com.loja.dto;

import java.io.Serializable;

import br.com.loja.model.PessoaFisica;
import br.com.loja.model.PessoaJuridica;
import br.com.loja.model.Produto;

public class AvaliacaoProdutoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String descricao;
	
	private Integer nota;
	
	
	private Long empresa;

	
	private Long produto;

	private Long pessoa;
	
	

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public Integer getNota() {
		return nota;
	}


	public void setNota(Integer nota) {
		this.nota = nota;
	}


	public Long getEmpresa() {
		return empresa;
	}


	public void setEmpresa(Long empresa) {
		this.empresa = empresa;
	}


	public Long getProduto() {
		return produto;
	}


	public void setProduto(Long produto) {
		this.produto = produto;
	}


	public Long getPessoa() {
		return pessoa;
	}


	public void setPessoa(Long pessoa) {
		this.pessoa = pessoa;
	}


	
	
}
