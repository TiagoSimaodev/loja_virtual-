package br.com.loja.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConsultaFreteDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private FromDTO from;
	
	private ToDTO to;
	
	private List<ProductDTO> products = new ArrayList<ProductDTO>();

	public FromDTO getFrom() {
		return from;
	}

	public void setFrom(FromDTO from) {
		this.from = from;
	}

	public ToDTO getTo() {
		return to;
	}

	public void setTo(ToDTO to) {
		this.to = to;
	}

	public List<ProductDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDTO> products) {
		this.products = products;
	}
	
	
	
	
}
