package br.com.screenshow.screenshow.principal;

import br.com.screenshow.screenshow.model.DadosEpisodio;
import br.com.screenshow.screenshow.model.DadosSerie;
import br.com.screenshow.screenshow.model.DadosTemporada;
import br.com.screenshow.screenshow.model.Episodio;
import br.com.screenshow.screenshow.service.ConsumoApi;
import br.com.screenshow.screenshow.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private final Scanner leitura = new Scanner(System.in);

    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=b776c8d7";

    public void exibeMenu(){
        System.out.println("digite o nome da serie: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        ConverteDados conversor = new ConverteDados();
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i<=dados.totalTemporadas(); i++){
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") +"&Season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);

        // função lambda
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())) );

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());
        //      .toList();

//        System.out.println("Top 10 episodios: ");
//        dadosEpisodios.add(new DadosEpisodio("Teste",1,"10","2020-01-01"));
//        dadosEpisodios
//                .stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Todos que não sao N/A " + e))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .peek(e -> System.out.println("ORDENAÇÃO " + e))
//                .limit(10)
//                .map(e -> e.titulo().toUpperCase())
//                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> {
                            return new Episodio(t.numero(), d);
                        })
                ).collect(Collectors.toList());
            episodios.forEach(System.out::println);

//        System.out.println("Digite o trecho do epsodio: ");
//            var trechoTitulo = leitura.nextLine();
//            Optional<Episodio> episodioBuscado = episodios.stream()
//                    .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                    .findFirst();
//            if(episodioBuscado.isPresent()){
//                System.out.println("Episódio encontrado!");
//                System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
//            }else{
//                System.out.println("Informação do Epsódio não encontrada!");
//            }


//        System.out.println("A partir de que ano deseja ver o episodio?");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano,1,1);
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                            "Temporada: " + e.getTemporada() +
//                            "Episodio: " + e.getTitulo() +
//                            "Data Lançamento: " + e.getDataLancamento().format(formatador)
//                        ));

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("Média: "+ est.getAverage());
        System.out.println("Melhor Episódio: "+ est.getMax());
        System.out.println("Pior Episódio: "+ est.getMin());
        System.out.println("Quantidade: "+ est.getCount());



    }

}

//        for(int i =0; i < dados.totalTemporadas(); i++ ){
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for(int j = 0; j< episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }


//        List<String> nomes = Arrays.asList("Joao","Maria","Pedrinho","Joana");
//        nomes.stream()
//                .sorted()
//                .limit(3)
//                .filter(n -> n.startsWith("M"))
//                .map(n -> n.toUpperCase())
//                .forEach(System.out::println);
