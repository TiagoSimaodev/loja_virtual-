package br.com.loja.dto;

import java.io.Serializable;

public class EnvioEtiquetaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String service;
	
	private String agency;
	
	private FromEnvioEtiquetaDTO from = new FromEnvioEtiquetaDTO();

	private ToEnvioEtiquetaDTO to = new ToEnvioEtiquetaDTO();
	
}
