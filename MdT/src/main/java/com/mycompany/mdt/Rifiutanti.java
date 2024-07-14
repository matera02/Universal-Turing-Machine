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
public class Rifiutanti {
    private Set<Stato> rifiutanti;
    private Q stati;
    
    public Rifiutanti(Q stati){
        this.rifiutanti = new HashSet<>();
        this.stati = stati;
    }
    
    //vale lo stesso ragionamento per gli stati accettanti
    public void aggiungiRifiutante(Stato stato){
        if(this.stati.contieneStato(stato)){
            stato.setRifiuto(true);
            if(stato.isRifiuto()){
                this.rifiutanti.add(stato);
            }
        }
    }
    
    public void rimuoviRifiutante(Stato stato){
        this.rifiutanti.remove(stato);
        stato.setRifiuto(false);
    }
    
    public boolean contieneRifiutante(Stato stato){
        return this.rifiutanti.contains(stato);
    }
    
}
