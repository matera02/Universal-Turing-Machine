/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mdt;

import java.util.Objects;

/**
 *
 * @author manuelrobertomatera
 */
public class Stato {
    
    private String etichetta;
    private boolean iniziale = false;
    private boolean accettazione = false;
    private boolean rifiuto = false;
    
    public Stato(String etichetta){
        this.etichetta = etichetta;
    }
    
    public void setEtichetta(String etichetta){
        this.etichetta = etichetta;
    }
    
    public String getEtichetta(){
        return this.etichetta;
    }
    
    //fai attenzione all'uso
    public void setIniziale(boolean iniziale){
        this.iniziale = iniziale;
    }
    
    public boolean isIniziale(){
        return this.iniziale;
    }
    
    public void setAccettazione(boolean accettazione){
        if(this.rifiuto == false)
            this.accettazione = accettazione;
    }
    
    public boolean isAccettazione(){
        return this.accettazione;
    }
    
    public void setRifiuto(boolean rifiuto){
        if(this.accettazione == false)
            this.rifiuto = rifiuto;
    }
    
    public boolean isRifiuto(){
        return this.rifiuto;
    }
    
    @Override
    public int hashCode(){
        return this.etichetta.hashCode();
    }
    
    //lo uso come codifica nella descrizione della mdt
    public String getBinaryCode(){
        return Integer.toBinaryString(this.hashCode());
    }

    //verifico l'uguaglianza solo per etichetta
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Stato other = (Stato) obj;
        return Objects.equals(this.etichetta, other.etichetta);
    }
    
    public static void main(String[] args){
        Stato q = new Stato("prova");
        System.out.println(q.getBinaryCode());
    }
}
