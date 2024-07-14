/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mdt;

import java.util.Arrays;


/**
 *
 * @author manuelrobertomatera
 */
public class MdT {
    
    private static final char BLANK = Gamma.getBlank();
    private static final char LEFT = Delta.getLeft();
    private static final char RIGHT = Delta.getRight();
    private static final char STAY = Delta.getStay();
    private static final char SEPARATORE_ELEMENTI_COPIA_NASTRI = '\u002A'; //rappresenta il simbolo *
    private static final char SEPARATORE = '\u002D'; // rappresenta il simbolo -
    
    private Q stati;
    
    private Sigma alfabetoInput;
    
    private Gamma alfabetoNastro;
    
    private Delta transizioni; 
    
    private Stato statoIniziale;
    
    private Accettanti statiAccettazione;
    private Rifiutanti statiRifiuto;
    
    private Nastro nastro;
    
    
    public MdT(Q stati, Sigma alfabetoInput, Gamma alfabetoNastro, Delta transizioni, Stato statoIniziale, Accettanti statiAccettazione, Rifiutanti statiRifiuto){
        this.stati = stati;
        this.alfabetoInput = alfabetoInput;
        this.alfabetoNastro = alfabetoNastro;
        this.transizioni = transizioni;
        
        //rivaluta nel caso
        if(statoIniziale.isIniziale())
            this.statoIniziale = statoIniziale;
        
        this.statiAccettazione = statiAccettazione;
        this.statiRifiuto = statiRifiuto;
    }
    
    
    public MdT(Q stati, Delta transizioni, Stato statoIniziale, Accettanti statiAccettazione, Rifiutanti statiRifiuto){
        this.stati = stati;
        this.transizioni = transizioni;
        this.statoIniziale = statoIniziale;
        this.statiAccettazione = statiAccettazione;
        this.statiRifiuto = statiRifiuto;
    }
    
    //verifico se l'input è stato scritto con i giusti simboli
    private boolean isInputInAlfabeto(String input){
        for(int i = 0; i < input.length(); ++i){
            if(!this.alfabetoInput.contieneSimbolo(input.charAt(i)))
                return false;
        }
        return true;
    }
    //tieni conto dello stato corrente
    //restituisce true se l'input viene accettato, false altrimenti
    public boolean isInputAccettato(String input){
        //aggiungi controlli per insieme di stati e il resto delle robe
        //aggiungi eccezioni per i casi in cui devi aggiungere i controlli
        
        if(!isInputInAlfabeto(input)) return false;
        Stato corrente = this.getStatoIniziale();
        if(corrente == null) return false; //non è stato settato lo stato iniziale
        this.setNastro(new Nastro(this.getAlfabetoNastro(), input));
        for(;;){
            if(this.getStatiAccettazione().contieneAccettante(corrente))
                return true;
            else if(this.getStatiRifiuto().contieneRifiutante(corrente))
                return false;
            
            char letto = this.getNastro().getCarattereCellaCorrente();
            
            String value = this.getTransizioni().ottieniValore(corrente, letto);
            Stato successivo = this.getTransizioni().ottieniStatoSuccessivo(value);
            char carattereDaScrivere = this.getTransizioni().ottieniCarattereDaScrivere(value);
            char spostamento = this.getTransizioni().ottieniSpostamento(value);
            
            corrente = successivo;
            this.getNastro().setCarattereCellaCorrente(carattereDaScrivere);
            
            switch(spostamento){
                case '\u004c' -> //Left
                    this.getNastro().spostaTestinaSinistra();
                case '\u0052' -> //Right
                    this.getNastro().spostaTestinaDestra();
                case '\u0053' -> {
                    //Stay
                }
                default -> {
                }
            }
            
        }
    }
    
    //considero più stati di accettazione MA NON QUELLI DI RIGETTO
    //aggiungere controlli
    public String getCodificaMdT(){
        return this.getStatoIniziale().getBinaryCode()
                + SEPARATORE
                + this.getStatiAccettazione().getCodificaAccettanti()
                + SEPARATORE_ELEMENTI_COPIA_NASTRI
                + this.getTransizioni().getCodificaQuintuple();
    }
    
    
    
    
    
    
    
    
    //AGGIUNGERE CODIFICHE PER LE COMPONENTI DELLA MACCHINA PER FARE LA MACCHINA DI TURING UNIVERSALE
    
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
        
        System.out.println(m.isInputAccettato("11")); //restituisce 100
        System.out.println(m.getNastro().stampaCaratteri());
        System.out.println(m.getCodificaMdT());
        
        String s1 = new String(new char[]{'1'});
        String s2 = new String(new char[]{'1'});

        
        

        System.out.println("Binario s1: " + Integer.toBinaryString(s1.hashCode()));
        System.out.println("Binario s2: " + Integer.toBinaryString(s2.hashCode()));
        String z[] = m.getCodificaMdT().split(new String(new char[] {SEPARATORE}));
        System.out.println(z[0]);
        System.out.println(Arrays.toString(m.getCodificaMdT().split("\\*")));
        String f[] = m.getCodificaMdT().split("\\*");
        String w[] = f[0].split("-");
        String res = String.join("-", Arrays.copyOfRange(w, 1, w.length));
        System.out.println("Stati di accettazione: " + res);
        
        
        String[] copie = m.getCodificaMdT().split("\\*")[1].split("\\+");
        String c = String.join("+", Arrays.copyOfRange(copie, 0, copie.length));
        System.out.println("Altra parte della codifica della macchina di turing: " + c);
        
        String right = new String(new char[]{RIGHT});
        String left = new String(new char[]{LEFT});
        String stay = new String(new char[]{STAY});
        System.out.println("left: " + Integer.toBinaryString(left.hashCode()));
        System.out.println("right: " + Integer.toBinaryString(right.hashCode()));
        System.out.println("stay: " + Integer.toBinaryString(stay.hashCode()));
        System.out.println("valueof left: " + Integer.valueOf(Integer.toBinaryString(left.hashCode())));
        
        
    }

    /**
     * @return the stati
     */
    public Q getStati() {
        return stati;
    }

    /**
     * @return the alfabetoInput
     */
    public Sigma getAlfabetoInput() {
        return alfabetoInput;
    }

    /**
     * @return the alfabetoNastro
     */
    public Gamma getAlfabetoNastro() {
        return alfabetoNastro;
    }

    /**
     * @return the transizioni
     */
    public Delta getTransizioni() {
        return transizioni;
    }

    /**
     * @return the statoIniziale
     */
    public Stato getStatoIniziale() {
        return statoIniziale;
    }

    /**
     * @return the statiAccettazione
     */
    public Accettanti getStatiAccettazione() {
        return statiAccettazione;
    }

    /**
     * @return the statiRifiuto
     */
    public Rifiutanti getStatiRifiuto() {
        return statiRifiuto;
    }

    /**
     * @param stati the stati to set
     */
    public void setStati(Q stati) {
        this.stati = stati;
    }

    /**
     * @param alfabetoInput the alfabetoInput to set
     */
    public void setAlfabetoInput(Sigma alfabetoInput) {
        this.alfabetoInput = alfabetoInput;
    }

    /**
     * @param alfabetoNastro the alfabetoNastro to set
     */
    public void setAlfabetoNastro(Gamma alfabetoNastro) {
        this.alfabetoNastro = alfabetoNastro;
    }

    /**
     * @param transizioni the transizioni to set
     */
    public void setTransizioni(Delta transizioni) {
        this.transizioni = transizioni;
    }

    /**
     * @param statoIniziale the statoIniziale to set
     */
    public void setStatoIniziale(Stato statoIniziale) {
        this.statoIniziale = statoIniziale;
    }

    /**
     * @param statiAccettazione the statiAccettazione to set
     */
    public void setStatiAccettazione(Accettanti statiAccettazione) {
        this.statiAccettazione = statiAccettazione;
    }

    /**
     * @param statiRifiuto the statiRifiuto to set
     */
    public void setStatiRifiuto(Rifiutanti statiRifiuto) {
        this.statiRifiuto = statiRifiuto;
    }

    /**
     * @param nastro the nastro to set
     */
    public void setNastro(Nastro nastro) {
        this.nastro = nastro;
    }

    /**
     * @return the nastro
     */
    public Nastro getNastro() {
        return nastro;
    }
}
