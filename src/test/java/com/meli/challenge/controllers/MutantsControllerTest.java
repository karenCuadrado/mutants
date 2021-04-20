package com.meli.challenge.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.meli.challenge.services.MutantAnalyzerService;
import com.meli.challenge.services.StatsDTO;
import com.meli.challenge.TestConstants;
import com.meli.challenge.models.Dna;

@RunWith(SpringRunner.class)
@WebMvcTest(MutantsController.class)
public class MutantsControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MutantAnalyzerService dnaAnalyzerService;

	@Test
	public void mutantPostOK() throws Exception {
		mockMvc.perform(post("/mutants/").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(TestConstants.MUTANT_DNA)));
	}

	@Test
	public void mutantResponseOK2() throws Exception {
		mockMvc.perform(post("/mutants/").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(TestConstants.MUTANT_DNA_2)));
	}

    @Test
	public void noMutantResponseForbidden() throws Exception {
	 	mockMvc.perform(post("/mutants/").contentType(MediaType.APPLICATION_JSON)
	 			.content(asJsonString(TestConstants.HUMAN_DNA)))
	 			.andExpect(status().isForbidden());
	 }

	 @Test
	 public void noMutantResponseForbidden2() throws Exception {
	 	mockMvc.perform(post("/mutants/").contentType(MediaType.APPLICATION_JSON)
	 			.content(asJsonString(TestConstants.HUMAN_DNA)))
	 			.andExpect(status().isForbidden());
	 }
	 
	 @Test
	 public void noMutantResponseForbidden3() throws Exception {
		Dna dna = new Dna(TestConstants.HUMAN_DNA,false); 
	 	mockMvc.perform(post("/mutants/").contentType(MediaType.APPLICATION_JSON)
	 			.content(asJsonString(dna.getSequence())))
	 			.andExpect(status().isForbidden());
	 }
	 
	 public void noMutantResponseForbidden4() throws Exception {
		Dna dna4 = new Dna();
		dna4.setIsMutant(false);
		dna4.setSequence(TestConstants.HUMAN_DNA);
	 	mockMvc.perform(post("/mutants/").contentType(MediaType.APPLICATION_JSON)
	 			.content(asJsonString(dna4.getSequence())))
	 			.andExpect(status().isForbidden());
	 }
    
	 @Test
	 public void wrongDNACharacterResponseBadRequest() throws Exception {
		 Dna dna5 = new Dna();
			dna5.setIsMutant(false);
			dna5.setSequence(TestConstants.WRONG_CHARACTERS_DNA);
	 	when(dnaAnalyzerService.isMutant(dna5.getSequence()))
	 			.thenThrow(new IllegalArgumentException("Invalid Sequence"));

	 	mockMvc.perform(post("/mutants/").contentType(MediaType.APPLICATION_JSON)
	 			.content(asJsonString(TestConstants.WRONG_CHARACTERS_DNA)))
	 			.andExpect(status().isBadRequest());
	 }
	 

	 @Test
	 public void wrongDNASizeResponseBadRequest() throws Exception {
	 	when(dnaAnalyzerService.isMutant(TestConstants.SMALL_DNA))
	 			.thenThrow(new IllegalArgumentException("Invalid Sequence"));

	 	mockMvc.perform(post("/mutants/").contentType(MediaType.APPLICATION_JSON)
	 			.content(asJsonString(TestConstants.SMALL_DNA)))
	 			.andExpect(status().isBadRequest());
	 }
	
	 @Test
	public void getStats() throws Exception {
		StatsDTO stats = new StatsDTO(1L, 1L);
		when(dnaAnalyzerService.getStats()).thenReturn(stats);

		mockMvc.perform(get("/stats/")).andExpect(status().isOk())
				.andExpect(content().json(asJsonString(stats)));

		verify(dnaAnalyzerService, times(1)).getStats();
		verifyNoMoreInteractions(dnaAnalyzerService);
	}

	@Test
	public void getNoStats() throws Exception {
		StatsDTO stats = new StatsDTO(0, 0);
		when(dnaAnalyzerService.getStats()).thenReturn(stats);
		mockMvc.perform(get("/stats/")).andExpect(status().isOk())
				.andExpect(content().json(asJsonString(stats)));

		verify(dnaAnalyzerService, times(1)).getStats();
		verifyNoMoreInteractions(dnaAnalyzerService);
	}

	private static String asJsonString(final Object obj) {
		try {
			return (new ObjectMapper()).writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}