package br.com.loja.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import br.com.loja.dto.ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO;

@Service
public class NotaFiscalCompraService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO> gerarRelatorioProdCompraNota(
			ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO objetoRequisicaoRelatorioProdCompraNotaFiscaldto) {

		List<ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO> retorno = new ArrayList<ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO>();
		
		String sql = "";
		
		retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO.class));
		
		return null;
	}

}
