package com.example.redone.models;

import java.util.ArrayList;
import java.util.List;

public class Codigos {

    private List<RegistroUserFicha>fichas = new ArrayList<>();

    public List<RegistroUserFicha> getFichas() {
        return fichas;
    }

    public void setFichas(List<RegistroUserFicha> fichas) {
        this.fichas = fichas;
    }
}
