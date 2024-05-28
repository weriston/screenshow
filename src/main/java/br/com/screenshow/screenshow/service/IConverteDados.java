package br.com.screenshow.screenshow.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
