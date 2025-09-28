package br.com.loja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.model.Acesso;
import br.com.loja.model.MarcaProduto;

@Repository
@Transactional
public interface MarcaRepository extends JpaRepository<MarcaProduto, Long> {
	
	@Query("select m from MarcaProduto m where upper(trim(m.nomeDesc)) like %?1%")
	List<MarcaProduto> buscarMarcaDesc(String desc);
	
}
