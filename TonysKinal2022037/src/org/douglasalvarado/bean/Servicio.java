package org.douglasalvarado.bean;

import java.sql.Time;
//import java.time.LocalTime;
import java.util.Date;

// ver (2[0-3]|[01]?[0-9]):([0-5]?[0-9])$
// ver ^(2[0-4]|1[0-9]|[1-9])$(^[1-5]?[0-9]$)
// vet \b([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]\b
// opcion = \d{0,2}(0-24)\:\d{1,2}(0-59)\:\d{1,2}(0-59)
public class Servicio {
    private int codigoServicio;
    private Date fechaServicio;
    private String tipoServicio;
    private Time horaServicio;
    private String lugarServicio;
    private String telefonoContacto;
    private int codigoEmpresa;

    public Servicio() {
    }

    public Servicio(int codigoServicio, Date fechaServicio, String tipoServicio, Time horaServicio, String lugarServicio, String telefonoContacto, int codigoEmpresa) {
        this.codigoServicio = codigoServicio;
        this.fechaServicio = fechaServicio;
        this.tipoServicio = tipoServicio;
        this.horaServicio = horaServicio;
        this.lugarServicio = lugarServicio;
        this.telefonoContacto = telefonoContacto;
        this.codigoEmpresa = codigoEmpresa;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public Date getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(Date fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public Time getHoraServicio() {
        return horaServicio;
    }

    public void setHoraServicio(Time horaServicio) {
        this.horaServicio = horaServicio;
    }

    public String getLugarServicio() {
        return lugarServicio;
    }

    public void setLugarServicio(String lugarServicio) {
        this.lugarServicio = lugarServicio;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    @Override
    public String toString() {
        return codigoServicio + " | " + tipoServicio;
    }
    
}
