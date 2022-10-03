package io.davidarchanjo.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.davidarchanjo.dto.Person;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping
public class GreetingController {

    @SneakyThrows
    @RequestMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> processEvent(HttpServletRequest req) {
        var objectMapper = new ObjectMapper();
        var data = StreamUtils.copyToByteArray(req.getInputStream());
        var payload = objectMapper.readValue(data, new TypeReference<HashMap<String, String>>() {});
        if (payload.containsKey("firstName") && payload.containsKey("lastName")) {
            var person = objectMapper.convertValue(payload, Person.class);
            log.info("Received message: {}", person);
        }        
        return ResponseEntity.ok().build();
    }

}