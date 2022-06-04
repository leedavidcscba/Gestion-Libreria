/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.sistemalibreria.servicios;

import com.libreria.sistemalibreria.entidades.Autor;
import com.libreria.sistemalibreria.entidades.Editorial;
import com.libreria.sistemalibreria.entidades.Libro;
import com.libreria.sistemalibreria.errores.ErrorServicio;
import com.libreria.sistemalibreria.repositorios.AutorRepositorio;
import com.libreria.sistemalibreria.repositorios.EditorialRepositorio;
import com.libreria.sistemalibreria.repositorios.LibroRepositorio;
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
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Autowired
    AutorRepositorio autorRepositorio;

    @Transactional
    public void agregarLibro(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer idEditorial, Integer idAutor) throws ErrorServicio {

        validar(titulo, isbn, anio, ejemplares);

        Libro chequeado = libroRepositorio.buscarLibroPorTitulo(titulo);

        if (chequeado == null) {
            Editorial editorial = editorialRepositorio.findById(idEditorial).get();
            Autor autor = autorRepositorio.findById(idAutor).get();

            Libro libro = new Libro();

            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(0);
            libro.setEjemplaresRestantes(ejemplares);
            libro.setAlta(Boolean.TRUE);
            libro.setEditorial(editorial);
            libro.setAutor(autor);

            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("Autor ya existe en lista: " + chequeado.getTitulo());
        }
    }

    @Transactional
    public void modificarLibro(String idLibro, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, Integer idEditorial, Integer idAutor) throws ErrorServicio {

        validar2(titulo, isbn, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, idEditorial, idAutor);

        Optional<Libro> respuesta = libroRepositorio.findById(idLibro);
        Editorial editorial = editorialRepositorio.findById(idEditorial).get();
        Autor autor = autorRepositorio.findById(idAutor).get();

        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(ejemplaresPrestados);
            libro.setEjemplaresRestantes(ejemplares - ejemplaresPrestados);
            libro.setAlta(Boolean.TRUE);
            libro.setEditorial(editorial);
            libro.setAutor(autor);

            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No existe libro con el identificador solicitado");

        }

    }

    @Transactional
    public void deshabilitarLibro(String idLibro) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(idLibro);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();

            libro.setAlta(Boolean.FALSE);

            libroRepositorio.save(libro);

        } else {
            throw new ErrorServicio("No existe un libro con el identificador solicitado");
        }
    }

    @Transactional
    public void habilitarLibro(String idLibro) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(idLibro);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            if (libro.getEditorial().getAlta() == Boolean.TRUE && libro.getAutor().getAlta() == Boolean.TRUE) {
                libro.setAlta(Boolean.TRUE);

                libroRepositorio.save(libro);
            } else {
                throw new ErrorServicio(" - No se puede deshabilitar Libro, porque hay Autor e Editorial Activo");
            }
        } else {
            throw new ErrorServicio(" - No existe un libro con el identificador solicitado");
        }
    }

    @Transactional
    public void eliminarLibro(String idLibro) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(idLibro);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            if (libro.getAlta() == Boolean.FALSE) {
                libroRepositorio.delete(libro);
            } else {
                throw new ErrorServicio(" - No se puede deshabilitar Libro, porque esta activo");
            }
        } else {
            throw new ErrorServicio(" - No existe un libro con el identificador solicitado");
        }
    }

    @Transactional
    public void prestarLibro(String idLibro) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(idLibro);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            if (libro.getEjemplaresRestantes() < 1) {
                throw new ErrorServicio(" - No hay cantidad libros a prestar");
            } else {
                libro.setEjemplaresRestantes(libro.getEjemplares() - 1);
                libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() + 1);

                libroRepositorio.save(libro);
            }
        } else {
            throw new ErrorServicio(" - No existe un libro con el identificador solicitado");
        }
    }

    @Transactional
    public void devolverLibro(String idLibro) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(idLibro);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            if (libro.getEjemplaresRestantes() < libro.getEjemplares() || libro.getEjemplaresPrestados() == 0) {
                libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() + 1);
                libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() - 1);

                libroRepositorio.save(libro);
            }else{
                throw new ErrorServicio(" - No se puede devolver libro y dar baja prestamo, porque Ejemplares Prestados es Cero");
            }
        } else {
            throw new ErrorServicio(" - No existe un libro con el identificador solicitado");
        }

    }

    public List<Libro> listarLibro() {
        return libroRepositorio.findAll();
    }

    public List<Libro> listarLibroParaListado() {
        return libroRepositorio.buscarLibroParaListado();
    }

    public Libro buscarLibroPorId(String id) {
        return libroRepositorio.getById(id);
    }

    public Libro buscarLibroPorTitulo(String titulo) {
        return libroRepositorio.buscarLibroPorTitulo(titulo);
    }

    public List<Libro> listarLibroPorFiltro(String alta) {
        if (!alta.equals("")) {
            if (alta.equals("Activo")) {
                return libroRepositorio.buscarLibroPorEstado(Boolean.TRUE);
            } else {
                return libroRepositorio.buscarLibroPorEstado(Boolean.FALSE);
            }
        } else {
            return libroRepositorio.findAll();
        }
    }

    public List<Libro> listarLibroPorAutor(Integer id) {
        if (id != 0) {
            return libroRepositorio.buscarLibroPorAutor(id);
        } else {
            return libroRepositorio.findAll();
        }
    }

    public List<Libro> listarLibroPorEditorial(Integer id) {
        if (id != 0) {
            return libroRepositorio.buscarLibroPorEditorial(id);
        } else {
            return libroRepositorio.findAll();
        }
    }

    public void validar(String titulo, Long isbn, Integer anio, Integer ejemplares) throws ErrorServicio {
        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio(" - El titulo del libro no puede ser vacio o nulo");
        }
        if (isbn == null || isbn.toString().trim().isEmpty() || (isbn > 9 && isbn < 14)) {
            throw new ErrorServicio(" - El isbn no puede ser nulo o debe tener entre 10 a 13 digitos");
        }
        if (anio == null || anio.toString().trim().isEmpty()) {
            throw new ErrorServicio(" - El anio no puede ser nulo o debe ser yyyy");
        }
        if (ejemplares == null || ejemplares.toString().trim().isEmpty() || ejemplares <= 0) {
            throw new ErrorServicio(" - El ejemplares no puede ser nulo o vacio o mayor a 0");
        }
    }

    public void validar2(String titulo, Long isbn, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, Integer idEditorial, Integer idAutor) throws ErrorServicio {
        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio(" - El Titulo no puede ser vacio o nulo");
        }
        if (isbn == null || isbn.toString().trim().isEmpty() || (isbn > 9 && isbn < 14)) {
            throw new ErrorServicio(" - El isbn no puede ser nulo o debe tener entre 10 a 13 digitos");
        }
        if (anio == null || anio.toString().trim().isEmpty()) {
            throw new ErrorServicio(" - El anio no puede ser nulo o debe ser yyyy");
        }
        if (ejemplares == null || ejemplares.toString().trim().isEmpty() || ejemplares <= 0) {
            throw new ErrorServicio(" - El ejemplares no puede ser nulo, vacio o mayor a 0");
        }

        if (ejemplaresPrestados == null || ejemplaresPrestados.toString().trim().isEmpty() || ejemplaresPrestados < 0) {
            throw new ErrorServicio(" - El ejemplares Prestados no puede ser nulo, vacio o menores a 0");
        }
        if (ejemplaresRestantes == null || ejemplaresRestantes.toString().trim().isEmpty() || ejemplaresRestantes < 0) {
            throw new ErrorServicio(" - El ejemplares Restantes  no puede ser nulo, vacio o menores a 0");
        }
        if (idEditorial == null || idEditorial.toString().trim().isEmpty() || idEditorial < 0) {
            throw new ErrorServicio(" - El Editorial no puede ser nulo");
        }
        if (idAutor == null || idAutor.toString().trim().isEmpty() || idAutor < 0) {
            throw new ErrorServicio(" - El Autor no puede ser nulo");
        }
    }
}
