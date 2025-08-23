package br.com.loja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "br.com.loja.model")
public class LojaVirtualApplication {

	public static void main(String[] args) {
		SpringApplication.run(LojaVirtualApplication.class, args);
	}

}
