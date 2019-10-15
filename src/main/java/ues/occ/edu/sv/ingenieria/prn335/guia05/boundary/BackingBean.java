/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ues.occ.edu.sv.ingenieria.prn335.guia05.boundary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ues.occ.edu.sv.ingenieria.prn335.guia05.control.AbstractFacade;

/**
 *
 * @author CF14010
 */

/**
 * Clase abstracta Backing Bean para 
 * manejar cualquier entidad de tipo T
 * @param <T> 
 */
public abstract class BackingBean<T> {

   /**
    * 
    */
    protected EstadoCRUD estado;
    private LazyDataModel<T> modelo;
    protected T registro;

    @PostConstruct
    public void inicializar() {
        crearNuevo();
        this.estado = EstadoCRUD.NUEVO;
        this.inicializarModelo();
        this.modelo.setRowIndex(-1);

    }

    public abstract Object clavePorDatos(T object);

    public abstract T datosPorClave(String rowkey);

    protected int pageSize = 5;

    public abstract AbstractFacade getFacade();

    public abstract void crearNuevo();
    
    
    public List<T> cargarDatos(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<T> salida = null;
        try {
            if (getFacade() != null) {
                salida = getFacade().findRange(first, pageSize);
                if (this.modelo != null) {
                    this.modelo.setRowCount(getFacade().count());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            if (salida == null) {
                salida = new ArrayList();
            }
        }
        return salida;
    }

    protected void inicializarModelo() {
        try {
            this.modelo = new LazyDataModel<T>() {
                @Override
                public Object getRowKey(T object) {
                    return clavePorDatos(object);
                }

                @Override
                public T getRowData(String rowKey) {
                    return datosPorClave(rowKey);
                }

                @Override
                public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    return cargarDatos(first, pageSize, sortField, sortOrder, filters);
                }
            };

        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    public void btnNuevo(ActionEvent ae) {
        this.estado = EstadoCRUD.CREAR;
    }

        public void btnAgregar(ActionEvent ae) {
            this.estado=EstadoCRUD.NUEVO;
        if (registro != null) {
            getFacade().create(registro);
            
        }
    }

    public void btnModificar(ActionEvent ae) {
        this.estado = EstadoCRUD.NUEVO;
        if (registro != null) {
            getFacade().edit(registro);
            inicializar();
        }
    }

    public void btnEliminarHandler(ActionEvent ae) {
        this.estado = EstadoCRUD.NUEVO;
        if (getFacade() != null && registro != null) {
            try {
                getFacade().remove(registro);
                inicializar();
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }


    public void btnCancelarHandler(ActionEvent ae) {
        this.estado = EstadoCRUD.NUEVO;
        this.registro=null;
    }

    public void onRowSelected(SelectEvent se) {

        if (se.getObject() != null) {
            try {
                this.registro = (T) se.getObject();
                this.estado = EstadoCRUD.EDITAR;
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    public void onRowDeselect(UnselectEvent event) {
        this.estado = EstadoCRUD.NUEVO;
        crearNuevo();
        this.modelo.setRowIndex(-1);

    }

    public LazyDataModel<T> getModelo() {
        return modelo;
    }

    public void setModelo(LazyDataModel<T> model) {
        this.modelo = model;
    }

    public enum EstadoCRUD {
        NUEVO, CREAR, EDITAR;
    }

    public EstadoCRUD getEstado() {
        return estado;
    }

    public void setEstado(EstadoCRUD estado) {
        this.estado = estado;
    }

    public T getRegistro() {
        return registro;
    }

    public void setRegistro(T registro) {
        this.registro = registro;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
}