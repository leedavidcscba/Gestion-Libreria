/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.sistemalibreria.repositorios;

import com.libreria.sistemalibreria.entidades.Cliente;
import com.libreria.sistemalibreria.entidades.Libro;
import com.libreria.sistemalibreria.entidades.Prestamo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author leedavidcuellar
 */
public interface PrestamoRepositorio extends JpaRepository<Prestamo, String>{
    @Query("SELECT c FROM Prestamo c WHERE c.id = :id")
    public Prestamo buscarPrestamoPorId(@Param("id") String id);
    
     @Query("SELECT c FROM Prestamo c WHERE c.libro = :libro")
    public Prestamo buscarPrestamoPorLibro(@Param("libro") Libro libro);
    
     @Query("SELECT c FROM Prestamo c WHERE c.cliente = :cliente")
    public Prestamo buscarPrestamoPorCliente(@Param("cliente") Cliente cliente);
    
    @Query("SELECT c FROM Prestamo c WHERE c.alta = :alta")
    public List<Prestamo> buscarPrestamoPorEstado(@Param("alta") Boolean alta);
    
    @Query("SELECT c FROM Prestamo c WHERE c.cliente.id = :cliente AND c.cliente.alta = true")
    public List<Prestamo> buscarPrestamoPorClienteLista(@Param("cliente") String cliente);
}
