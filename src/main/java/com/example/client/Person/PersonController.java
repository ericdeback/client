package com.example.client.Person;

import org.slf4j.*;
import org.springframework.context.annotation.*;
import org.springframework.core.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;

import java.util.*;

@RestController
public class PersonController {
    RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(PersonController.class);

    @Bean
    public RestTemplate getRestTemplate() {
        this.restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(HttpStatus statusCode) {

                return false;
            }
        });
        return restTemplate;
    }

    public Person getPerson(String url) throws Exception {
        //"http://localhost:8081/api/v1/person/19efecbf-1243-4656-a461-58a61f9e5dd5"
        Person person = restTemplate.getForObject(url, Person.class);

        return person;
    }

     public  ResponseEntity<List<Person>> getPersons(String url) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity <String> entity = new HttpEntity<String>(headers);

        ResponseEntity<List<Person>> res = null;

         try {
            res = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Person>>() {});
        } catch (Exception ex) {
            log.error(ex.getStackTrace().toString());
        }

        return res;
     }

    public  ResponseEntity<String> postPersons(String url, Person person) {
        /* INFO
        HttpEntity represents an HTTP request or response entity, consisting of headers and body.
        Typically used in combination with the RestTemplate
        */

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Person> entity = new HttpEntity<Person>(person, headers);

        ResponseEntity<String> res = null;

        try {
            res = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (Exception ex) {
            log.error(ex.getStackTrace().toString());
        }

        return res;
    }

    public  ResponseEntity<String> putPersons(String url, Person person) {
        /*
        HttpEntity represents an HTTP request or response entity, consisting of headers and body.
        Typically used in combination with the RestTemplate
        */

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Person> entity = new HttpEntity<Person>(person, headers);

        ResponseEntity<String> res = null;

        try {
            res = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        } catch (Exception ex) {
            log.error(ex.getStackTrace().toString());
        }

        return res;
    }

    public ResponseEntity<String> test(String url, String cookie){

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Set-Cookie", cookie);

        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);

        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
    }


}
