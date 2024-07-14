/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mdt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author manuelrobertomatera
 */
public class UMdT {

    private static final char BLANK = Gamma.getBlank();
    private static final char LEFT = Delta.getLeft();
    private static final char RIGHT = Delta.getRight();
    private static final char STAY = Delta.getStay();
    
    private static final int BINLEFT = 1001100;
    private static final int BINRIGHT = 1010010;
    private static final int BINSTAY = 1010011;
    
    

    private static final char SEPARATORE_ELEMENTI_COPIA_NASTRI = '\u002A'; //rappresenta il simbolo *
    private static final String SEPARATORE_QUINTUPLE = "\\u002B"; // rappresenta il simbolo +
    private static final String SEPARATORE_ELEMENTI = "\u002D"; // rappresenta il simbolo -

    private Nastro1 n1;
    private Nastro n2;
    private Nastro n3;
    private Nastro n4;

    private Gamma gamma;

    private Stato statoCorrente;
    private Q stati;
    
    
    private Map<Stato, Transizione> transizioni;

    //aggiungere metodo che mi restituisce la codifica in binario del carattere che sto leggendo attualmente
    //secondo nastro
    private interface Transizione {
        Stato eseguiTransizione(Nastro1 n1, Nastro n2, Nastro n3, Nastro n4);
    }
    
    private String getCodificaCarattereOsservato(char carattereOsservato){
        return Integer.toBinaryString(new String(new char[]{carattereOsservato}).hashCode());
    }
    
    private char getCarattereCodificaOsservata(String codifica){
        //System.out.println("Codifica che sto convertendo: " + codifica);
        return (char) Integer.parseInt(codifica, 2);
    }
    
    private boolean isStatoAccettante(){
        String s3 = n3.stampaCaratteri();
        String[] s4 = n4.stampaCaratteri().split("-");
        for(String s : s4){
            if(s3.equals(s)) return true;
        }
        return false;
    }
    
    
    //L'idea è di ciclare su tutte le delta finché non mi trovo in uno stato di accettazione
    private Map<Stato, Transizione> getTransizioni() {
       Map<Stato, Transizione> transizioni = new HashMap<>();
       
       Transizione fromQ0 = (n1, n2, n3, n4) ->{
           return stati.ottieniStato("q1");
       };
       
       
       Transizione fromQ1 = (n1, n2, n3, n4) ->{
           if(n1.getCodificaCorrente().equals(n3.stampaCaratteri())){
               n1.spostaCodificaSuccessiva();
               return stati.ottieniStato("qStatoCorretto");
           }else{
               return stati.ottieniStato("q3");
           }
       };
       
       Transizione fromQStatoCorretto = (n1, n2, n3, n4) ->{
           //System.out.println("Carattere osservato su n2: " + n2.getCarattereCellaCorrente());
           if(n1.getCodificaCorrente()
                   .equals(getCodificaCarattereOsservato(
                           n2.getCarattereCellaCorrente()))){
               n1.spostaCodificaSuccessiva();
               return stati.ottieniStato("qScrivi"); 
           }else{
               return stati.ottieniStato("q2");
           }
       };
       
       Transizione fromQ2 = (n1, n2, n3, n4) ->{
           if(!n1.spostaQuintuplaSuccessiva()){
               return stati.ottieniStato("qReject");
           }else{
               return stati.ottieniStato("q1");
           }
       };
       
       
       Transizione fromQ3 = (n1, n2, n3, n4) ->{
           if(!n1.spostaQuintuplaSuccessiva()){
               //ATTENZIONE
               if(isStatoAccettante()){
                   return stati.ottieniStato("qAccept");
               }else{
                   return stati.ottieniStato("qReject");
               }
           }else{
               return stati.ottieniStato("q1");
           }
       };
       
       
       Transizione fromQScrivi = (n1, n2, n3, n4) ->{
           //System.out.println("Carattere codifica osservata su n1: " + getCarattereCodificaOsservata(n1.getCodificaCorrente()));
           n2.setCarattereCellaCorrente(getCarattereCodificaOsservata(n1.getCodificaCorrente()));
           n1.spostaCodificaSuccessiva();
           return stati.ottieniStato("qCambiaStato");
       };
       
       //DA SISTEMARE
       Transizione fromQCambiaStato = (n1, n2, n3, n4)->{
           //System.out.println("Sono in qCambiaStato e leggo su n1:" + n1.getCodificaCorrente());
           n3.sostituisciContenuto(new Nastro(gamma, n1.getCodificaCorrente()).stampaCaratteri());
           n1.spostaCodificaSuccessiva();
           return stati.ottieniStato("qMuovi");
       };
       
       
       Transizione fromQMuovi = (n1, n2, n3, n4)->{
           //System.out.println("spostamento: " + Integer.valueOf(n1.getCodificaCorrente()));
           switch(Integer.valueOf(n1.getCodificaCorrente())){
               case BINLEFT -> n2.spostaTestinaSinistra();
               case BINRIGHT -> n2.spostaTestinaDestra();
               case BINSTAY -> {}
               default -> {}
           }
           return stati.ottieniStato("qRiavvolgi");
       };
       
       Transizione fromQRiavvolgi = (n1, n2, n3, n4) ->{
           n1.riavvolgiNastro();
           return stati.ottieniStato("q1");
       };
       
       transizioni.put(stati.ottieniStato("q0"), fromQ0);
       transizioni.put(stati.ottieniStato("q1"), fromQ1);
       transizioni.put(stati.ottieniStato("q2"), fromQ2);
       transizioni.put(stati.ottieniStato("q3"), fromQ3);
       transizioni.put(stati.ottieniStato("qStatoCorretto"), fromQStatoCorretto);
       transizioni.put(stati.ottieniStato("qScrivi"), fromQScrivi);
       transizioni.put(stati.ottieniStato("qCambiaStato"), fromQCambiaStato);
       transizioni.put(stati.ottieniStato("qMuovi"), fromQMuovi);
       transizioni.put(stati.ottieniStato("qRiavvolgi"), fromQRiavvolgi);
       
       return transizioni;
    }
    

    //lavoro su codifiche binarrie
    private Sigma getAlfabetoInput() {
        Sigma sigma = new Sigma();
        sigma.aggiungiSimbolo('0');
        sigma.aggiungiSimbolo('1');
        sigma.aggiungiSimbolo('\u002B'); //separatore quintuple
        sigma.aggiungiSimbolo('\u002A');  //separatore elementi copia nastri
        sigma.aggiungiSimbolo('\u002D');  //separatore elementi
        return sigma;
    }

    //avrò in aggiunta il solo simbolo blank
    private Gamma getAlfabetoNastro() {
        return new Gamma(getAlfabetoInput());
    }

    private String getStatoIniziale(String descrizioneMdT) {
        return descrizioneMdT.split(SEPARATORE_ELEMENTI)[0];
    }

    private String getStatiAccettanti(String descrizioneMdT) {
        String[] copie = descrizioneMdT.split("\\*")[0].split("-"); //uso esplicito di \\* perché Dangling meta character '*' near index 0
        return String.join("-", Arrays.copyOfRange(copie, 1, copie.length));
    }

    private Q getStatiMacchinaUniversale() {

        Q q = new Q();

        Stato q0 = new Stato("q0");
        q0.setIniziale(true);

        Stato qAccept = new Stato("qAccept");
        qAccept.setAccettazione(true);

        Stato qReject = new Stato("qReject");
        qReject.setRifiuto(true);

        Stato q1 = new Stato("q1");
        Stato q2 = new Stato("q2");
        Stato q3 = new Stato("q3");

        Stato qStatoCorretto = new Stato("qStatoCorretto");
        Stato qScrivi = new Stato("qScrivi");
        Stato qCambiaStato = new Stato("qCambiaStato");
        Stato qMuovi = new Stato("qMuovi");
        Stato qRiavvolgi = new Stato("qRiavvolgi");

        q.aggiungiStati(q0, qAccept, qReject, q1, q2,
                q3, qStatoCorretto, qScrivi,
                qCambiaStato, qMuovi, qRiavvolgi);

        return q;

    }

    public UMdT(String descrizioneMdT, String input) {
        this.gamma = this.getAlfabetoNastro();
        this.n1 = new Nastro1(gamma, descrizioneMdT);
        this.n2 = new Nastro(gamma, input);
        this.n3 = new Nastro(gamma, this.getStatoIniziale(descrizioneMdT));
        this.n4 = new Nastro(gamma, this.getStatiAccettanti(descrizioneMdT));
        this.stati = this.getStatiMacchinaUniversale();

        //rivedi meglio
        if (this.stati.ottieniStato("q0").isIniziale()) {
            this.statoCorrente = this.stati.ottieniStato("q0");
        }
        
        this.transizioni = this.getTransizioni();
    }
    
    public boolean isInputAccettato(){
        for(;;){
            //System.out.println("Stato: " + this.statoCorrente.getEtichetta());
            if(this.statoCorrente.isAccettazione()) return true;
            if(this.statoCorrente.isRifiuto()) return false;
            Transizione transizione = transizioni.get(this.statoCorrente);
            this.statoCorrente = transizione.eseguiTransizione(n1, n2, n3, n4);
            //System.out.println("NASTRO 3: "+ n3.stampaCaratteri());
            //System.out.println("NASTRO 4: " + n4.stampaCaratteri());
        }
    }

    private class Nastro1 extends Nastro {

        private List<String> quintuple = new ArrayList<>();
        private String quintuplaCorrente;
        private int posizioneCodifica;
        private String codificaCorrente;

        public Nastro1(Gamma gamma, String input) {
            super(gamma, Nastro1.getDescrizione2MdT(input));
            this.quintuple = Arrays.asList(super.stampaCaratteri().split(SEPARATORE_QUINTUPLE));
            this.quintuplaCorrente = quintuple.get(0);
            this.posizioneCodifica = 0;
            this.codificaCorrente = this.quintuplaCorrente.split(SEPARATORE_ELEMENTI)[this.posizioneCodifica];
        }

        //restituisco la seconda parte della descrizione della mdt che non comprende lo stato iniziale
        //e quelli di accettazione della macchina
        private static String getDescrizione2MdT(String descrizioneMdT) {
            String[] copie = descrizioneMdT.split("\\*")[1].split("\\+");
            return String.join("+", Arrays.copyOfRange(copie, 0, copie.length));
        }

        public int getPosizioneCodifica() {
            return this.posizioneCodifica;
        }

        public void setPosizioneCodifica(int posizioneCodifica) {
            this.posizioneCodifica = posizioneCodifica;
        }

        public void setQuintuplaCorrente(String quintuplaCorrente) {
            this.quintuplaCorrente = quintuplaCorrente;
        }

        //false se l'operazione non può essere eseguita, true altrimenti
        public boolean spostaQuintuplaSuccessiva() {
            int pos = quintuple.indexOf(quintuplaCorrente);
            //FAI ATTENZIONE
            //mi posiziono alla prima codifica
            if (pos >= 0 && pos < quintuple.size() - 1) {
                this.setQuintuplaCorrente(quintuple.get(pos + 1));
                this.posizioneCodifica = 0;
                this.codificaCorrente = this.quintuplaCorrente.split(SEPARATORE_ELEMENTI)[this.posizioneCodifica];
                return true;
            } else {
                return false;  //MAGARI LANCIO ECCEZIONE
            }
        }
        
        private String ottieniCodificaCorrente() {
        return this.quintuplaCorrente.split(SEPARATORE_ELEMENTI)[this.posizioneCodifica];
        }

        public void spostaCodificaSuccessiva() {
            if (this.posizioneCodifica < this.quintuplaCorrente.split(SEPARATORE_ELEMENTI).length - 1) {
                this.posizioneCodifica++;
                this.codificaCorrente = ottieniCodificaCorrente();
            } //da prevedere un altro controllo
        }

        public void riavvolgiNastro() {
            this.quintuplaCorrente = quintuple.get(0);
            this.posizioneCodifica = 0;
            this.codificaCorrente = this.quintuplaCorrente.split(SEPARATORE_ELEMENTI)[this.posizioneCodifica];
        }

        public String getCodificaCorrente() {
            return this.codificaCorrente;
        }
    }

    public static void main(String[] args) {
        //Considero come esempio una mdt che fa l'incremento binario
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
        
        Accettanti a = new Accettanti(q);
        a.aggiungiAccettante(q3);
        a.aggiungiAccettante(q4);
        
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

        Rifiutanti r = new Rifiutanti(q);
        
        MdT m = new MdT(q, s, g, d, q0, a, r);
        
        //aggiungere controllo sull'input per vedere se è nell'alfabeto oppure evitare di considerare l'alfabeto
        
        String input = "111";
        System.out.println("MACCHINA DI TURING A SINGOLO NASTRO su input: " + input);
        System.out.println("Input accettato da mdt a singolo nastro: " + m.isInputAccettato(input)); //restituisce 100
        System.out.println("Nastro: " + m.getNastro().stampaCaratteri());
        System.out.println("Codifica: " + m.getCodificaMdT());

        UMdT u = new UMdT(m.getCodificaMdT(), input);

        System.out.println("");
        System.out.println("MACCHINA DI TURING UNIVERSALE");
        System.out.println("Nastro 1: \n" + u.n1.stampaCaratteri());
        System.out.println("Nastro 2: \n" + u.n2.stampaCaratteri());
        System.out.println("Nastro 3: \n" + u.n3.stampaCaratteri());
        System.out.println("Nastro 4: \n" + u.n4.stampaCaratteri());
        
        System.out.println("Input accettato da mdt Universale: " + u.isInputAccettato());
        System.out.println("Nastro 2 dopo esecuzione: " + u.n2.stampaCaratteri());
        
        //System.out.println(u.n3.stampaCaratteri());
        

    }

}
