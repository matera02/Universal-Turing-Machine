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
public class Sigma {
    private Set<Character> sigma = new HashSet<>();
    
    public void aggiungiSimbolo(char simbolo){
        this.sigma.add(simbolo);
    }
    
    public void rimuoviSimbolo(char simbolo){
        this.sigma.remove(simbolo);
    }
    
    public boolean contieneSimbolo(char simbolo){
       return this.sigma.contains(simbolo);
    }
    
    
    public Set<Character> getSigma(){
        return this.sigma;
    }
    
}
