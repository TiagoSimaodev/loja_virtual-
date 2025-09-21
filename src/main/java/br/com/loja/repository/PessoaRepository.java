package br.com.loja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.loja.model.PessoaFisica;
import br.com.loja.model.PessoaJuridica;

@Repository
public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long> {

	@Query(value = "select pj from PessoaJuridica pj where upper(trim(pj.nome)) like %?1%" )
	public List<PessoaJuridica> pesquisaPorNomePj(String nome);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1" )
	public PessoaJuridica existeCnpjCadastrado(String cnpj);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1" )
	public List<PessoaJuridica>existeCnpjCadastradoList(String cnpj);
	
	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1" )
	public PessoaFisica existeCpfCadastrado(String cpf);
	
	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1" )
	public List<PessoaFisica> existeCpfCadastradoList(String cpf);
	
	
	@Query(value = "select pj from PessoaJuridica pj where pj.inscrEstadual = ?1" )
	public PessoaJuridica existeInscEstadualCadastrado(String inscrEstadual);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.inscrEstadual = ?1" )
	public List<PessoaJuridica>existeInscEstadualCadastradoList(String inscrEstadual);
	
}
