package sinkj1.library.service;


public interface HttpClient<T> {

    String get(String url);
    String post(String url, T entity, String header);

}
