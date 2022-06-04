/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.sistemalibreria.controladores;

import com.libreria.sistemalibreria.errores.ErrorServicio;
import com.libreria.sistemalibreria.servicios.AutorServicio;
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
public class PortalControladorAutor {

    @Autowired
    private AutorServicio autorServicio;


    @GetMapping("/agregarautor")
    public String agregarautor() {
        return "agregarautor.html";
    }
    
    @GetMapping("/listarautor")
    public String listarautor(ModelMap modelo) {
        modelo.addAttribute("autor", autorServicio.listarAutores());
        return "listarautor.html";
    }
    
    

    @PostMapping("/registrarautor")
    public String registrarAutor(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido,RedirectAttributes redirectAttrs) {

        try {

            autorServicio.agregarAutor(nombre, apellido);
            redirectAttrs
            .addFlashAttribute("mensaje", "Autor agregado correctamente")
            .addFlashAttribute("clase", "success");
        return "redirect:/agregarautor";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al agregar Autor "+ex.getMessage());
            modelo.put("clase1", "danger");
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            return "agregarautor.html";
        }
    }
    
    @PostMapping("/registrarautormodal")
    public String registrarAutorModal(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido, RedirectAttributes redirectAttrs) {

        try {

            autorServicio.agregarAutor(nombre, apellido);
            redirectAttrs
            .addFlashAttribute("mensaje", "Autor agregado correctamente en la lista, elija uno")
            .addFlashAttribute("clase", "success");
            return "redirect:/agregarlibro";

        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al cargar autor "+ex.getMessage());
            modelo.put("clase1", "danger");
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            return "agregarautor.html";
        }
        
    }
    
    //Autor controlador para editar, baja, alta, eliminar
    @GetMapping("/autor/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, ModelMap modelo) {
        modelo.addAttribute("autor", autorServicio.buscarAutorId(id));
        return "editarautor.html";
    }

    @PostMapping("/autor/editar/{id}")
    public String modificarAutor(ModelMap modelo, @RequestParam Integer id, @RequestParam String nombre1, @RequestParam String apellido, RedirectAttributes redirectAttrs) {

        try {
            autorServicio.modificarAutor(id, nombre1, apellido);
            redirectAttrs
            .addFlashAttribute("mensaje", "Autor Editado correctamente")
            .addFlashAttribute("clase", "success");
            return "redirect:/listarautor";
        } catch (ErrorServicio ex) {
            ex.printStackTrace();
            modelo.put("mensaje1","Error al editar Autor "+ex.getMessage());
            modelo.put("clase1", "danger");
            modelo.put("nombre", nombre1);// en html registro para que lo use en los input th:value="$(nombre)"
            modelo.put("apellido", apellido);
            return "editarautor";
        }
    }

    @PostMapping("/autor/eliminar")
    public String eliminarAutor(ModelMap modelo, @RequestParam Integer idE,RedirectAttributes redirectAttrs) {
        try {
            autorServicio.eliminarAutor(idE);
            redirectAttrs
            .addFlashAttribute("mensaje", "Autor Eliminado correctamente")
            .addFlashAttribute("clase", "success");

            return "redirect:/listarautor";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al eliminar Autor "+ex.getMessage());
            modelo.put("clase1", "danger");
            return "listarautor";
        }
    }

    @PostMapping("/autor/deshabilitar")
    public String deshabilitarAutor(ModelMap modelo, @RequestParam Integer idD, RedirectAttributes redirectAttrs) {
        try {
            autorServicio.deshabilitarAutor(idD);
            redirectAttrs
            .addFlashAttribute("mensaje", "Autor Deshabilitado correctamente")
            .addFlashAttribute("clase", "success");

            return "redirect:/listarautor";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al deshabilitar Autor "+ex.getMessage());
            modelo.put("clase1", "danger");
            return "listarautor";
        }  
    }
    
    @PostMapping("/autor/alta")
    public String habilitarAutor(ModelMap modelo, @RequestParam Integer id, RedirectAttributes redirectAttrs) {
        try {
            autorServicio.habilitarAutor(id);
            redirectAttrs
            .addFlashAttribute("mensaje", "Autor Habilitado correctamente")
            .addFlashAttribute("clase", "success");

            return "redirect:/listarautor";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al habilitar Autor "+ex.getMessage());
            modelo.put("clase1", "danger");
            return "listarautor";
        }  
    }
    
    @PostMapping("/autor/filtrar")
    public String filtrarAutor(ModelMap modelo, @RequestParam String filtro){
        modelo.addAttribute("autor",autorServicio.listarAutoresPorFiltro(filtro));
        return "listarautor";
    }
}
