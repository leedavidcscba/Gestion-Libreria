/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.sistemalibreria.servicios;

import com.libreria.sistemalibreria.entidades.Editorial;
import com.libreria.sistemalibreria.errores.ErrorServicio;
import com.libreria.sistemalibreria.repositorios.EditorialRepositorio;
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
public class EditorialServicio{
    
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    @Transactional
    public void agregarEditorial(String nombre) throws ErrorServicio {
        validar(nombre);
        Editorial chequeado = editorialRepositorio.buscarEditorialPorNombre(nombre);
        if(chequeado==null){
            Editorial editorial = new Editorial();
            editorial.setNombre(nombre);
            editorial.setAlta(Boolean.TRUE);

            editorialRepositorio.save(editorial);
        }else{
            throw new ErrorServicio("Editorial ya existe en lista: " + chequeado.getNombre());
        }

    }
    
    @Transactional
    public void modificarEditorial(Integer idEditorial, String nombre) throws ErrorServicio {
        validar(nombre);

        Optional<Editorial> respuesta = editorialRepositorio.findById(idEditorial);
        if (respuesta.isPresent()) {
        Editorial editorial = respuesta.get();
        editorial.setNombre(nombre);
        editorial.setAlta(Boolean.TRUE);

        editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontro el editorial solicitado");
        }
    }
    
     @Transactional
    public void deshabilitarEditorial (Integer idEditorial) throws ErrorServicio{
        Optional<Editorial> respuesta = editorialRepositorio.findById(idEditorial);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(Boolean.FALSE);

            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontro el editorial solicitado");
        }
    }
    
    @Transactional
    public void habilitarEditorial (Integer idEditorial) throws ErrorServicio{
        Optional<Editorial> respuesta = editorialRepositorio.findById(idEditorial);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(Boolean.TRUE);

            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontro el autor solicitado");
        }
    }
    
    @Transactional
    public void eliminarEditorial(Integer idEditorial) throws ErrorServicio{
        Optional<Editorial> respuesta = editorialRepositorio.findById(idEditorial);
        if(respuesta.isPresent()){
            Editorial libro = respuesta.get();
                       
            editorialRepositorio.delete(libro);
        }else{
            throw new ErrorServicio("No existe un libro con el identificador solicitado");
        }
    }
    
    public List<Editorial> listarEditoriales(){
        return editorialRepositorio.findAll();
    }
    
    public List<Editorial> listarEditorialesParaListado(){
        return editorialRepositorio.buscarEditorialParaListado();
    }
    
    public List<Editorial> listarAutoresPorFiltro(String alta) {
        if(!alta.equals("")){
         if(alta.equals("Activo")){
                return editorialRepositorio.buscarEditorialPorEstado(Boolean.TRUE);
         }else{
             return editorialRepositorio.buscarEditorialPorEstado(Boolean.FALSE);
         }
        }else{
            return editorialRepositorio.findAll();
        }
    }
    
    public Editorial buscarEditorialNombre(String nombre){
        return editorialRepositorio.buscarEditorialPorNombre(nombre);
    }
    
    public Editorial buscarEditorialPorId(Integer id){
        return editorialRepositorio.buscarEditorialPorId(id);
    }
     public void validar(String nombre) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre de autor no puede ser vacio o nulo");
        }

    }
}
