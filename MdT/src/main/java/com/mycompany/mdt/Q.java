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
public class Q {
    private Set<Stato> stati = new HashSet<>();
    
    
    public void aggiungiStati(Stato... stati){
        for(Stato s : stati){
            this.aggiungiStato(s);
        }
    }
    
    //aggiungo lo stato se ha un'etichetta diversa
    public void aggiungiStato(Stato stato){
        for(Stato st : stati){
            if(st.equals(stato)){ //verifico l'uguaglianza solo per etichetta
                return;
            }
        }
        this.stati.add(stato);
    }
    
    public void rimuoviStato(Stato stato){
        this.stati.remove(stato);
    }
    
    public boolean contieneStato(Stato stato){
        return this.stati.contains(stato);
    }
    
    public Stato ottieniStato(String etichetta){
        for(Stato st : stati){
            if(st.getEtichetta().equals(etichetta)){
                return st;
            }
        }
        return null;
    }
}
