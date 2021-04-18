package com.meli.challenge.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.meli.challenge.services.StatsDTO;


@Controller
@RequestMapping("/")
public class MutantsController extends BaseController{
	
	@RequestMapping(value="mutants", method=RequestMethod.POST)
	public ResponseEntity<String> isMutant(@RequestBody String[] dna) {
		HttpStatus status;
		String body;
		try {
			Boolean isMutant = mutantsService.isMutant(dna);
			if (isMutant) {
				status = HttpStatus.OK;
				body = "{\"message\": \"Welcome! Mutant Friend\"}";
			} else {
				status = HttpStatus.FORBIDDEN;
				body = "{\"message\": \"Sorry! only mutants allowed\"}";
			}
		} catch (IllegalArgumentException e) {
			status = HttpStatus.BAD_REQUEST;
			body = e.getMessage();
		}
		
		return ResponseEntity.status(status)
				.contentType(MediaType.APPLICATION_JSON).body(body);
	}
	
	@RequestMapping(value="stats", method=RequestMethod.GET)
    public ResponseEntity<String> GetStats() throws JsonProcessingException {
        StatsDTO stats = mutantsService.getStats();
        String body = objectMapper.writeValueAsString(stats);
        return ResponseEntity.ok(body);
    }
}
