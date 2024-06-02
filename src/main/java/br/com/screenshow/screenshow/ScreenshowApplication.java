package br.com.screenshow.screenshow;

import br.com.screenshow.screenshow.model.DadosEpisodio;
import br.com.screenshow.screenshow.model.DadosSerie;
import br.com.screenshow.screenshow.model.DadosTemporada;
import br.com.screenshow.screenshow.principal.Principal;
import br.com.screenshow.screenshow.service.ConsumoApi;
import br.com.screenshow.screenshow.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenshowApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenshowApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

			Principal principal = new Principal();
			principal.exibeMenu();
	}
}
