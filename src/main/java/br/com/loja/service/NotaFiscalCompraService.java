package br.com.loja.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import br.com.loja.dto.ObjetoRequisicaoRelatorioProdAlertaEstoqueDTO;
import br.com.loja.dto.ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO;

@Service
public class NotaFiscalCompraService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	/** 
	 * Title: Histórico de compras de produtos para a loja virtual  / relatorio.
	 * Este relatório permite saber os produtos comprados para serem vendido pela loja virtual, todos os produtos tem relação com 
	 * a nota fiscal de compra/venda 
	 * @param objetoRequisicaoRelatorioProdCompraNotaFiscaldto
	 * @param dataInicio e dataFinal são parametros obrigatórios.
	 * @return List<ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO>
	 */
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
	
	/**
	 * Este relatório retorna os produtos que estão com estoque menor ou igual a quantidade definida no campo de qtde_alerta_estoque.
	 * @param alertaEstoque
	 * @return
	 */
	public List<ObjetoRequisicaoRelatorioProdAlertaEstoqueDTO> gerarRelatorioAlertaEstoque(ObjetoRequisicaoRelatorioProdAlertaEstoqueDTO  alertaEstoque) {
		
		
		List<ObjetoRequisicaoRelatorioProdAlertaEstoqueDTO> retorno = new ArrayList<ObjetoRequisicaoRelatorioProdAlertaEstoqueDTO>();
		
		String sql = "select p.id as codigoProduto, p.nome as nomeProduto, "
				+ " p.valor_venda as valorVendaProduto, ntp.quantidade as quantidadeComprada, "
				+ " pj.id as codigoFornecedor, pj.nome as nomeFornecedor,cfc.data_compra as dataCompra, "
				+ " p.qtd_estoque as qtdEstoque, p.qtde_alerta_estoque as qtdAlertaEstoque "
				+ " from nota_fiscal_compra as cfc "
				+ " inner join nota_item_produto as ntp on cfc.id = nota_fiscal_compra_id  "
				+ " inner join produto as p on p.id = ntp.produto_id "
				+ " inner join pessoa_juridica as pj on pj.id = cfc.pessoa_id where  ";
		
		
		sql += " cfc.data_compra >='"+alertaEstoque.getDataInicial()+"' and  ";
		sql += " cfc.data_compra <= '"+alertaEstoque.getDataFinal()+"' ";
		sql += " and p.alerta_qtde_estoque = true and p.qtd_estoque <= p.qtde_alerta_estoque ";
		
		
		if(!alertaEstoque.getCodigoNota().isEmpty()) {
			sql += " and cfc.id = " + alertaEstoque.getCodigoNota() + " ";
		
		}
		
		if (!alertaEstoque.getCodigoProduto().isEmpty()) {
			sql += " and p.id = " + alertaEstoque.getCodigoProduto() + " ";
		}
		
		if (!alertaEstoque.getNomeProduto().isEmpty()) {
			sql += "upper(p.nome) like upper('%"+alertaEstoque.getNomeProduto()+"')";
		}
		
		if (!alertaEstoque.getNomeFornecedor().isEmpty()) {
			sql += "upper(pj.nome) like upper('%"+alertaEstoque.getNomeFornecedor()+"')";

		}
		
		
		retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(ObjetoRequisicaoRelatorioProdAlertaEstoqueDTO.class));
		
		return retorno;
		
	}
	
	
	

}
