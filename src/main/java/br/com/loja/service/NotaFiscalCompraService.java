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
		
		String sql = "select p.id as codigoProduto, p.nome as nomeProduto, "
				+ " p.valor_venda as valorVendaProduto, ntp.quantidade as quantidadeComprada, "
				+ " pj.id as codigoFornecedor, pj.nome as nomeFornecedor,cfc.data_compra as dataCompra "
				+ " from nota_fiscal_compra as cfc "
				+ " inner join nota_item_produto as ntp on cfc.id = nota_fiscal_compra_id  "
				+ " inner join produto as p on p.id = ntp.produto_id "
				+ " inner join pessoa_juridica as pj on pj.id = cfc.pessoa_id where  ";
		
		
		sql += " cfc.data_compra >='"+objetoRequisicaoRelatorioProdCompraNotaFiscaldto.getDataInicial()+"' and  ";
		sql += " cfc.data_compra <= '"+objetoRequisicaoRelatorioProdCompraNotaFiscaldto.getDataFinal()+"' ";
		
		if(!objetoRequisicaoRelatorioProdCompraNotaFiscaldto.getCodigoNota().isEmpty()) {
			sql += " and cfc.id = " + objetoRequisicaoRelatorioProdCompraNotaFiscaldto.getCodigoNota() + " ";
		
		}
		
		if (!objetoRequisicaoRelatorioProdCompraNotaFiscaldto.getCodigoProduto().isEmpty()) {
			sql += " and p.id = " + objetoRequisicaoRelatorioProdCompraNotaFiscaldto.getCodigoProduto() + " ";
		}
		
		if (!objetoRequisicaoRelatorioProdCompraNotaFiscaldto.getNomeProduto().isEmpty()) {
			sql += "upper(p.nome) like upper('%"+objetoRequisicaoRelatorioProdCompraNotaFiscaldto.getNomeProduto()+"')";
		}
		
		if (!objetoRequisicaoRelatorioProdCompraNotaFiscaldto.getNomeFornecedor().isEmpty()) {
			sql += "upper(pj.nome) like upper('%"+objetoRequisicaoRelatorioProdCompraNotaFiscaldto.getNomeFornecedor()+"')";

		}
		
		
		retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO.class));
		
		return retorno;
	}

}
