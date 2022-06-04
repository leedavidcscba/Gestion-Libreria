/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.sistemalibreria.controladores;

import com.libreria.sistemalibreria.errores.ErrorServicio;
import com.libreria.sistemalibreria.servicios.ClienteServicio;
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
public class PortalControladorCliente {
    
    @Autowired
    private ClienteServicio clienteServicio;
    
    @GetMapping("/agregarcliente")
    public String agregarcliente() {
        return "agregarcliente.html";
    }
    
    @GetMapping("/listarcliente")
    public String listarcliente(ModelMap modelo) {
        modelo.addAttribute("cliente", clienteServicio.listarClientes());
        return "listarcliente.html";
    }
    
    @PostMapping("/registrarcliente")
    public String registrarcliente(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido,@RequestParam Long dni,@RequestParam String telefono,RedirectAttributes redirectAttrs) {

        try {

            clienteServicio.agregarCliente(nombre, apellido, dni, telefono);
            redirectAttrs
            .addFlashAttribute("mensaje", "Cliente agregado correctamente")
            .addFlashAttribute("clase", "success");
        return "redirect:/agregarcliente";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al agregar Autor "+ex.getMessage());
            modelo.put("clase1", "danger");
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("dni", dni);
            modelo.put("telefono", telefono);
            return "agregarcliente.html";
        }
    }
    
    @PostMapping("/registrarclientemodal")
    public String registrarClienteModal(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido,@RequestParam Long dni,@RequestParam String telefono,RedirectAttributes redirectAttrs) {

        try {

            clienteServicio.agregarCliente(nombre, apellido, dni, telefono);
            redirectAttrs
            .addFlashAttribute("mensaje", "Cliente agregado correctamente")
            .addFlashAttribute("clase", "success");
            return "redirect:/agregarcliente";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al agregar Autor "+ex.getMessage());
            modelo.put("clase1", "danger");
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("dni", dni);
            modelo.put("telefono", telefono);
            return "agregarcliente.html";
        }
        
    }
    
    //Cliente controlador para editar, baja, alta, eliminar
    @GetMapping("/cliente/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable String id, ModelMap modelo) {
        modelo.addAttribute("cliente", clienteServicio.buscarClienteId(id));
        return "editarcliente.html";
    }

    @PostMapping("/cliente/editar/{id}")
    public String modificarCliente(ModelMap modelo, @RequestParam String id, @RequestParam String nombre1, @RequestParam String apellido, @RequestParam Long dni,@RequestParam String telefono, RedirectAttributes redirectAttrs) {

        try {
            clienteServicio.modificarCliente(id, dni, nombre1, apellido, telefono);
            redirectAttrs
            .addFlashAttribute("mensaje", "Autor Editado correctamente")
            .addFlashAttribute("clase", "success");
            return "redirect:/listarcliente";
        } catch (ErrorServicio ex) {
            ex.printStackTrace();
            modelo.put("mensaje1","Error al editar Autor "+ex.getMessage());
            modelo.put("clase1", "danger");
            modelo.put("nombre", nombre1);// en html registro para que lo use en los input th:value="$(nombre)"
            modelo.put("apellido", apellido);
            modelo.put("dni", dni);
            modelo.put("telefono", telefono);
            return "editarcliente";
        }
    }

    @PostMapping("/cliente/eliminar")
    public String eliminarCliente(ModelMap modelo, @RequestParam String idE,RedirectAttributes redirectAttrs) {
        try {
            clienteServicio.eliminarCliente(idE);
            redirectAttrs
            .addFlashAttribute("mensaje", "Cliente Eliminado correctamente")
            .addFlashAttribute("clase", "success");

            return "redirect:/listarcliente";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al eliminar Cliente "+ex.getMessage());
            modelo.put("clase1", "danger");
            return "listarcliente";
        }
    }

    @PostMapping("/cliente/deshabilitar")
    public String deshabilitarCliente(ModelMap modelo, @RequestParam String idD, RedirectAttributes redirectAttrs) {
        try {
            clienteServicio.deshabilitarCliente(idD);
            redirectAttrs
            .addFlashAttribute("mensaje", "Cliente Deshabilitado correctamente")
            .addFlashAttribute("clase", "success");

            return "redirect:/listarcliente";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al deshabilitar Cliente "+ex.getMessage());
            modelo.put("clase1", "danger");
            return "listarcliente";
        }  
    }
    
    @PostMapping("/cliente/alta")
    public String habilitarAutor(ModelMap modelo, @RequestParam String id, RedirectAttributes redirectAttrs) {
        try {
            clienteServicio.habilitarCliente(id);
            redirectAttrs
            .addFlashAttribute("mensaje", "Autor Habilitado correctamente")
            .addFlashAttribute("clase", "success");

            return "redirect:/listarcliente";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al habilitar Autor "+ex.getMessage());
            modelo.put("clase1", "danger");
            return "listarcliente";
        }  
    }
    
    @PostMapping("/cliente/filtrar")
    public String filtrarCliente(ModelMap modelo, @RequestParam String filtro){
        modelo.addAttribute("cliente",clienteServicio.listarClientePorFiltro(filtro));
        return "listarcliente";
    }
    
}
