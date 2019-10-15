/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ues.occ.edu.sv.ingenieria.prn335.guia05.boundary;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import ues.occ.edu.sv.ingenieria.prn335.cineData.entity.Clasificacion;
import ues.occ.edu.sv.ingenieria.prn335.cineData.entity.Director;
import ues.occ.edu.sv.ingenieria.prn335.cineData.entity.Genero;
import ues.occ.edu.sv.ingenieria.prn335.cineData.entity.Pelicula;
import ues.occ.edu.sv.ingenieria.prn335.guia05.control.AbstractFacade;
import ues.occ.edu.sv.ingenieria.prn335.guia05.control.ClasificacionFacade;
import ues.occ.edu.sv.ingenieria.prn335.guia05.control.DirectorFacade;
import ues.occ.edu.sv.ingenieria.prn335.guia05.control.GeneroFacade;
import ues.occ.edu.sv.ingenieria.prn335.guia05.control.PeliculaFacade;

/**
 *
 * @author CF14010
 */
@Named(value = "BeanPelicula")
@ViewScoped
public class PeliculaBean extends BackingBean<Pelicula> implements Serializable {

        public PeliculaBean() {
    }    

    @Inject
    PeliculaFacade peliculaFacade;
    @Inject
    ClasificacionFacade clasificacionFacade;
    @Inject
    DirectorFacade directorFacade;
    @Inject
    GeneroFacade generoFacade;
    protected List<Clasificacion> listaClasificacion;
    protected List<Director> listaDirector;
    List<Genero> generoList;

    @PostConstruct
    @Override
    public void inicializar() {
        super.inicializar();
        listaClasificacion = new ArrayList<>();
        listaDirector = new ArrayList<>();
        generoList = new ArrayList<>();
        try {
            this.listaClasificacion = clasificacionFacade.findAll();
        } catch (Exception ex) {
            this.listaClasificacion = Collections.EMPTY_LIST;
        }
        try {
            this.listaDirector = directorFacade.findAll();
        } catch (Exception ex) {
            this.listaDirector = Collections.EMPTY_LIST;
        }
        try {
            this.generoList = generoFacade.findAll();
        } catch (Exception ex) {
            this.generoList = Collections.EMPTY_LIST;
        }
    }

    @Override
    public Object clavePorDatos(Pelicula object) {
                if (object != null) {
            return object.getIdPelicula();
        }
        return null;

    }

    @Override
    public Pelicula datosPorClave(String rowkey) {
        if (rowkey != null && !rowkey.trim().isEmpty()) {
            try {
                return this.getModelo().getWrappedData().stream().filter(r -> r.getIdPelicula().toString().compareTo(rowkey) == 0).collect(Collectors.toList()).get(0);
            } catch (Exception ex) {

            }
        }
        return null;

    }

    @Override
    public AbstractFacade<Pelicula> getFacade() {
        return peliculaFacade;
    }

    @Override
    public void crearNuevo() {
        this.registro = new Pelicula();
    }

    @Override
    public void btnEliminarHandler(ActionEvent ae) {
        super.btnEliminarHandler(ae);
    }

    @Override
    public void btnCancelarHandler(ActionEvent ae) {
        super.btnCancelarHandler(ae);
    }

    @Override
    public LazyDataModel<Pelicula> getModelo() {
        return super.getModelo();
    }

 @Override
    public void btnAgregar(ActionEvent ae) {
        if (registro != null || registro.getGeneroList() != null) {
            List<Genero> generos = registro.getGeneroList();
            registro.setGeneroList(Collections.emptyList());
            getFacade().create(registro);
            registro.setGeneroList(generos);
            this.btnModificar(ae);
            inicializar();
        }
    }
   
    @Override
        public void btnModificar(ActionEvent ae) {
        List<Genero> generosOriginales = getFacade().find(registro.getIdPelicula()).getGeneroList();
        if (registro != null) {
            getFacade().edit(registro);
            if (generosOriginales.size() > registro.getGeneroList().size()) { 
                for (int i = 0; i < generosOriginales.size(); i++) {
                    if (registro.getGeneroList().contains(generosOriginales.get(i)) == false) {
                        generosOriginales.get(i).getPeliculaList().remove(registro);
                        generoFacade.edit(generosOriginales.get(i));
                        System.out.println("borro");
                    }
                }
            } else {
                for (int i = 0; i < registro.getGeneroList().size(); i++) { //añadio generos
                    if (generosOriginales.contains(registro.getGeneroList().get(i)) == false) {
                        registro.getGeneroList().get(i).getPeliculaList().add(registro);
                        generoFacade.edit(registro.getGeneroList().get(i));
                        System.out.println("añadio");
                    }
                }
            }
            inicializar();
        }
    }
        
        
        public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Fecha Seleccionada", format.format(event.getObject())));
    }
                    
        
        public void onItemSelect(SelectEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item Selected", event.getObject().toString()));
    }

    public List<Clasificacion> getListaClasificacion() {
        return listaClasificacion;
    }

    public void setListaClasificacion(List<Clasificacion> listaClasificacion) {
        this.listaClasificacion = listaClasificacion;
    }

    public List<Director> getListaDirector() {
        return listaDirector;
    }

    public void setListaDirector(List<Director> listaDirector) {
        this.listaDirector = listaDirector;
    }

    public List<Genero> getGeneroList() {
        return generoList;
    }

    public void setGeneroList(List<Genero> generoList) {
        this.generoList = generoList;
    }
    
    @Override
    public Pelicula getRegistro() {
        if (registro == null) {
            registro = new Pelicula();
        }
        return super.getRegistro();
    }
    
}