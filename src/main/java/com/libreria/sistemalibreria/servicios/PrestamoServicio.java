/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.sistemalibreria.servicios;

import com.libreria.sistemalibreria.entidades.Cliente;
import com.libreria.sistemalibreria.entidades.Libro;
import com.libreria.sistemalibreria.entidades.Prestamo;
import com.libreria.sistemalibreria.errores.ErrorServicio;
import com.libreria.sistemalibreria.repositorios.PrestamoRepositorio;
import java.util.Date;
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
public class PrestamoServicio {
    @Autowired
    private PrestamoRepositorio prestamoRepositorio;
    
    @Autowired
    private LibroServicio libroServicio;
    
    @Autowired
    private ClienteServicio clienteServicio;
    
    @Transactional
    public void agregarPrestamo(Date fechaPrestamo, Date fechaDevolucion,Libro libro,Cliente cliente)throws ErrorServicio{
        validar(fechaPrestamo, fechaDevolucion, libro, cliente);
        
        Prestamo prestamo = new Prestamo();
        prestamo.setFechaPrestamo(fechaPrestamo);
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setAlta(Boolean.TRUE);
        libroServicio.prestarLibro(libro.getId());
        prestamo.setLibro(libro);
        clienteServicio.validarCliente(cliente);
        prestamo.setCliente(cliente);
        
        prestamoRepositorio.save(prestamo);
    }
    
    @Transactional
    public void modificarPrestamo(String idPrestamo,Date fechaPrestamo, Date fechaDevolucion)throws ErrorServicio{
        validar2(fechaPrestamo, fechaDevolucion);
        
         Optional<Prestamo> respuesta = prestamoRepositorio.findById(idPrestamo);
         if (respuesta.isPresent()) {
             Prestamo prestamo = respuesta.get();
             prestamo.setFechaPrestamo(fechaPrestamo);
            prestamo.setFechaDevolucion(fechaDevolucion);
            prestamo.setAlta(Boolean.TRUE);
            prestamoRepositorio.save(prestamo);
         }else {
            throw new ErrorServicio(" - No se encontro el Prestamo solicitado");
        }
    }
    
    @Transactional
    public void deshabilitarPrestamo(String idPrestamo) throws ErrorServicio {
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(idPrestamo);
        if (respuesta.isPresent()) {
            Prestamo prestamo = respuesta.get();
            prestamo.setAlta(Boolean.FALSE);
            libroServicio.devolverLibro(prestamo.getLibro().getId());
            prestamoRepositorio.save(prestamo);
        } else {
            throw new ErrorServicio(" - No existe un Prestamo con el identificador solicitado");
        }
    }
    
    @Transactional
    public void habilitarPrestamo(String idPrestamo) throws ErrorServicio {
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(idPrestamo);
        if (respuesta.isPresent()) {
            Prestamo prestamo = respuesta.get();
            prestamo.setAlta(Boolean.TRUE);

            prestamoRepositorio.save(prestamo);
        } else {
            throw new ErrorServicio("- No existe un Prestamo con el identificador solicitado");
        }
    }
    
    @Transactional
    public void eliminarPrestamo(String idPrestamo) throws ErrorServicio {
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(idPrestamo);
        if (respuesta.isPresent()) {
            Prestamo prestamo = respuesta.get();
            if(prestamo.getAlta()==Boolean.FALSE){

            prestamoRepositorio.delete(prestamo);
            }else{
                throw new ErrorServicio(" - No se puede eliminar porque esta Activo");
            }
        } else {
            throw new ErrorServicio("- No existe un Prestamo con el identificador solicitado");
        }
    }
    
    public List<Prestamo> listarPrestamos() {
        return prestamoRepositorio.findAll();
    }
    
    public List<Prestamo> listarPrestamoPorFiltro(String alta) {
        if(!alta.equals("")){
         if(alta.equals("Activo")){
                return prestamoRepositorio.buscarPrestamoPorEstado(Boolean.TRUE);
         }else{
             return prestamoRepositorio.buscarPrestamoPorEstado(Boolean.FALSE);
         }
        }else{
            return prestamoRepositorio.findAll();
        }
    }
    
        public List<Prestamo> listarPrestamoPorCliente(String idCliente) {
        if(!idCliente.equals("")){
         
                return prestamoRepositorio.buscarPrestamoPorClienteLista(idCliente);
         }else{
            return prestamoRepositorio.findAll();
        }
    }
    
    public Prestamo buscarPrestamoPorId(String id) {
        return prestamoRepositorio.getById(id);
    }
    
    public Prestamo buscarPrestamoPorLibro(Libro libro) {
        return prestamoRepositorio.buscarPrestamoPorLibro(libro);
    }
    
    public Prestamo buscarPrestamoPorCliente(Cliente cliente) {
        return prestamoRepositorio.buscarPrestamoPorCliente(cliente);
    }
    
    public void validar(Date fechaPrestamo, Date fechaDevolucion,Libro libro,Cliente cliente)throws ErrorServicio{
        
        if(libro == null || libro.getAlta()==Boolean.FALSE){
            throw new ErrorServicio(" - Debe ingresar un libro Activo o no puede ser nulo o vacio");
        }
        
        if(cliente == null || cliente.getAlta()==Boolean.FALSE){
            throw new ErrorServicio("- Debe ingresar un cliente Activo o no puede ser nulo o vacio");
        }
    }
    
    public void validar2(Date fechaPrestamo, Date fechaDevolucion)throws ErrorServicio{
        if(fechaDevolucion.before(fechaPrestamo)){
            throw new ErrorServicio("- Debe ingresar una fecha mayor a devolucion");
        }
        
        if(fechaDevolucion == null){
            throw new ErrorServicio("- Debe ingresar una fecha devolucion, no puede ser nulo o vacio");
        }
    }
}
