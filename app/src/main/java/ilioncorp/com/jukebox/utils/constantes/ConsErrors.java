package ilioncorp.com.jukebox.utils.constantes;

/**
 * Created by leona on 2018-03-23.
 */

public enum ConsErrors {
    ERROR_INSERTANDO_DATOS(-1,"Error al insertar los datos #DATO#"),
    ERROR_MODIFICANDO_DATOS(-2,"Error al modificar los datos #DATO#"),
    ERROR_ELIMINANDO_DATOS(-3, "Error al eliminar los datos #DATO#"),
    ERROR_CONSULTANDO_DATOS(-4, "Error al consultar los datos #DATO#"),
    CAMPO_VACIO(-5, "Campo obligatorio #DATO#"),
    CONTRASEÑA_INCORRECTA(-6,"Contraseña Incorrecta"),
    USUARIO_INCORRECTO(-7,"Usuario Incorrecto"),
    PRODUCTO_VENCIDO(-8,"Producto Vencido no se puede registrar");


    int codigo;
    String mensaje;

    ConsErrors(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
