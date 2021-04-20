package com.meli.challenge.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meli.challenge.models.Dna;
import com.meli.challenge.repositories.MutantsRepository;

@Service
public class MutantAnalyzerServiceImplement implements MutantAnalyzerService{
    @Autowired
    protected MutantsRepository mutantsRepository;
	private static final String[] MUTANT_DNA_SEQUENCES = { "AAAA", "CCCC", "GGGG", "TTTT" };
    private static int tamanio_dna = MUTANT_DNA_SEQUENCES[1].length()-1;
    
    @Override
    public boolean isMutant(String[] dnaString) {
        Dna dna = mutantsRepository.findBySequence(dnaString);
        if (dna != null)
            return dna.getIsMutant();

        dna = new Dna(dnaString, false);

        int count = 0;
        ArrayList<String> sequence = reorganizeDnaSequence(dna);
        for (int i = 0; i < sequence.size(); i++) {
            for (String mutantDnaSequence : MUTANT_DNA_SEQUENCES) {
                if(sequence.get(i).indexOf(mutantDnaSequence)!=-1)
                    count ++;
                if (count >= 2) {
                    dna.setIsMutant(true);
                    break;
                }
            }
        }
        mutantsRepository.save(dna);
        return dna.getIsMutant();
    }

    private static ArrayList<String> reorganizeDnaSequence(Dna dna) {
        ArrayList<String> sequenceReorganized = new ArrayList<>();
        String[] sequence = dna.getSequence();
        // Filas
        for (String s : sequence) {
            sequenceReorganized.add(s);
        }
        // Columnas
        for (int row = 0; row < sequence.length; row++) {
            StringBuffer strColumn = new StringBuffer(sequence.length);
            for (int column = 0; column < sequence.length; column++) {
                strColumn.append(sequence[column].charAt(row));
            }
            sequenceReorganized.add(strColumn.toString());
        }
        // Oblicuo
        int recorrido = sequence.length - tamanio_dna;
        for (int i = 0; i < recorrido; i++) {
            StringBuffer obliqueDna1 = new StringBuffer(sequence.length);
            StringBuffer obliqueDna2 = new StringBuffer(sequence.length);
            for (int j = 0; j < recorrido; j++) {
                obliqueDna1.append(sequence[j].charAt(j + i));
                if (i != 0) {
                    obliqueDna2.append(sequence[i + j].charAt(j));
                }
            }
            if (obliqueDna1.length() > 0) {
                sequenceReorganized.add(obliqueDna1.toString());
            }
            if (obliqueDna2.length() > 0) {
                sequenceReorganized.add(obliqueDna2.toString());
            }
        }
        return sequenceReorganized;
    }
    
    public StatsDTO getStats() {
        long countMutants = mutantsRepository.countAllByIsMutant(true);
        long countHuman = mutantsRepository.countAllByIsMutant(false);
        StatsDTO stats = new StatsDTO(countMutants, countHuman);
        return stats;
    }

}
