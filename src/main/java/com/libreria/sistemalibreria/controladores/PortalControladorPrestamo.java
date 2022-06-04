/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.sistemalibreria.controladores;

import com.libreria.sistemalibreria.entidades.Cliente;
import com.libreria.sistemalibreria.entidades.Libro;
import com.libreria.sistemalibreria.errores.ErrorServicio;
import com.libreria.sistemalibreria.servicios.ClienteServicio;
import com.libreria.sistemalibreria.servicios.LibroServicio;
import com.libreria.sistemalibreria.servicios.PrestamoServicio;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class PortalControladorPrestamo {
    
    @Autowired
    private PrestamoServicio prestamoServicio;
    
    @Autowired
    private LibroServicio libroServicio;
    
    @Autowired
    private ClienteServicio clienteServicio;
    
    @GetMapping("/agregarprestamo")
    public String agregarPrestamo(ModelMap modelo) {
        List<Cliente> listaclientes = clienteServicio.listarClientesParaListado();
        List<Libro> listalibros = libroServicio.listarLibroParaListado();
        modelo.addAttribute("Clientes", listaclientes);
        modelo.addAttribute("Libros", listalibros);
        return "agregarprestamo.html";
    }
    
    @GetMapping("/listarprestamo")
    public String listarprestamo(ModelMap modelo) {
        List<Cliente> listaClientes = clienteServicio.listarClientesParaListado();
            modelo.addAttribute("Clientes",listaClientes);
        modelo.addAttribute("prestamos", prestamoServicio.listarPrestamos());
        return "listarprestamo.html";
    }
    
    @PostMapping("/registrarprestamo")
    public String registrarPrestamo(ModelMap modelo, @RequestParam String fechaPrestamo, @RequestParam String fechaDevolucion, @RequestParam String idCliente, @RequestParam String libroTitulo,RedirectAttributes redirectAttrs) throws ParseException {

        try {
            
            Libro auxLibro = libroServicio.buscarLibroPorTitulo(libroTitulo);
            Cliente auxCliente = clienteServicio.buscarClienteId(idCliente);
            SimpleDateFormat formatoDateFecha = new SimpleDateFormat("yyyy-mm-dd");
            Date fechaPrestamoAux=formatoDateFecha.parse(fechaPrestamo);
            Date fechaDevolucionAux=formatoDateFecha.parse(fechaDevolucion);
            prestamoServicio.agregarPrestamo(fechaPrestamoAux, fechaDevolucionAux, auxLibro, auxCliente);
            redirectAttrs
            .addFlashAttribute("mensaje", "Prestamo agregado correctamente")
            .addFlashAttribute("clase", "success");
            return "redirect:/agregarprestamo";
        } catch (ErrorServicio ex) {
            //System.out.println(ex.getMessage());
            modelo.put("mensaje1","Error al cargar Prestamo "+ex.getMessage());
            modelo.put("clase1", "danger");
            modelo.put("fechaPrestamo", fechaPrestamo);
            modelo.put("fechaDevolucion", fechaDevolucion);
            List<Libro> listalibros = libroServicio.listarLibroParaListado();
            List<Cliente> listaclientes = clienteServicio.listarClientesParaListado();
            modelo.addAttribute("Libros", listalibros);
            modelo.addAttribute("Clientes", listaclientes);
            return "agregarprestamo.html";
        }

    }
    
    //Prestamo controlador editar, alta, baja, eliminar
    @GetMapping("/prestamo/editar/{id}")
    public String mostrarFormularioEditarP(@PathVariable String id, ModelMap modelo) {
            List<Libro> listalibros = libroServicio.listarLibroParaListado();
            List<Cliente> listaclientes = clienteServicio.listarClientesParaListado();
            modelo.addAttribute("Libros", listalibros);
            modelo.addAttribute("Clientes", listaclientes);
        modelo.addAttribute("prestamoAux", prestamoServicio.buscarPrestamoPorId(id));
        return "editarprestamo.html";
    }

    @PostMapping("/prestamo/editar/{id}")
    public String modificarPrestamo(ModelMap modelo, @RequestParam String id, @RequestParam String fechaPrestamo, @RequestParam String fechaDevolucion1, @RequestParam String idCliente, @RequestParam String libroTitulo, RedirectAttributes redirectAttrs) throws ParseException {

        try {
            SimpleDateFormat formatoDateFecha = new SimpleDateFormat("yyyy-mm-dd");
            Date fechaPrestamoAux=formatoDateFecha.parse(fechaPrestamo);
            Date fechaDevolucionAux=formatoDateFecha.parse(fechaDevolucion1);
            prestamoServicio.modificarPrestamo(id, fechaPrestamoAux, fechaDevolucionAux);
            redirectAttrs
            .addFlashAttribute("mensaje", "Prestamo Editado correctamente")
            .addFlashAttribute("clase", "success");
            return "redirect:/listarprestamo";//en este html <h2 class="display-4" th:text="$(titulo)"></h2> <p th:text="$(descripcion)"></p>
        } catch (ErrorServicio ex) {
            ex.printStackTrace();
            modelo.put("mensaje1","Error al editar libro "+ex.getMessage());
            modelo.put("clase1", "danger");
            modelo.put("fechaPrestamo", fechaPrestamo);
            modelo.put("fechaDevolucion", fechaDevolucion1);
            List<Libro> listalibros = libroServicio.listarLibroParaListado();
            List<Cliente> listaclientes = clienteServicio.listarClientesParaListado();
            modelo.addAttribute("Libros", listalibros);
            modelo.addAttribute("Clientes", listaclientes);
            return "editarprestamo";
        }
    }

    @PostMapping("/prestamo/eliminar")
    public String eliminarPrestamo(ModelMap modelo, @RequestParam String idE, RedirectAttributes redirectAttrs) {
        try {
            prestamoServicio.eliminarPrestamo(idE);
            redirectAttrs
            .addFlashAttribute("mensaje", "Prestamo Eliminado correctamente")
            .addFlashAttribute("clase", "success");
            return "redirect:/listarprestamo";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al eliminar Prestamo "+ex.getMessage());
            modelo.put("clase1", "danger");
            return "listarprestamo";
        }
    }

    @PostMapping("/prestamo/deshabilitar")
    public String deshabilitarPrestamo(ModelMap modelo, @RequestParam String idD, RedirectAttributes redirectAttrs) {
        try {
            prestamoServicio.deshabilitarPrestamo(idD);
            redirectAttrs
            .addFlashAttribute("mensaje", "Prestamo Deshabilitado correctamente")
            .addFlashAttribute("clase", "success");
            return "redirect:/listarprestamo";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al deshabilitar Prestamo "+ex.getMessage());
            modelo.put("clase1", "danger");
            return "listarprestamo";
        }  
    }
    
    @PostMapping("/prestamo/habilitar")
    public String habilitarPrestamo(ModelMap modelo, @RequestParam String id, RedirectAttributes redirectAttrs) {
        try {
            prestamoServicio.habilitarPrestamo(id);
            redirectAttrs
            .addFlashAttribute("mensaje", "Prestamo Habilitado correctamente")
            .addFlashAttribute("clase", "success");
            return "redirect:/listarprestamo";
        } catch (ErrorServicio ex) {
            modelo.put("mensaje1","Error al habilitar Prestamo "+ex.getMessage());
            modelo.put("clase1", "danger");
            return "listarprestamo";
        }  
    }
    
    @PostMapping("/prestamo/filtrar")
    public String filtrarPrestamoA(ModelMap modelo, @RequestParam String filtro){
        modelo.addAttribute("prestamos",prestamoServicio.listarPrestamoPorFiltro(filtro));
        return "listarprestamo";
    }
    
    @PostMapping("/prestamo/filtrarC")
    public String filtrarPrestamoC(ModelMap modelo, @RequestParam String idCliente){
            List<Cliente> listaClientes = clienteServicio.listarClientes();
            modelo.addAttribute("Clientes",listaClientes);
            modelo.addAttribute("prestamos",prestamoServicio.listarPrestamoPorCliente(idCliente));
        return "listarprestamo";
    }
    
}
