package br.com.loja.dto;

import br.com.loja.model.NotaFiscalVenda;

public class NotaFiscalVendaDTO {

	private Long id;
    private String numero;
    private String serie;
    private String tipo;
    private Long idVenda;
    private Long idEmpresa;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Long getIdVenda() {
		return idVenda;
	}
	public void setIdVenda(Long idVenda) {
		this.idVenda = idVenda;
	}
	public Long getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	
	public static NotaFiscalVendaDTO fromEntity(NotaFiscalVenda entity) {
        NotaFiscalVendaDTO dto = new NotaFiscalVendaDTO();
        dto.setId(entity.getId());
        dto.setNumero(entity.getNumero());
        dto.setSerie(entity.getSerie());
        dto.setTipo(entity.getTipo());
        dto.setIdVenda(entity.getVendaCompraLojaVirtual().getId());
        dto.setIdEmpresa(entity.getEmpresa().getId());
        return dto;
    }
    
    
	
}
