/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.sistemalibreria.repositorios;

import com.libreria.sistemalibreria.entidades.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author leedavidcuellar
 */
@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String>{
    
    @Query("SELECT c FROM Cliente c WHERE c.id = :id")
    public Cliente buscarClientePorId(@Param("id") String id);
    
    @Query("SELECT c FROM Cliente c WHERE c.alta = true")
    public List<Cliente> buscarAutorParaListado();
    
    @Query("SELECT c FROM Cliente c WHERE c.nombre = :nombre AND c.apellido = :apellido")
    public Cliente buscarClientePorNombre(@Param("nombre") String nombre, @Param("apellido")String apellido);
    
    @Query("SELECT c FROM Cliente c WHERE c.alta = :alta")
    public List<Cliente> buscarAutorPorEstado(@Param("alta") Boolean alta);
    
}
