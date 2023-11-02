package com.example.article.tool;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


public class RestClient<T,V> {
    private String urlApiUtilisateur;

    private String urlApiToken = "http://localhost:8081/api/test";
    private RestTemplate template;
    private HttpHeaders httpHeaders;
    private HttpStatus httpStatus;

    public RestClient(String urlApiUtilisateur) {
        template = new RestTemplate();
        httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", "*/*");
        httpHeaders.add("content-type", "application/json");
        this.urlApiUtilisateur = urlApiUtilisateur;
    }

    public T get(String uri, Class<T> type) {
        HttpEntity<String> requestEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<T> response = template.exchange(urlApiUtilisateur+uri, HttpMethod.GET, requestEntity, type);
        return response.getBody();
    }

    public boolean testToken(String token, Class<T> type) {
        httpHeaders.add("Authorization", token);
        HttpEntity<String> requestEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<T> response = template.exchange(urlApiToken, HttpMethod.GET, requestEntity, type);
        System.out.println(response);
        if(response.hasBody()) {
            return response.getStatusCode().is2xxSuccessful();
        }
        return false;
    }
}
