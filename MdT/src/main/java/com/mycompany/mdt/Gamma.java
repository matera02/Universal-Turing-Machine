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
public class Gamma {
    //come blank considero il carattere spazio
    private static final char BLANK = '\u0020';
    
    private Set<Character> gamma = new HashSet<>();
    
    //Sigma è un sottoinsieme di Gamma
    public Gamma(Sigma sigma){
        
        for(char c : sigma.getSigma()){
            this.aggiungiSimbolo(c);
        }
        
        this.aggiungiSimbolo(BLANK);
    }
    
    public void aggiungiSimbolo(char simbolo){
        this.gamma.add(simbolo);
    }
    
    public void rimuoviSimbolo(char simbolo){
        this.gamma.remove(simbolo);
    }
    
    public boolean contieneSimbolo(char simbolo){
        return this.gamma.contains(simbolo);
    }
    
    //mi serve per la codifica della mdt
    public String getCodificaSimbolo(char simbolo){
        if(this.contieneSimbolo(simbolo)){
            return Integer.toBinaryString(Integer.valueOf(simbolo));
        }
        return null;
    }
    
    public static final char getBlank(){
        return Gamma.BLANK;
    }
}
