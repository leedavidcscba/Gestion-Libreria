/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.sistemalibreria.controladores;

import com.libreria.sistemalibreria.errores.ErrorServicio;
import com.libreria.sistemalibreria.servicios.EditorialServicio;
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
public class PortalControladorEditorial {
    
    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/agregareditorial")
    public String agregareditorial() {
        return "agregareditorial.html";
    }

    @GetMapping("/listareditorial")
    public String listareditorial(ModelMap modelo) {
        modelo.addAttribute("editorial", editorialServicio.listarEditoriales());
        return "listareditorial.html";
    }

    @PostMapping("/registrareditorial")
    public String registrarEditorial(ModelMap modelo, @RequestParam String nombre,RedirectAttributes redirectAttrs) {

        try {
            editorialServicio.agregarEditorial(nombre);
            redirectAttrs
            .addFlashAttribute("mensaje", "Editorial agregado correctamente")
            .addFlashAttribute("clase", "success");
            return "redirect:/agregareditorial";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al cargar Editorial "+ex.getMessage());
            modelo.put("clase1", "danger");
            modelo.put("nombre", nombre);
            return "agregareditorial.html";
        }
    }
    
    
    @PostMapping("/registrareditorialmodal")
    public String registrarEditorialModal(ModelMap modelo, @RequestParam String nombre, RedirectAttributes redirectAttrs) {

        try {

            editorialServicio.agregarEditorial(nombre);
            redirectAttrs
            .addFlashAttribute("mensaje", "Editorial Agregado correctamente en Lista, elija uno")
            .addFlashAttribute("clase", "success");
            return "redirect:/agregarlibro";

        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al cargar Editorial "+ex.getMessage());
            modelo.put("clase1", "danger");
            modelo.put("nombre", nombre);
            return "agregareditorial.html";
        }
        
    }

    //Editorial editar, alta, baja y eliminar
    @GetMapping("/editorial/editar/{id}")
    public String mostrarFormularioEditarEd(ModelMap modelo, @PathVariable Integer id) {
        modelo.addAttribute("editorial", editorialServicio.buscarEditorialPorId(id));
        return "editareditorial.html";
    }

    @PostMapping("/editorial/editar/{id}")
    public String modificarEditorial(ModelMap modelo, @RequestParam Integer id, @RequestParam String nombre1, RedirectAttributes redirectAttrs) {

        try {
            editorialServicio.modificarEditorial(id, nombre1);
            redirectAttrs
            .addFlashAttribute("mensaje", "Editorial Editado correctamente")
            .addFlashAttribute("clase", "success");
            return "redirect:/listareditorial";
        } catch (ErrorServicio ex) {
            ex.printStackTrace();
            modelo.put("mensaje1","Error al editar Editorial "+ex.getMessage());
            modelo.put("clase1", "danger");
            modelo.put("nombre", nombre1);// en html registro para que lo use en los input th:value="$(nombre)"
            return "editareditorial";
        }
    }

    @PostMapping("/editorial/eliminar")
    public String eliminarEditorial(ModelMap modelo, @RequestParam Integer id, RedirectAttributes redirectAttrs) {
        try {
            editorialServicio.eliminarEditorial(id);
            redirectAttrs
            .addFlashAttribute("mensaje", "Editorial Eliminado correctamente")
            .addFlashAttribute("clase", "success");

            return "redirect:/listareditorial";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al eliminar Editorial "+ex.getMessage());
            modelo.put("clase1", "danger");
            return "redirect:/listareditorial";
        }
    }

    @PostMapping("/editorial/deshabilitar")
    public String deshabilitarEditorial(ModelMap modelo, @RequestParam Integer id, RedirectAttributes redirectAttrs) {
        try {
            editorialServicio.deshabilitarEditorial(id);
            redirectAttrs
            .addFlashAttribute("mensaje", "Editorial Deshabilitado correctamente")
            .addFlashAttribute("clase", "success");

            return "redirect:/listareditorial";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al deshabilitar Editorial "+ex.getMessage());
            modelo.put("clase1", "danger");
            return "redirect:/listareditorial";
        }  
    }
    
    @PostMapping("/editorial/alta")
    public String habilitarEditorial(ModelMap modelo, @RequestParam Integer id, RedirectAttributes redirectAttrs) {
        try {
            editorialServicio.habilitarEditorial(id);
            redirectAttrs
            .addFlashAttribute("mensaje", "Editorial Habilitado correctamente")
            .addFlashAttribute("clase", "success");

            return "redirect:/listareditorial";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al habilitar Editorial "+ex.getMessage());
            modelo.put("clase1", "danger");
            return "redirect:/listareditorial";
        }  
    }
    
    @PostMapping("/editorial/filtrar")
    public String filtrarEditorial(ModelMap modelo, @RequestParam String filtro){
        modelo.addAttribute("editorial",editorialServicio.listarAutoresPorFiltro(filtro));
        return "listareditorial";
    }
}
