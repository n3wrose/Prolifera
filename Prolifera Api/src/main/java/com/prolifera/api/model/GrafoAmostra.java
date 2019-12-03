package com.prolifera.api.model;

import com.prolifera.api.model.DTO.AmostraSimples;
import com.prolifera.api.model.Vertice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GrafoAmostra {

    private HashMap<Long, AmostraSimples> amostras;
    private List<Vertice> vertices ;

    public GrafoAmostra() {
        amostras = new HashMap<Long, AmostraSimples>();
        vertices = new ArrayList<Vertice>();
    }

    public GrafoAmostra(AmostraSimples asdto) {
        amostras = new HashMap<Long, AmostraSimples>();
        vertices = new ArrayList<Vertice>();
        amostras.put(asdto.getIdAmostra(), asdto);
    }

    public void add(AmostraSimples as) {
        if (amostras.containsKey(as.getIdAmostra()))
            return;
        amostras.put(as.getIdAmostra(), as);
        for (Long id : as.getFilhos())
            vertices.add(new Vertice(as.getIdAmostra(), id));
    }

    public boolean isThere(Long id) {
        return amostras.containsKey(id);
    }

    public HashMap<Long, AmostraSimples> getAmostras() {
        return amostras;
    }

    public void setAmostras(HashMap<Long, AmostraSimples> amostras) {
        this.amostras = amostras;
    }

    public List<Vertice> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertice> vertices) {
        this.vertices = vertices;
    }
}
