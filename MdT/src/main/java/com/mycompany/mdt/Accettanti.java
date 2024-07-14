/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mdt;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author manuelrobertomatera
 */
public class Accettanti {
    private static final char SEPARATORE_ELEMENTI = '\u002D'; // rappresenta il simbolo -
    private Set<Stato> accettanti;
    private Q stati;
    
    public Accettanti(Q stati){
        this.accettanti = new HashSet<>();
        this.stati = stati;
    }
    
    public void aggiungiAccettante(Stato stato){
        if(this.stati.contieneStato(stato)){
            stato.setAccettazione(true);
            //potrebbe essere uno stato di rifiuto e nella classe stato controllo se non è uno stato di rifiuto
            //per questo mi conviene controllare se il valore di accettazione sia stato settato correttamente
            if(stato.isAccettazione()){
                this.accettanti.add(stato);
            }
        }
    }
    
    public void rimuoviAccettante(Stato stato){
        this.accettanti.remove(stato);
        stato.setAccettazione(false);
    }
    
    
    public boolean contieneAccettante(Stato stato){
        return this.accettanti.contains(stato);
    }
    
    
    public String getCodificaAccettanti(){
        String s = new String();
        int i = 0;
        for(Stato q : this.accettanti){
            s += q.getBinaryCode();
            if(i < this.accettanti.size() - 1) s+= SEPARATORE_ELEMENTI;
            ++i;
        }
        return s;
    }
    
    
}
