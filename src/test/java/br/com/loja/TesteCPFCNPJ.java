package br.com.loja;

import br.com.loja.util.ValidaCNPJ;
import br.com.loja.util.ValidaCPF;

public class TesteCPFCNPJ {

	public static void main(String[] args) {
	boolean isCnpj =	ValidaCNPJ.isCNPJ("10.640.974/0001-93");
	
	System.out.println("Cnpj válido: " + isCnpj);
	
	
	boolean isCpf = ValidaCPF.isCPF("443.139.590-33");

	System.out.println("CPF válido: " + isCpf);
	
	}
	
	
	
	
	
	
}
