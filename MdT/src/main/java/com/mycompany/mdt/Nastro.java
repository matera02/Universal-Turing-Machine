/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mdt;

/**
 *
 * @author manuelrobertomatera
 */
public class Nastro {
    
    private Gamma gamma;
    
    private Cella cellaCorrente;
    
    private class Cella{
        
        private char carattere;
        
        private Cella successiva = null;
        
        private Cella precedente = null;
        
        private Cella(char carattere){
            this.carattere = carattere;
        }
        
        private void setCellaSuccessiva(Cella successiva){
            this.successiva = successiva;
        }
        
        private Cella getCellaSuccessiva(){
            return this.successiva;
        }
        
        private void setCellaPrecedente(Cella precedente){
            this.precedente = precedente;
        }
        
        private Cella getCellaPrecedente(){
            return this.precedente;
        }
        
        private void setCarattereCella(char carattere){
            this.carattere = carattere;
        }
        
        private char getCarattereCella(){
            return this.carattere;
        }
    }
    
    private void aggiungiCaratteriGamma(String input){
        if(input.isEmpty()) return;
        for(int i = 0; i < input.length(); ++i){
            if(!this.gamma.contieneSimbolo(input.charAt(i))){
                this.gamma.aggiungiSimbolo(input.charAt(i));
            }
        }
    }
    
    public Nastro(Gamma gamma, String input){
        this.gamma = gamma;
        this.aggiungiCaratteriGamma(input);
        if(input.isEmpty()){
            this.cellaCorrente = new Cella(Gamma.getBlank());
        }else{
            Cella iniziale = new Cella(input.charAt(0));
            Cella temp = iniziale;
            for(int i = 1; i < input.length(); i++){
                Cella corrente = new Cella(input.charAt(i));
                corrente.setCellaPrecedente(temp);
                temp.setCellaSuccessiva(corrente);
                temp = corrente;
            }
            this.cellaCorrente = iniziale;
        }
    }
    
    private void setCellaCorrente(Cella cellaCorrente){
        this.cellaCorrente = cellaCorrente;
    }
    
    public void spostaTestinaDestra(){
        //se a destra la cella è vuota la riempio con blank e gestisco il linking fra le celle
        if(this.cellaCorrente.getCellaSuccessiva() == null){
            this.cellaCorrente.setCellaSuccessiva(new Cella(Gamma.getBlank()));
            this.cellaCorrente.getCellaSuccessiva().setCellaPrecedente(this.cellaCorrente);
        }
        this.setCellaCorrente(this.cellaCorrente.getCellaSuccessiva());
    }
    
    public void spostaTestinaSinistra(){
        if(this.cellaCorrente.getCellaPrecedente() == null){
            this.cellaCorrente.setCellaPrecedente(new Cella(Gamma.getBlank()));
            this.cellaCorrente.getCellaPrecedente().setCellaSuccessiva(this.cellaCorrente);
        }
        this.setCellaCorrente(this.cellaCorrente.getCellaPrecedente());
    }
    
    
    public void setCarattereCellaCorrente(char carattere){
        if(!this.gamma.contieneSimbolo(carattere))
            this.gamma.aggiungiSimbolo(carattere);
        this.cellaCorrente.setCarattereCella(carattere);
    }
    
    public char getCarattereCellaCorrente(){
        return this.cellaCorrente.getCarattereCella();
    }
    
    
    public void aggiungiCellaSuccessiva(char carattere) {
        if (this.cellaCorrente != null) {
            Cella nuovaCella = new Cella(carattere);
            Cella successiva = this.cellaCorrente.getCellaSuccessiva();
            if (successiva != null) {
                nuovaCella.setCellaSuccessiva(successiva);
                successiva.setCellaPrecedente(nuovaCella);
            }
            this.cellaCorrente.setCellaSuccessiva(nuovaCella);
            nuovaCella.setCellaPrecedente(this.cellaCorrente);
        }
    }

    public void aggiungiCellaPrecedente(char carattere) {
        if (this.cellaCorrente != null) {
            Cella nuovaCella = new Cella(carattere);
            Cella precedente = this.cellaCorrente.getCellaPrecedente();
            if (precedente != null) {
                nuovaCella.setCellaPrecedente(precedente);
                precedente.setCellaSuccessiva(nuovaCella);
            }
            this.cellaCorrente.setCellaPrecedente(nuovaCella);
            nuovaCella.setCellaSuccessiva(this.cellaCorrente);
        }
    }
    
    public String stampaCaratteri(){
        String out = new String();
        //ricerco la posizione iniziale del nastro
        Cella corrente = this.cellaCorrente;
        while(corrente.getCellaPrecedente() != null){
            corrente = corrente.getCellaPrecedente();
        }
        for(Cella c = corrente; c != null; c = c.getCellaSuccessiva()){
            out+=c.getCarattereCella();
        }
        return out;
    }
    
    //provo a vedere se funziona
    public void sostituisciContenuto(String nuovoContenuto) {
        // RimuovO tutte le celle esistenti nel nastro corrente
        while (this.cellaCorrente.getCellaPrecedente() != null) {
            this.cellaCorrente = this.cellaCorrente.getCellaPrecedente();
        }
        this.cellaCorrente = new Cella(nuovoContenuto.charAt(0));
        Cella temp = this.cellaCorrente;
        for (int i = 1; i < nuovoContenuto.length(); i++) {
            Cella nuovaCella = new Cella(nuovoContenuto.charAt(i));
            nuovaCella.setCellaPrecedente(temp);
            temp.setCellaSuccessiva(nuovaCella);
            temp = nuovaCella;
        }
    }
}
