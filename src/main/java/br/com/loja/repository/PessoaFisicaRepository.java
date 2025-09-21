package br.com.loja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.loja.model.PessoaFisica;
import br.com.loja.model.PessoaJuridica;

@Repository
public interface PessoaFisicaRepository extends CrudRepository<PessoaFisica, Long> {
	
	@Query(value = "select pf from PessoaFisica pf where upper(trim(pf.nome)) like %?1%" )
	public List<PessoaFisica> pesquisaPorNomePf(String nome);
	
	
	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1" )
	public List<PessoaFisica> pesquisaPorCpfPf(String cpf);
	
	
	
	
}
