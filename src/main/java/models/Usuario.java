package models;

import java.util.Objects;

public class Usuario {

    private Long id;
    private String username;
    private String pass;
    private String priv;


    public Usuario () {}

    public Usuario(Long id, String username, String pass, String priv) {
        this.id = id;
        this.username = username;
        this.pass = pass;
        this.priv = priv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPriv() {
        return priv;
    }

    public void setPriv(String priv) {
        this.priv = priv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return priv == usuario.priv && Objects.equals(id, usuario.id) && Objects.equals(username, usuario.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, priv);
    }
}
