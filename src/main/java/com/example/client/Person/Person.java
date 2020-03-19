package com.example.client.Person;

import com.fasterxml.jackson.annotation.*;
import org.slf4j.*;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

    private String id;
    private String name;
    private static final Logger log = LoggerFactory.getLogger(PersonController.class);

    public Person() {
    }

    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String formatToJSON(List<Person> personList) {
        String spacing = "    ";
        StringBuilder sb = new StringBuilder();

        log.info("Converting List<Person> into String");

        sb.append("[\n");
        for (Person p: personList) {
            sb.append(spacing + "{\n");
            sb.append(spacing + spacing + "\"id\": \"" + p.getId() + "\",\n");
            sb.append(spacing + spacing + "\"name\": \"" + p.getName() + "\"\n");
            sb.append(spacing + "}\n");
        }
        sb.append("]\n");

        return sb.toString();
    }
}