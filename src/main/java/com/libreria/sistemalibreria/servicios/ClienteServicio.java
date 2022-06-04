/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.sistemalibreria.servicios;

import com.libreria.sistemalibreria.entidades.Cliente;
import com.libreria.sistemalibreria.errores.ErrorServicio;
import com.libreria.sistemalibreria.repositorios.ClienteRepositorio;
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
public class ClienteServicio {
    
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    
    @Transactional
    public void agregarCliente(String nombre, String apellido, Long dni, String telefono) throws ErrorServicio {
        validar(nombre, apellido, dni, telefono);
        
        Cliente chequeado = clienteRepositorio.buscarClientePorNombre(nombre,apellido);
        if(chequeado == null){
        Cliente cliente = new Cliente();
        cliente.setDni(dni);
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setTelefono(telefono);
        cliente.setAlta(Boolean.TRUE);

        clienteRepositorio.save(cliente);
        }else{
            throw new ErrorServicio("Cliente ya existe en lista: " + chequeado.getNombre() + " "+ chequeado.getApellido());
        }
    }
    
    @Transactional
    public void modificarCliente(String idCliente, Long dni, String nombre, String apellido, String telefono) throws ErrorServicio {

        validar(nombre, apellido, dni, telefono);

        Optional<Cliente> respuesta = clienteRepositorio.findById(idCliente);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setDni(dni);
            cliente.setTelefono(telefono);
            cliente.setAlta(Boolean.TRUE);

            clienteRepositorio.save(cliente);
        } else {
            throw new ErrorServicio("No se encontro el cliente solicitado");
        }
    }
    
    @Transactional
    public void deshabilitarCliente(String idCliente) throws ErrorServicio {
        Optional<Cliente> respuesta = clienteRepositorio.findById(idCliente);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
            cliente.setAlta(Boolean.FALSE);

            clienteRepositorio.save(cliente);
        } else {
            throw new ErrorServicio("No se encontro el cliente solicitado");
        }
    }

    @Transactional
    public void habilitarCliente(String idCliente) throws ErrorServicio {
        Optional<Cliente> respuesta = clienteRepositorio.findById(idCliente);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
            cliente.setAlta(Boolean.TRUE);

            clienteRepositorio.save(cliente);
        } else {
            throw new ErrorServicio("No se encontro el cliente solicitado");
        }
    }
    
    @Transactional
    public void eliminarCliente(String idCliente) throws ErrorServicio {
        Optional<Cliente> respuesta = clienteRepositorio.findById(idCliente);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
            if(cliente.getAlta()== Boolean.FALSE){
            clienteRepositorio.delete(cliente);
            }else{
               throw new ErrorServicio("No se puede eliminar, porque cliente esta activo, debe darlo baja"); 
            }
        } else {
            throw new ErrorServicio("No existe un Cliente con el identificador solicitado");
        }
    }

    public List<Cliente> listarClientesParaListado() {
        return clienteRepositorio.buscarAutorParaListado();
    }
    
    public List<Cliente> listarClientes() {
        return clienteRepositorio.findAll();
    }
    
    public List<Cliente> listarClientePorFiltro(String alta) {
        if(!alta.equals("")){
         if(alta.equals("Activo")){
                return clienteRepositorio.buscarAutorPorEstado(Boolean.TRUE);
         }else{
             return clienteRepositorio.buscarAutorPorEstado(Boolean.FALSE);
         }
        }else{
            return clienteRepositorio.findAll();
        }
    }

    public Cliente buscarClienteNombre(String nombre, String apellido) {
        return clienteRepositorio.buscarClientePorNombre(nombre, apellido);
    }

    public Cliente buscarClienteId(String idCliente) {
        return clienteRepositorio.buscarClientePorId(idCliente);
    }
    
    public void validarCliente(Cliente cliente) throws ErrorServicio{
        Optional<Cliente> respuesta = clienteRepositorio.findById(cliente.getId());
        if (!respuesta.isPresent()) {
            throw new ErrorServicio("No existe un Cliente con el identificador solicitado, debe cargarlo"); 
        }
    }
    
    public void validar(String nombre, String apellido, Long dni, String telefono) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del cliente no puede ser vacio o nulo");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido del cliente no puede ser vacio o nulo");
        }
        
        if (dni == null|| dni.toString().trim().isEmpty()|| dni < 9) {
            throw new ErrorServicio("El DNI no puede ser nulo, vacio o mayor a 9 digitos");
        }
        
        if (telefono == null || telefono.isEmpty()) {
            throw new ErrorServicio("El telefono del cliente no puede ser vacio o nulo");
        }
    }
}
