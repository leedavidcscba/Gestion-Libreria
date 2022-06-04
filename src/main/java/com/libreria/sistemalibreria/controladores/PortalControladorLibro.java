/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.sistemalibreria.controladores;

import com.libreria.sistemalibreria.entidades.Autor;
import com.libreria.sistemalibreria.entidades.Editorial;
import com.libreria.sistemalibreria.errores.ErrorServicio;
import com.libreria.sistemalibreria.servicios.AutorServicio;
import com.libreria.sistemalibreria.servicios.EditorialServicio;
import com.libreria.sistemalibreria.servicios.LibroServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author leedavidcuellar
 */
@Controller
public class PortalControladorLibro {
    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private EditorialServicio editorialServicio;

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/agregarlibro")
    public String agregarlibro(ModelMap modelo) {
        List<Autor> listaautores = autorServicio.listarAutores();
        List<Editorial> listaeditoriales = editorialServicio.listarEditoriales();
        modelo.addAttribute("Autores", listaautores);
        modelo.addAttribute("Editoriales", listaeditoriales);
        return "agregarlibro.html";
    }


    @GetMapping("/listarlibro")
    public String listarlibro(ModelMap modelo) {
            List<Autor> listaautores = autorServicio.listarAutoresParaListado();
            List<Editorial> listaeditoriales = editorialServicio.listarEditorialesParaListado();
            modelo.addAttribute("Autores", listaautores);
            modelo.addAttribute("Editoriales", listaeditoriales);
        modelo.addAttribute("libros", libroServicio.listarLibro());
        return "listarlibro.html";
    }

    @PostMapping("/registrarlibro")
    public String registrarLibro(ModelMap modelo, @RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam String idAutor, @RequestParam String idEditorial,RedirectAttributes redirectAttrs) {

        try {
            Autor aux1 = autorServicio.buscarAutorNombre(idAutor);
            Editorial aux2 = editorialServicio.buscarEditorialNombre(idEditorial);
            libroServicio.agregarLibro(isbn, titulo, anio, ejemplares, aux2.getId(), aux1.getId());
            redirectAttrs
            .addFlashAttribute("mensaje", "Libro agregado correctamente")
            .addFlashAttribute("clase", "success");
            return "redirect:/agregarlibro";
        } catch (ErrorServicio ex) {
            //System.out.println(ex.getMessage());
            modelo.put("mensaje1","Error al cargar Libro "+ex.getMessage());
            modelo.put("clase1", "danger");
            modelo.put("isbn", isbn);
            modelo.put("titulo", titulo);
            modelo.put("año", anio);
            modelo.put("ejemplares", ejemplares);
            List<Autor> listaautores = autorServicio.listarAutoresParaListado();
            List<Editorial> listaeditoriales = editorialServicio.listarEditorialesParaListado();
            modelo.addAttribute("Autores", listaautores);
            modelo.addAttribute("Editoriales", listaeditoriales);
            return "agregarlibro.html";
        }

    }
    
    @PostMapping("/registrarlibromodal")
    public String registrarLibroModal(ModelMap modelo, @RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam String idAutor, @RequestParam String idEditorial,RedirectAttributes redirectAttrs) {

        try {

            Autor aux1 = autorServicio.buscarAutorNombre(idAutor);
            Editorial aux2 = editorialServicio.buscarEditorialNombre(idEditorial);
            libroServicio.agregarLibro(isbn, titulo, anio, ejemplares, aux2.getId(), aux1.getId());
            redirectAttrs
            .addFlashAttribute("mensaje", "Editorial Agregado correctamente en Lista, elija uno")
            .addFlashAttribute("clase", "success");
            return "redirect:/agregarprestamo";

        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al cargar Libro "+ex.getMessage());
            modelo.put("clase1", "danger");
            modelo.put("isbn", isbn);
            modelo.put("titulo", titulo);
            modelo.put("año", anio);
            modelo.put("ejemplares", ejemplares);
            List<Autor> listaautores = autorServicio.listarAutoresParaListado();
            List<Editorial> listaeditoriales = editorialServicio.listarEditorialesParaListado();
            modelo.addAttribute("Autores", listaautores);
            modelo.addAttribute("Editoriales", listaeditoriales);
            return "agregarlibro.html";
        }
        
    }
    
    
    //Libro controlador editar, alta, baja, eliminar
    @GetMapping("/libro/editar/{id}")
    public String mostrarFormularioEditarL(@PathVariable String id, ModelMap modelo) {
        List<Autor> listaautores = autorServicio.listarAutoresParaListado();
        List<Editorial> listaeditoriales = editorialServicio.listarEditorialesParaListado();
        modelo.addAttribute("Autores", listaautores);
        modelo.addAttribute("Editoriales", listaeditoriales);
        modelo.addAttribute("libroAux", libroServicio.buscarLibroPorId(id));
        return "editarlibro.html";
    }

    @PostMapping("/libro/editar/{id}")
    public String modificarLibro(ModelMap modelo, @RequestParam String id, @RequestParam Long isbn, @RequestParam String titulo1, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados, @RequestParam Integer ejemplaresRestantes, @RequestParam String idEditorial, @RequestParam String idAutor, RedirectAttributes redirectAttrs) {

        try {
            Autor auxA = autorServicio.buscarAutorNombre(idAutor);
            Editorial auxE = editorialServicio.buscarEditorialNombre(idEditorial);
            libroServicio.modificarLibro(id, isbn, titulo1, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, auxE.getId(), auxA.getId());
            redirectAttrs
            .addFlashAttribute("mensaje", "Libro Editado correctamente")
            .addFlashAttribute("clase", "success");
            return "redirect:/listarlibro";//en este html <h2 class="display-4" th:text="$(titulo)"></h2> <p th:text="$(descripcion)"></p>
        } catch (ErrorServicio ex) {
            ex.printStackTrace();
            modelo.put("mensaje1","Error al editar libro "+ex.getMessage());
            modelo.put("clase1", "danger");
            modelo.put("isbn", isbn);
            modelo.put("titulo", titulo1);
            modelo.put("año", anio);
            modelo.put("ejemplares", ejemplares);
            modelo.put("ejemplaresPrestados", ejemplaresPrestados);
            modelo.put("ejemplaresRestantes", ejemplaresRestantes);
            List<Autor> listaautores = autorServicio.listarAutoresParaListado();
            List<Editorial> listaeditoriales = editorialServicio.listarEditorialesParaListado();
            modelo.addAttribute("Autores", listaautores);
            modelo.addAttribute("Editoriales", listaeditoriales);
            return "editarlibro";
        }
    }

    @PostMapping("/libro/eliminar")
    public String eliminarLibro(ModelMap modelo, @RequestParam String idE, RedirectAttributes redirectAttrs) {
        try {
            libroServicio.eliminarLibro(idE);
            redirectAttrs
            .addFlashAttribute("mensaje", "Libro Eliminado correctamente")
            .addFlashAttribute("clase", "success");
            return "redirect:/listarlibro";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al eliminar Libro "+ex.getMessage());
            modelo.put("clase1", "danger");
            return "listarlibro";
        }
    }

    @PostMapping("/libro/deshabilitar")
    public String deshabilitarLibro(ModelMap modelo, @RequestParam String idD, RedirectAttributes redirectAttrs) {
        try {
            libroServicio.deshabilitarLibro(idD);
            redirectAttrs
            .addFlashAttribute("mensaje", "Libro Deshabilitado correctamente")
            .addFlashAttribute("clase", "success");
            return "redirect:/listarlibro";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al deshabilitar Libro "+ex.getMessage());
            modelo.put("clase1", "danger");
            return "listarlibro";
        }  
    }
    
    @PostMapping("/libro/habilitar")
    public String habilitarLibro(ModelMap modelo, @RequestParam String id, RedirectAttributes redirectAttrs) {
        try {
            libroServicio.habilitarLibro(id);
            redirectAttrs
            .addFlashAttribute("mensaje", "Libro Habilitado correctamente")
            .addFlashAttribute("clase", "success");
            return "redirect:/listarlibro";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al habilitar Libro "+ex.getMessage());
            modelo.put("clase1", "danger");
            return "listarlibro";
        }  
    }
    
    @PostMapping("/libro/filtrarE")
    public String filtrarLibroE(ModelMap modelo, @RequestParam String filtro){
            List<Autor> listaautores = autorServicio.listarAutoresParaListado();
            List<Editorial> listaeditoriales = editorialServicio.listarEditorialesParaListado();
            modelo.addAttribute("Autores", listaautores);
            modelo.addAttribute("Editoriales", listaeditoriales);
        modelo.addAttribute("libros",libroServicio.listarLibroPorFiltro(filtro));
        return "listarlibro";
    }
    
    @PostMapping("/libro/filtrarEd")
    public String filtrarLibroEd(ModelMap modelo, @RequestParam Integer idEditorial){
            List<Autor> listaautores = autorServicio.listarAutoresParaListado();
            List<Editorial> listaeditoriales = editorialServicio.listarEditorialesParaListado();
            modelo.addAttribute("Autores", listaautores);
            modelo.addAttribute("Editoriales", listaeditoriales);
        modelo.addAttribute("libros",libroServicio.listarLibroPorEditorial(idEditorial));
        return "listarlibro";
    }
    
    @PostMapping("/libro/filtrarA")
    public String filtrarLibroA(ModelMap modelo, @RequestParam Integer idAutor){
            List<Autor> listaautores = autorServicio.listarAutoresParaListado();
            List<Editorial> listaeditoriales = editorialServicio.listarEditorialesParaListado();
            modelo.addAttribute("Autores", listaautores);
            modelo.addAttribute("Editoriales", listaeditoriales);
        modelo.addAttribute("libros",libroServicio.listarLibroPorAutor(idAutor));
        return "listarlibro";
    }
}
