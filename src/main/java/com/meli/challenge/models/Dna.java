package com.meli.challenge.models;

import java.util.Arrays;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dna") 
public class Dna {

    private static final String PATTERN_BASES_NITROGENADAS = "^[AGTC]+$";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String[] sequence;
    private Boolean isMutant;

    public Dna() {
    }

    public Dna(String[] sequence, boolean isMutant) {
        if (!validateSequence(sequence))
            throw new IllegalArgumentException("Invalid Sequence");

        this.sequence = sequence;
        this.isMutant = isMutant;
    }

    private boolean validateSequence(String[] sequence) {
        Stream<String> dnaStream = Arrays.stream(sequence);
        return dnaStream.allMatch(s -> s.length() >= 4 && s.matches(PATTERN_BASES_NITROGENADAS));
    }
    
    
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String[] getSequence() {
		return sequence;
	}
	public void setSequence(String[] sequence) {
		this.sequence = sequence;
	}
	public Boolean getIsMutant() {
		return isMutant;
	}
	public void setIsMutant(Boolean isMutant) {
		this.isMutant = isMutant;
	}
    
}
