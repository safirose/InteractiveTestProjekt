package org.beginningandroid.interactivetest;

public class Kvittering {
    public int lokationId;
    public int antalTypeA;
    public int antalTypeB;
    public int antalTypeC;
    public String tidspunkt;
    public String dato;

    public Kvittering() {
        // Tom konstruktør
    }

    public Kvittering(int lokationId, int a, int b, int c, String tidspunkt, String dato) {
        this.lokationId = lokationId;
        this.antalTypeA = a;
        this.antalTypeB = b;
        this.antalTypeC = c;
        this.tidspunkt = tidspunkt;
        this.dato = dato;
    }

    // Udregner samlet beløb i kroner
    public double getSamletBeloeb() {
        return antalTypeA * 1.0 + antalTypeB * 1.5 + antalTypeC * 3.0;
    }

    // Returnér pænt format til visning
    public String toDisplayString() {
        String lokationNavn = (lokationId == 1) ? "Netto Blågårdsgade 26" :
                (lokationId == 2) ? "Netto Rantzausgade 21" :
                        "Ukendt lokation";

        return "Dato: " + dato + " kl. " + tidspunkt + "\n"
                + "Lokation: " + lokationNavn + "\n"
                + "Flasker: A=" + antalTypeA + ", B=" + antalTypeB + ", C=" + antalTypeC + "\n"
                + "Beløb: " + getSamletBeloeb() + " kr";
    }
}
