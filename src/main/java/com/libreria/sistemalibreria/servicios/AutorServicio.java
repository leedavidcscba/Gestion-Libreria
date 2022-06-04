/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.sistemalibreria.servicios;

import com.libreria.sistemalibreria.entidades.Autor;
import com.libreria.sistemalibreria.errores.ErrorServicio;
import com.libreria.sistemalibreria.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author leedavidcuellar
 */
@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional
    public void agregarAutor(String nombre, String apellido) throws ErrorServicio {
        validar(nombre, apellido);
        String nom = nombre + " " + apellido;
        Autor chequeado = autorRepositorio.buscarAutorPorNombre(nom);
        if (chequeado == null) {
            Autor autor = new Autor();
            autor.setNombre(nom);
            autor.setAlta(Boolean.TRUE);

            autorRepositorio.save(autor);
        }else{
            throw new ErrorServicio("Autor ya existe en lista: " + chequeado.getNombre());
        }
    }

    @Transactional
    public void modificarAutor(Integer idAutor, String nombre, String apellido) throws ErrorServicio {

        validar(nombre, apellido);

        Optional<Autor> respuesta = autorRepositorio.findById(idAutor);
        if (respuesta.isPresent()) {
            String nom = nombre + " " + apellido;
            Autor autor = respuesta.get();
            autor.setNombre(nom);
            autor.setAlta(Boolean.TRUE);

            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontro el autor solicitado");
        }
    }

    @Transactional
    public void deshabilitarAutor(Integer idAutor) throws ErrorServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(idAutor);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setAlta(Boolean.FALSE);

            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontro el autor solicitado");
        }
    }

    @Transactional
    public void habilitarAutor(Integer idAutor) throws ErrorServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(idAutor);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setAlta(Boolean.TRUE);

            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontro el autor solicitado");
        }
    }

    @Transactional
    public void eliminarAutor(Integer idAutor) throws ErrorServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(idAutor);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();

            autorRepositorio.delete(autor);
        } else {
            throw new ErrorServicio("No existe un Autor con el identificador solicitado");
        }
    }

    public List<Autor> listarAutoresParaListado() {
           
            return autorRepositorio.buscarAutorParaListado();
        
    }
    
    public List<Autor> listarAutores() {
           
            return autorRepositorio.findAll();
        
    }
    
    public List<Autor> listarAutoresPorFiltro(String alta) {
        if(!alta.equals("")){
         if(alta.equals("Activo")){
                return autorRepositorio.buscarAutorPorEstado(Boolean.TRUE);
         }else{
             return autorRepositorio.buscarAutorPorEstado(Boolean.FALSE);
         }
        }else{
            return autorRepositorio.findAll();
        }
    }

    public Autor buscarAutorNombre(String nombre) {
        return autorRepositorio.buscarAutorPorNombre(nombre);
    }

    public Autor buscarAutorId(Integer id) {
        return autorRepositorio.buscarAutorPorId(id);
    }

    public void validar(String nombre, String apellido) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre de autor no puede ser vacio o nulo");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido de autor no puede ser vacio o nulo");
        }
    }

}
