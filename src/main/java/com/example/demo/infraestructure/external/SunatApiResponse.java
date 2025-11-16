package com.example.demo.infraestructure.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SunatApiResponse( String fullname) {
}
