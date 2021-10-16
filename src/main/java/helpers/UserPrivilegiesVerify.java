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

        switch (buttonPriv){
            case "visitante":
                if(user.getPriv().equals("admin")){
                    state = true;
                }else if(user.getPriv().equals("user")){
                    state = true;
                }else if(user.getPriv().equals("visitante")){
                    state = true;
                } else{
                    state = false;
                }
                break;
            case "user":
                if(user.getPriv().equals("admin")){
                    state = true;
                }else if(user.getPriv().equals("user")){
                    state = true;
                }else{
                    state = false;
                }
                break;
            case "admin":
                if(user.getPriv().equals("admin")){
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
