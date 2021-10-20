package helpers;

import models.Usuario;

public class UserPrivilegiesVerify {

    /////////////////////////
    // priv 1 - visitante //
    // priv 2 - operador   //
    // priv 3 - admin      //
    /////////////////////////

    public static boolean permissaoVerBotao(Usuario user, String buttonPriv){
        boolean state = false;
        buttonPriv = buttonPriv.toUpperCase().trim();
        user.setPriv(user.getPriv().toUpperCase().trim());

        switch (buttonPriv.toUpperCase()){
            case "VISITANTE":
                if(user.getPriv().equals("ADMIN")){
                    state = true;
                }else if(user.getPriv().equals("OPERADOR")){
                    state = true;
                }else if(user.getPriv().equals("VISITANTE")){
                    state = true;
                } else{
                    state = false;
                }
                break;
            case "OPERADOR":
                if(user.getPriv().equals("ADMIN")){
                    state = true;
                }else if(user.getPriv().equals("OPERADOR")){
                    state = true;
                }else{
                    state = false;
                }
                break;
            case "ADMIN":
                if(user.getPriv().equals("ADMIN")){
                    state = true;
                }else{
                    state = false;
                }
                break;
        }

        if(state == false){
            System.out.println("Permissao recusada! :"
                    + "\n"
                    + "Permissao do usuario: \n"
                    + user.getPriv()
                    + "\n"
                    + "Permissao necessaria: \n"
                    + buttonPriv);
        }
        return state;
    }

}
