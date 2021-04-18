package com.meli.challenge.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.meli.challenge.TestConstants;
import com.meli.challenge.models.Dna;
import com.meli.challenge.repositories.MutantsRepository;

@TestPropertySource(locations = {"classpath:application.properties"})
@ContextConfiguration
@RunWith(SpringRunner.class)
public class MutantsAnalyzerService {

	@TestConfiguration
	static class DnaAnalyzerServiceTestContextConfiguration {

		@Bean
		public MutantAnalyzerService dnaService() {
			return new MutantAnalyzerServiceImplement();
		}
	}

	@Autowired
	private MutantAnalyzerService dnaService;

	@MockBean
	private MutantsRepository dnaRepository;

	@Test
	public void isMutantDna1() {
		assertTrue(dnaService.isMutant(TestConstants.MUTANT_DNA));
	}

	@Test
	public void isMutantDna2() {
		Dna dna = new Dna();
		dna.setSequence(TestConstants.MUTANT_DNA_2);
		dna.setIsMutant(true);
		when(dnaRepository.findBySequence(TestConstants.MUTANT_DNA_2))
				.thenReturn(dna);

		assertTrue(dnaService.isMutant(TestConstants.MUTANT_DNA_2));
	}

	@Test
	public void isHumanDna() {
		assertFalse(dnaService.isMutant(TestConstants.HUMAN_DNA));
	}

	@Test
	public void getStats() {
		assertNotNull(dnaService.getStats());
	}
}


