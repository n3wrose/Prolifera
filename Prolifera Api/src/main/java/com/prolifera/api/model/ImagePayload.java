package com.prolifera.api.model;

public class ImagePayload {
    private String imagem;
    private Long id;

    public ImagePayload() { }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}