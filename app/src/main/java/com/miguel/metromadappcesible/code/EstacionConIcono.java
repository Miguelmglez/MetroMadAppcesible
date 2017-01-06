package com.miguel.metromadappcesible.code;

import android.graphics.drawable.Drawable;

/**
 * Created by Miguel Maroto González on 01/01/2017.
 *
 * Esta clase representa un objeto necesario para pintar las estaciones en la lista de "activity_details".
 *
 * iconoLinea: Representa el icono de la línea que aparecerá en la lista.
 * descripcionEstacion: Representa el texto que aparecerá en la lista.
 *
 */

public class EstacionConIcono {
    Drawable iconoLinea;
    String descripcionEstacion;
    /**
     * Constructor
     */
    public EstacionConIcono(Drawable iconoLinea, String descripcionEstacion) {
        this.iconoLinea = iconoLinea;
        this.descripcionEstacion = descripcionEstacion;
    }

    public Drawable getIconoLinea() {
        return iconoLinea;
    }

    public String getDescripcionEstacion() {
        return descripcionEstacion;
    }
}
