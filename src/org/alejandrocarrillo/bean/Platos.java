
package org.alejandrocarrillo.bean;

public class Platos {
    private int codigoPlato;
    private int  cantidad;
    private String nombrePlato;
    private String descripcionPlato;
    private double precioPlato;
    private int TipoPlato_codigoTipoPlato;

    public Platos() {
    }

    public Platos(int codigoPlato, int cantidad, String nombrePlato, String descripcionPlato, double precioPlato, int TipoPlato_codigoTipoPlato) {
        this.codigoPlato = codigoPlato;
        this.cantidad = cantidad;
        this.nombrePlato = nombrePlato;
        this.descripcionPlato = descripcionPlato;
        this.precioPlato = precioPlato;
        this.TipoPlato_codigoTipoPlato = TipoPlato_codigoTipoPlato;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
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

    public int getTipoPlato_codigoTipoPlato() {
        return TipoPlato_codigoTipoPlato;
    }

    public void setTipoPlato_codigoTipoPlato(int TipoPlato_codigoTipoPlato) {
        this.TipoPlato_codigoTipoPlato = TipoPlato_codigoTipoPlato;
    }
      public String toString(){
        return getCodigoPlato()+ " °|° " + getNombrePlato();
    
    }
    
    
}
