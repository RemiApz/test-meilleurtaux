package com.meilleurtaux.restservice;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CommuneService {

    private final RestClient restClient;

    // init restClient
    public CommuneService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://geo.api.gouv.fr/communes").build();
    }

    // call external api with zipcode as parameter
    public List<Commune> getCommuneByZipcode(String zipcode) {
        List<Commune> communes = this.restClient.get()
                .uri("?codePostal={zipcode}", zipcode)
                .retrieve()
                // handle external api errors
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "geo.api.gouv.fr returned an error : " + response.getBody());
                })
                .body(new ParameterizedTypeReference<>() {
                });
        // handle no results
        if (CollectionUtils.isEmpty(communes)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find any commune with zipcode : " + zipcode);
        }
        return communes;
    }

}
