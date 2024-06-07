package com.meilleurtaux.restservice;

public class Commune {
    private String nom;
    private int population;

    // Constructor
    public Commune(String nom, int population) {
        this.nom = nom;
        this.population = population;
    }

    // Getters
    public String getNom() {
        return nom;
    }

    public int getPopulation() {
        return population;
    }
}
