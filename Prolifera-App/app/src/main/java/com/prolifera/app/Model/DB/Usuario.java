package com.prolifera.app.Model.DB;

import java.io.Serializable;

public class Usuario implements Serializable {

    public static final int TIPO_ADMIN = 1;
    public static final int TIPO_COMUM= 2;
    private String nome;
    private String sobrenome;
    private int tipo;
    
    private String login;
    private String senha;
    private String email;

    public String fillPayload() {
        return " {" +
                " \"nome\": \""+nome+"\"," +
                " \"sobrenome\": \""+sobrenome+"\"," +
                " \"tipo\": "+tipo+"," +
                " \"login\": \""+login+"\"," +
                " \"senha\": \""+senha+"\"," +
                " \"email\": \""+email+"\"" +
                " }";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
