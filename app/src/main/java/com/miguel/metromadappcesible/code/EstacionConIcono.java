package com.miguel.metromadappcesible.code;

import android.graphics.drawable.Drawable;

/**
 * Created by Miguel on 01/01/2017.
 */

public class EstacionConIcono {
    Drawable iconoLinea;
    String descripcionEstacion;

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
