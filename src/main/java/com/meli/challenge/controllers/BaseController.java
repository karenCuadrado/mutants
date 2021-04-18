package com.meli.challenge.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.challenge.services.MutantAnalyzerService;

public class BaseController {
	@Autowired
    protected MutantAnalyzerService mutantsService;
	@Autowired
    protected ObjectMapper objectMapper;
}