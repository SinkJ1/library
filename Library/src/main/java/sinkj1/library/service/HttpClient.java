package sinkj1.library.service;


public interface HttpClient<T> {

    String get(String url);
    String post(String uri, T entity, String token);

}
