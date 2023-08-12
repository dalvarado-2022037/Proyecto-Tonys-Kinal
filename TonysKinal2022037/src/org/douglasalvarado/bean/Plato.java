package org.douglasalvarado.bean;

public class Plato {
    private int codigoPlato;
    private int cantidadPlato;
    private String nombrePlato;
    private String descripcionPlato;
    private double precioPlato;
    private int codigoTipoPlato;

    public Plato() {
    }

    public Plato(int codigoPlato, int cantidadPlato, String nombrePlato, String descripcionPlato, double precioPlato, int codigoTipoPlato) {
        this.codigoPlato = codigoPlato;
        this.cantidadPlato = cantidadPlato;
        this.nombrePlato = nombrePlato;
        this.descripcionPlato = descripcionPlato;
        this.precioPlato = precioPlato;
        this.codigoTipoPlato = codigoTipoPlato;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCantidadPlato() {
        return cantidadPlato;
    }

    public void setCantidadPlato(int cantidadPlato) {
        this.cantidadPlato = cantidadPlato;
    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public String getDescripcionPlato() {
        return descripcionPlato;
    }

    public void setDescripcionPlato(String descripcionPlato) {
        this.descripcionPlato = descripcionPlato;
    }

    public double getPrecioPlato() {
        return precioPlato;
    }

    public void setPrecioPlato(double precioPlato) {
        this.precioPlato = precioPlato;
    }

    public int getCodigoTipoPlato() {
        return codigoTipoPlato;
    }

    public void setCodigoTipoPlato(int codigoTipoPlato) {
        this.codigoTipoPlato = codigoTipoPlato;
    }

    @Override
    public String toString() {
        return codigoPlato + " | " + nombrePlato;
    }
}
