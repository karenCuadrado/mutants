package com.meli.challenge.services;

public interface MutantAnalyzerService {
    boolean isMutant(String[] dna);
    StatsDTO getStats();
}
