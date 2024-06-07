package com.meilleurtaux.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class CommuneController {

    @Autowired
    private CommuneService communeService;

    // Get list of communes matching a zipcode
    @GetMapping("/commune")
    public ResponseEntity<List<Commune>> commune(@RequestParam String zipcode) {

        // Check if zipcode is valid
        Pattern pattern = Pattern.compile("^[0-9]{5}$");
        Matcher match = pattern.matcher(zipcode);
        if (!match.matches())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, zipcode + " is not a valid zipcode");
        return new ResponseEntity<>(communeService.getCommuneByZipcode(zipcode), HttpStatus.OK);
    }

}
