
package org.alejandrocarrillo.bean;


public class Productos_has_Platos {
    private int  codigoProducto;
    private int  codigoPlato;

    public Productos_has_Platos() {
    }

    public Productos_has_Platos(int codigoProducto, int codigoPlato) {
        this.codigoProducto = codigoProducto;
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }
    

}
