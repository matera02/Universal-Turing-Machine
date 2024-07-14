/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mdt;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author manuelrobertomatera
 */
public class Delta {
    static final char BLANK = Gamma.getBlank();
    private Map<String, String> delta;
    private Gamma gamma;
    private Q stati;
    private static final int ERROR = -1;
    private static final char LEFT = '\u004c';  //L
    private static final char RIGHT = '\u0052'; //R
    private static final char STAY = '\u0053';  //S
    private static final char SEPARATORE_QUINTUPLE = '\u002B'; // rappresenta il simbolo +
    private static final char SEPARATORE_ELEMENTI_QUINTUPLA = '\u002D'; // rappresenta il simbolo -
    private static final String SEPARATORE = "\n";
    
    public Delta(Q stati, Gamma gamma){
       this.delta = new HashMap<>();
       this.stati = stati;
       this.gamma = gamma;
    }
    
    private String calcolaChiave(Stato from, char letto){
        if(this.gamma.contieneSimbolo(letto))
            return 
                    from.getEtichetta()
                  + SEPARATORE
                  + new String(new char[]{letto});
        else
            return null;
    }
    
    private String calcolaValore(Stato to, char scritto, char spostamento){ //per rimanere fedele alla codifica del pdf
        return 
                new String(new char[]{scritto})
                + SEPARATORE 
                + to.getEtichetta()
                + SEPARATORE 
                + new String(new char[]{spostamento});
    }
    
    private boolean isSpostamentoValido(char spostamento){
       return (spostamento == LEFT || spostamento == RIGHT || spostamento == STAY);
    }
    
    public void aggiungiTransizione(Stato from, char letto, Stato to, char scritto, char spostamento){
        //controllo che gli stati siano corretti
        if(!(stati.contieneStato(from) && stati.contieneStato(to))) return;
        
        
        String key = this.calcolaChiave(from, letto);
        //controllo se il carattere osservato sia in gamma e se lo spostamento indicato sia corretto
        
        if(key!=null && isSpostamentoValido(spostamento)){
            if(!this.gamma.contieneSimbolo(scritto))
                this.gamma.aggiungiSimbolo(scritto);
            
            String value = calcolaValore(to, scritto, spostamento);
            
            this.delta.put(key, value);
        }
    }
    
    public void rimuoviTransizione(Stato from, char letto){
        this.delta.remove(calcolaChiave(from, letto));
    }
    
    
    public String ottieniValore(Stato from, char letto){
        return this.delta.get(calcolaChiave(from, letto));
    }
    
    //splitto per separatore e ottengo lo stato che corrisponde alla stringa etichetta in posizione 0
    public Stato ottieniStatoSuccessivo(String value){
        return this.stati.ottieniStato(
                value.split(SEPARATORE)[1]
        );
    }
    
    public char ottieniCarattereDaScrivere(String value){
        return value.split(SEPARATORE)[0].charAt(0);
    }
    
    public char ottieniSpostamento(String value){
        return value.split(SEPARATORE)[2].charAt(0);
    }
    
    public static final char getLeft(){
        return Delta.LEFT;
    }
    
    public static final char getRight(){
        return Delta.RIGHT;
    }
    
    public static final char getStay(){
        return Delta.STAY;
    }
    
    
    private String getCodificaQuintupla(String quintupla){
        String[] elementi = quintupla.split(SEPARATORE);
        String result = new String();
        for(int i = 0; i < elementi.length; ++i){
            result += Integer.toBinaryString(elementi[i].hashCode());
            if(i != elementi.length - 1) result += SEPARATORE_ELEMENTI_QUINTUPLA;
        }
        return result;
    }
    
    
    public String getCodificaQuintuple(){
        String codQuintuple = new String();
        for (Map.Entry<String, String> entry : this.delta.entrySet()) {
            String quintupla = entry.getKey() + SEPARATORE + entry.getValue();
            String codificaQuintupla = this.getCodificaQuintupla(quintupla);
            codQuintuple += codificaQuintupla;
            codQuintuple += SEPARATORE_QUINTUPLE;
        }
        return codQuintuple;
    }
    
    public static void main(String[] args){
        
        
        Q q = new Q();
        
        Stato q0 = new Stato("q0");
        Stato q1 = new Stato("q1");
        Stato q2 = new Stato("q2");
        Stato q3 = new Stato("q3");
        Stato q4 = new Stato("q4");
        
        q0.setIniziale(true);
        
        q.aggiungiStato(q0);
        q.aggiungiStato(q1);
        q.aggiungiStato(q2);
        q.aggiungiStato(q3);
        q.aggiungiStato(q4);
        
        Sigma s = new Sigma();
        s.aggiungiSimbolo('0');
        s.aggiungiSimbolo('1');
        
        
        Gamma g = new Gamma(s); //sigma è un sottoinsieme di gamma al più in gamma aggiungo altri simboli con le delta
        
       
        
        Delta d = new Delta(q, g);
        
        d.aggiungiTransizione(q0, '0', q0, '0', RIGHT);
        d.aggiungiTransizione(q0, '1', q0, '1', RIGHT);
        d.aggiungiTransizione(q0, BLANK, q1, BLANK, LEFT);
        d.aggiungiTransizione(q1, '1', q1, '0', LEFT);
        d.aggiungiTransizione(q1, '0',q2, '1', LEFT);
        d.aggiungiTransizione(q1, BLANK, q4, '1', STAY);
        d.aggiungiTransizione(q2, '0', q2, '0', LEFT);
        d.aggiungiTransizione(q2, '1', q2, '1', LEFT);
        d.aggiungiTransizione(q2, BLANK, q3, BLANK, RIGHT);
        
        System.out.println(d.getCodificaQuintuple());
    }
    
}
