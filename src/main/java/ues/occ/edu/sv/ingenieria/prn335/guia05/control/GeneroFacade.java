/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ues.occ.edu.sv.ingenieria.prn335.guia05.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ues.occ.edu.sv.ingenieria.prn335.cineData.entity.Genero;

/**
 *
 * @author CF14010
 */
@Stateless
public class GeneroFacade extends AbstractFacade<Genero>{
        @PersistenceContext(unitName = "cinePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Genero> findByNombre(String nombre) {
        List<Genero> GeneroList = new ArrayList<>();
        if (em != null) {
            try {
                Query query = em.createQuery("SELECT e FROM Genero e WHERE e.nombre LIKE CONCAT('%',:nombre,'%')").setParameter("nombre", nombre);
                GeneroList = query.getResultList();
                return GeneroList;
            } catch (Exception ex) {
            }
        }

        return Collections.EMPTY_LIST;
    }

    public GeneroFacade() {
        super(Genero.class);
    }
}
