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

@RunWith(SpringRunner.class)
@WebMvcTest(MutantsController.class)
public class MutantsControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MutantAnalyzerService dnaAnalyzerService;

	@Test
	public void mutantPostOK() throws Exception {
		mockMvc.perform(post("/mutant/").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(TestConstants.MUTANT_DNA)));
	}

	@Test
	public void mutantResponseOK2() throws Exception {
		mockMvc.perform(post("/mutant/").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(TestConstants.MUTANT_DNA_2)));
	}

	@Test
	public void noMutantResponseForbidden() throws Exception {
		mockMvc.perform(post("/mutant/").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(TestConstants.HUMAN_DNA)))
				.andExpect(status().isForbidden());
	}

	@Test
	public void noMutantResponseForbidden2() throws Exception {
		mockMvc.perform(post("/mutant/").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(TestConstants.HUMAN_DNA)))
				.andExpect(status().isForbidden());
	}

	@Test
	public void wrongDNACharacterResponseBadRequest() throws Exception {
		when(dnaAnalyzerService.isMutant(TestConstants.WRONG_CHARACTERS_DNA))
				.thenThrow(new IllegalArgumentException("Invalid Sequence"));

		mockMvc.perform(post("/mutant/").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(TestConstants.WRONG_CHARACTERS_DNA)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void wrongDNASizeResponseBadRequest() throws Exception {
		when(dnaAnalyzerService.isMutant(TestConstants.SMALL_DNA))
				.thenThrow(new IllegalArgumentException("Invalid Sequence"));

		mockMvc.perform(post("/mutant/").contentType(MediaType.APPLICATION_JSON)
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