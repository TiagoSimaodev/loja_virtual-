package br.com.loja.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.loja.ExceptionLojaVirtual;
import br.com.loja.dto.ConsultaFreteDTO;
import br.com.loja.dto.EmpresaTransporteDTO;
import br.com.loja.dto.EnvioEtiquetaDTO;
import br.com.loja.dto.ItemVendaLojaDTO;
import br.com.loja.dto.ProdutoDTO;
import br.com.loja.dto.VendaCompraLojaVirtualDTO;
import br.com.loja.enums.ApiTokenIntegracao;
import br.com.loja.model.Endereco;
import br.com.loja.model.ItemVendaLoja;
import br.com.loja.model.PessoaFisica;
import br.com.loja.model.StatusRastreio;
import br.com.loja.model.VendaCompraLojaVirtual;
import br.com.loja.repository.EnderecoRepository;
import br.com.loja.repository.NotaIFiscalVendaRepository;
import br.com.loja.repository.StatusRastreioRepository;
import br.com.loja.repository.Vd_Cp_loja_virtual_repository;
import br.com.loja.service.VendaService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController
public class Vd_Cp_Loja_Virt_Controller {

	@Autowired
	private Vd_Cp_loja_virtual_repository vd_Cp_loja_virtual_repository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private PessoaController pessoaController;

	@Autowired
	private NotaIFiscalVendaRepository notaIFiscalVendaRepository;
	
	@Autowired
	private StatusRastreioRepository statusRastreioRepository;
	
	@Autowired
	private VendaService vendaService;

	@ResponseBody
	@PostMapping(value = "**/salvarVendaLoja")
	public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendaLoja(
			@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws ExceptionLojaVirtual {

		vendaCompraLojaVirtual.getPessoa().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		PessoaFisica pessoaFisica = pessoaController.salvarPf(vendaCompraLojaVirtual.getPessoa()).getBody();
		vendaCompraLojaVirtual.setPessoa(pessoaFisica);

		vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(pessoaFisica);
		vendaCompraLojaVirtual.getEnderecoCobranca().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoCobranca());
		vendaCompraLojaVirtual.setEnderecoCobranca(enderecoCobranca);

		vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(pessoaFisica);
		vendaCompraLojaVirtual.getEnderecoEntrega().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);

		// associa a nota fiscal a empresa
		vendaCompraLojaVirtual.getNotaFiscalVenda().setEmpresa(vendaCompraLojaVirtual.getEmpresa());

		for (int i = 0; i < vendaCompraLojaVirtual.getItemVendaLojas().size(); i++) {
			vendaCompraLojaVirtual.getItemVendaLojas().get(i).setEmpresa(vendaCompraLojaVirtual.getEmpresa());
			vendaCompraLojaVirtual.getItemVendaLojas().get(i).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		}

		// salva primeiro a venda e todos os dados
		VendaCompraLojaVirtual salvarVendaLojaSalvo = vd_Cp_loja_virtual_repository
				.saveAndFlush(vendaCompraLojaVirtual);
		
		StatusRastreio statusRastreio = new StatusRastreio();
		statusRastreio.setCentroDistribuicao("Loja Local");
		statusRastreio.setCidade("local");
		statusRastreio.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		statusRastreio.setEstado("local");
		statusRastreio.setStatus("Inicio compra");
		statusRastreio.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		
		statusRastreioRepository.save(statusRastreio);
		
		// Associa a venda gravada no banco com a nota fiscal
		vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

		// Persiste novamente a nota fiscal novamente para ficar amarrada na venda
		notaIFiscalVendaRepository.saveAndFlush(vendaCompraLojaVirtual.getNotaFiscalVenda());
		salvarVendaLojaSalvo = vd_Cp_loja_virtual_repository.saveAndFlush(vendaCompraLojaVirtual);

		VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
		compraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
		compraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());

		compraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
		compraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
		compraLojaVirtualDTO.setValorDesc(vendaCompraLojaVirtual.getValorDesconto());
		compraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
		compraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());

		for (ItemVendaLoja item : vendaCompraLojaVirtual.getItemVendaLojas()) {
			ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();
			itemVendaLojaDTO.setQuantidade(item.getQuantidade());
			
			ProdutoDTO produtoDTO = new ProdutoDTO();
			
			produtoDTO.setId(item.getProduto().getId());
			produtoDTO.setNome(item.getProduto().getNome());
			produtoDTO.setDescricao(item.getProduto().getDescricao());
			produtoDTO.setTipoUnidade(item.getProduto().getTipoUnidade());
			produtoDTO.setValorVenda(item.getProduto().getValorVenda());
			
			
			itemVendaLojaDTO.setProduto(produtoDTO);

			compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);

		}

		return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.CREATED);

	}

	@ResponseBody
	@GetMapping(value = "**/consultaVendaId/{id}")
	public ResponseEntity<VendaCompraLojaVirtualDTO> consultaVendaId(@PathVariable("id") Long idVenda) {

		VendaCompraLojaVirtual compraLojaVirtual = vd_Cp_loja_virtual_repository.findByIdExclusao(idVenda);

		if (compraLojaVirtual == null) {
			compraLojaVirtual = new VendaCompraLojaVirtual();
		}
		

		VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

		compraLojaVirtualDTO.setValorTotal(compraLojaVirtual.getValorTotal());
		compraLojaVirtualDTO.setPessoa(compraLojaVirtual.getPessoa());

		compraLojaVirtualDTO.setCobranca(compraLojaVirtual.getEnderecoEntrega());
		compraLojaVirtualDTO.setEntrega(compraLojaVirtual.getEnderecoEntrega());
		compraLojaVirtualDTO.setValorDesc(compraLojaVirtual.getValorDesconto());
		compraLojaVirtualDTO.setValorFrete(compraLojaVirtualDTO.getValorFrete());
		compraLojaVirtualDTO.setId(compraLojaVirtual.getId());

		for (ItemVendaLoja item : compraLojaVirtual.getItemVendaLojas()) {
			ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();
			itemVendaLojaDTO.setQuantidade(item.getQuantidade());
			
			ProdutoDTO produtoDTO = new ProdutoDTO();
			
			produtoDTO.setId(item.getProduto().getId());
			produtoDTO.setDescricao(item.getProduto().getDescricao());
			produtoDTO.setNome(item.getProduto().getNome());
			produtoDTO.setTipoUnidade(item.getProduto().getTipoUnidade());
			produtoDTO.setValorVenda(item.getProduto().getValorVenda());
			
			
			itemVendaLojaDTO.setProduto(produtoDTO);

			compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);

		}

		return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.OK);

	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deleteVendaTotalBanco/{idVenda}")
	public ResponseEntity<String> deleteVendaTotalBanco(@PathVariable( value = "idVenda") Long idVenda) {
		vendaService.exclusaoTotalVendaBanco(idVenda);
		
		return new ResponseEntity<String>("Venda excluida com sucesso!",HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deleteVendaTotalBancoLogica/{idVenda}")
	public ResponseEntity<String> deleteVendaTotalBancoLogica(@PathVariable( value = "idVenda") Long idVenda) {
		vendaService.exclusaoTotalVendaBancoLogica(idVenda);
		
		return new ResponseEntity<String>("Venda excluida logicamente com sucesso!",HttpStatus.OK);
	}

	@ResponseBody
	@PutMapping(value = "**/ativaRegistroVendaBanco/{idVenda}")
	public ResponseEntity<String> ativaRegistroVendaBanco(@PathVariable( value = "idVenda") Long idVenda) {
		vendaService.ativaRegistroVendaBanco(idVenda);
		
		return new ResponseEntity<String>("Venda ativada com sucesso!",HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaVendaDinamicaFaixaData/{data1}/{data2}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> 
	consultaVendaDinamicaFaixaData(@PathVariable("data1") String data1, @PathVariable("data2") String data2) throws ParseException {
		
		List<VendaCompraLojaVirtual> compraLojaVirtual = null;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date d1 = dateFormat.parse(data1);
		Date d2 = dateFormat.parse(data2);
		
		compraLojaVirtual = vd_Cp_loja_virtual_repository.consultaVendaFaixaData(d1, d2);
		
		
		if (compraLojaVirtual == null) {
			compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}
		
			List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vel : compraLojaVirtual) {
			
		

			VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

			compraLojaVirtualDTO.setValorTotal(vel.getValorTotal());
			compraLojaVirtualDTO.setPessoa(vel.getPessoa());

			compraLojaVirtualDTO.setCobranca(vel.getEnderecoCobranca());
			compraLojaVirtualDTO.setEntrega(vel.getEnderecoEntrega());
			compraLojaVirtualDTO.setValorDesc(vel.getValorDesconto());
			compraLojaVirtualDTO.setValorFrete(vel.getValorFrete());
			compraLojaVirtualDTO.setId(vel.getId());

			for (ItemVendaLoja item : vel.getItemVendaLojas()) {
				ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();
				itemVendaLojaDTO.setQuantidade(item.getQuantidade());
			
				ProdutoDTO produtoDTO = new ProdutoDTO();
			
				produtoDTO.setId(item.getProduto().getId());
				produtoDTO.setDescricao(item.getProduto().getDescricao());
				produtoDTO.setNome(item.getProduto().getNome());
				produtoDTO.setTipoUnidade(item.getProduto().getTipoUnidade());
				produtoDTO.setValorVenda(item.getProduto().getValorVenda());
			
			
				itemVendaLojaDTO.setProduto(produtoDTO);

				compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);

					}
				compraLojaVirtualDTOList.add(compraLojaVirtualDTO);
				
			}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);

		
	}
	
	
	@ResponseBody
	@GetMapping(value = "**/consultaVendaDinamica/{valor}/{tipoconsulta}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> 
	consultaVendaDinamica(@PathVariable("valor") String valor, @PathVariable("tipoconsulta") String tipoconsulta) {

		List<VendaCompraLojaVirtual> compraLojaVirtual = null; 
		
		if (tipoconsulta.equalsIgnoreCase("POR_ID_PROD")) {
			compraLojaVirtual =vd_Cp_loja_virtual_repository.vendaPorProduto(Long.parseLong(valor));
		
		}else if (tipoconsulta.equalsIgnoreCase("POR_NOME_PROD")) {
			compraLojaVirtual = vd_Cp_loja_virtual_repository.vendaPorNomeProduto(valor.toUpperCase().trim());
		}else if (tipoconsulta.equalsIgnoreCase("POR_NOME_CLIENTE")) {
			compraLojaVirtual = vd_Cp_loja_virtual_repository.vendaPorNomeCliente(valor.toUpperCase().trim());
		}else if (tipoconsulta.equalsIgnoreCase("POR_NOME_ENDERECO_COBRANCA")) {
			compraLojaVirtual = vd_Cp_loja_virtual_repository.vendaPorEnderecoCobranca(valor.toUpperCase().trim());
		}else if (tipoconsulta.equalsIgnoreCase("POR_NOME_ENDERECO_ENTREGA")) {
			compraLojaVirtual = vd_Cp_loja_virtual_repository.vendaPorEnderecoEntrega(valor.toUpperCase().trim());
		}
		
		if (compraLojaVirtual == null) {
			compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}
		
		List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vel : compraLojaVirtual) {
			
		

			VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

			compraLojaVirtualDTO.setValorTotal(vel.getValorTotal());
			compraLojaVirtualDTO.setPessoa(vel.getPessoa());

			compraLojaVirtualDTO.setCobranca(vel.getEnderecoCobranca());
			compraLojaVirtualDTO.setEntrega(vel.getEnderecoEntrega());
			compraLojaVirtualDTO.setValorDesc(vel.getValorDesconto());
			compraLojaVirtualDTO.setValorFrete(vel.getValorFrete());
			compraLojaVirtualDTO.setId(vel.getId());

			for (ItemVendaLoja item : vel.getItemVendaLojas()) {
				ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();
				itemVendaLojaDTO.setQuantidade(item.getQuantidade());
			
				ProdutoDTO produtoDTO = new ProdutoDTO();
			
				produtoDTO.setId(item.getProduto().getId());
				produtoDTO.setDescricao(item.getProduto().getDescricao());
				produtoDTO.setNome(item.getProduto().getNome());
				produtoDTO.setTipoUnidade(item.getProduto().getTipoUnidade());
				produtoDTO.setValorVenda(item.getProduto().getValorVenda());
			
			
				itemVendaLojaDTO.setProduto(produtoDTO);

				compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);

					}
				compraLojaVirtualDTOList.add(compraLojaVirtualDTO);
				
			}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);

	}
	
	
	@ResponseBody
	@GetMapping(value = "**/vendaPorCliente/{idCliente}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> vendaPorCliente(@PathVariable("idCliente") Long idCliente) {

		List<VendaCompraLojaVirtual> compraLojaVirtual = vd_Cp_loja_virtual_repository.vendaPorCliente(idCliente);

		if (compraLojaVirtual == null) {
			compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}
		
		List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vel : compraLojaVirtual) {
			
		

			VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

			compraLojaVirtualDTO.setValorTotal(vel.getValorTotal());
			compraLojaVirtualDTO.setPessoa(vel.getPessoa());

			compraLojaVirtualDTO.setCobranca(vel.getEnderecoEntrega());
			compraLojaVirtualDTO.setEntrega(vel.getEnderecoEntrega());
			compraLojaVirtualDTO.setValorDesc(vel.getValorDesconto());
			compraLojaVirtualDTO.setValorFrete(vel.getValorFrete());
			compraLojaVirtualDTO.setId(vel.getId());

			for (ItemVendaLoja item : vel.getItemVendaLojas()) {
				ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();
				itemVendaLojaDTO.setQuantidade(item.getQuantidade());
			
				ProdutoDTO produtoDTO = new ProdutoDTO();
			
				produtoDTO.setId(item.getProduto().getId());
				produtoDTO.setDescricao(item.getProduto().getDescricao());
				produtoDTO.setNome(item.getProduto().getNome());
				produtoDTO.setTipoUnidade(item.getProduto().getTipoUnidade());
				produtoDTO.setValorVenda(item.getProduto().getValorVenda());
			
			
				itemVendaLojaDTO.setProduto(produtoDTO);

				compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);

					}
				compraLojaVirtualDTOList.add(compraLojaVirtualDTO);
				
			}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);

	}
	
	
	
	@ResponseBody
	@GetMapping(value = "**/consultaVendaPorProdutoId/{id}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaPorProdutoId(@PathVariable("id") Long idProd) {

		List<VendaCompraLojaVirtual> compraLojaVirtual = vd_Cp_loja_virtual_repository.vendaPorProduto(idProd);

		if (compraLojaVirtual == null) {
			compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}
		
		List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vel : compraLojaVirtual) {
			
		

			VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

			compraLojaVirtualDTO.setValorTotal(vel.getValorTotal());
			compraLojaVirtualDTO.setPessoa(vel.getPessoa());

			compraLojaVirtualDTO.setCobranca(vel.getEnderecoEntrega());
			compraLojaVirtualDTO.setEntrega(vel.getEnderecoEntrega());
			compraLojaVirtualDTO.setValorDesc(vel.getValorDesconto());
			compraLojaVirtualDTO.setValorFrete(vel.getValorFrete());
			compraLojaVirtualDTO.setId(vel.getId());

			for (ItemVendaLoja item : vel.getItemVendaLojas()) {
				ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();
				itemVendaLojaDTO.setQuantidade(item.getQuantidade());
			
				ProdutoDTO produtoDTO = new ProdutoDTO();
			
				produtoDTO.setId(item.getProduto().getId());
				produtoDTO.setDescricao(item.getProduto().getDescricao());
				produtoDTO.setNome(item.getProduto().getNome());
				produtoDTO.setTipoUnidade(item.getProduto().getTipoUnidade());
				produtoDTO.setValorVenda(item.getProduto().getValorVenda());
			
			
				itemVendaLojaDTO.setProduto(produtoDTO);

				compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);

					}
				compraLojaVirtualDTOList.add(compraLojaVirtualDTO);
				
			}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);

	}
	
	@ResponseBody
	@PostMapping(value = "**/imprimeCompraEtiquetaFrete")
	public ResponseEntity<String> imprimeCompraEtiquetaFrete(@RequestBody Long idVenda)  throws ExceptionLojaVirtual {
		
		VendaCompraLojaVirtual compraLojaVirtual = vd_Cp_loja_virtual_repository.findById(idVenda).orElseGet(null);
		
		if (compraLojaVirtual == null) {
			return new ResponseEntity<String>("Venda não encontrada", HttpStatus.OK);
		}
		
		EnvioEtiquetaDTO envioEtiquetaDTO = new EnvioEtiquetaDTO();
		
		envioEtiquetaDTO.setService(compraLojaVirtual.getServicoTransportadora());
		envioEtiquetaDTO.setAgency("49");
		envioEtiquetaDTO.getFrom().setName(compraLojaVirtual.getEmpresa().getNome());
		envioEtiquetaDTO.getFrom().setPhone(compraLojaVirtual.getEmpresa().getTelefone());
		envioEtiquetaDTO.getFrom().setEmail(compraLojaVirtual.getEmpresa().getEmail());
		envioEtiquetaDTO.getFrom().setCompany_document(compraLojaVirtual.getEmpresa().getCnpj());
		envioEtiquetaDTO.getFrom().setState_register(compraLojaVirtual.getEmpresa().getInscrEstadual());
		envioEtiquetaDTO.getFrom().setAddress(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getRuaLogra());
		envioEtiquetaDTO.getFrom().setComplement(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getComplemento());
		envioEtiquetaDTO.getFrom().setNumber(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getNumero());
		envioEtiquetaDTO.getFrom().setDistrict(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getEstado());
		envioEtiquetaDTO.getFrom().setCity(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getCidade());
		envioEtiquetaDTO.getFrom().setCountry_id(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getUf());
		envioEtiquetaDTO.getFrom().setPostal_code(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getCep());
		envioEtiquetaDTO.getFrom().setNote("Não há.");
		
		
		envioEtiquetaDTO.getTo().setName(compraLojaVirtual.getPessoa().getNome());
		
		return new ResponseEntity<String>("Sucesso", HttpStatus.OK);

		
	}
	
	
	
	@ResponseBody
	@PostMapping(value = "**/consultarFreteLojaVirtual")
	public ResponseEntity<List<EmpresaTransporteDTO>> 
	consultaFrete(@RequestBody @Valid ConsultaFreteDTO consultaFreteDTO) throws  Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(consultaFreteDTO);
		
		OkHttpClient cliente = new OkHttpClient().newBuilder()
				.build();
				MediaType mediaType = okhttp3.MediaType.parse("application/json");
				okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, json);
				okhttp3.Request request = new Request.Builder()
				.url (ApiTokenIntegracao.URL_MELHOR_ENVIO_SAND_BOX +"api/v2/me/shipment/calculate")
				.method("POST", body)
				.addHeader("Accept", "application/json")
				.addHeader("Content-Type","application/json")
				.addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
				.addHeader("User-Agent", "tiagosimaorodri123@gmail.com")
				.build();
				
				okhttp3.Response response = cliente.newCall(request).execute();
				
				JsonNode jsonNode = new ObjectMapper().readTree(response.body().string());
				 
				Iterator<JsonNode> iterator = jsonNode.iterator();
				 
				 List<EmpresaTransporteDTO> empresaTransporteDTOs = new ArrayList<EmpresaTransporteDTO>();
				 
				 while (iterator.hasNext()) {
					 JsonNode node = iterator.next();
					 
					 EmpresaTransporteDTO empresaTransporteDTO = new EmpresaTransporteDTO();
					 
					 if(node.get("id") != null) {
						 empresaTransporteDTO.setId(node.get("id").asText());
					 }
					 
					 if (node.get("name") != null) {
						 empresaTransporteDTO.setNome(node.get("name").asText());
					 }
					 
					 if (node.get("price") != null) {
						 empresaTransporteDTO.setValor(node.get("price").asText());
					 }
					 
					 if (node.get("company") != null ) {
						 empresaTransporteDTO.setEmpresa(node.get("company").get("name").asText());
						 empresaTransporteDTO.setPicture(node.get("company").get("picture").asText());
					 }
					 
					 if (empresaTransporteDTO.dadosOk()) {
						 empresaTransporteDTOs.add(empresaTransporteDTO);
					 }
					 
				 }
				 
				 
				 
				 return new ResponseEntity<List<EmpresaTransporteDTO>>(empresaTransporteDTOs, HttpStatus.OK);
		
	}
	

	
	
	
	
}
