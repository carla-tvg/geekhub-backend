package com.geekhub1.geekhub1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    private Long id;
    private String nombre;
    private double precio;
    private String imagen;
    private String categoria;
    private int stock;
    private double puntuacion;
    private Date fechaLlegada;

    public Producto(){

    }

    public Producto(Long id, String nombre, double precio, String imagen, String categoria, int stock, double puntuacion, Date fechaLlegada) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.categoria = categoria;
        this.stock = stock;
        this.puntuacion = puntuacion;
        this.fechaLlegada = fechaLlegada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Date getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(Date fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }
}
