package com.meli.challenge.repositories;

import org.springframework.data.repository.CrudRepository;
import com.meli.challenge.models.Dna;

public interface MutantsRepository extends CrudRepository<Dna, Integer> {
    Dna findBySequence(String[] sequence);
    long countAllByIsMutant(Boolean isMutant);
}