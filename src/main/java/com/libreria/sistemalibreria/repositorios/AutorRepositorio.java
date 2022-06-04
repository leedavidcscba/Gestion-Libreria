/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.sistemalibreria.repositorios;
import com.libreria.sistemalibreria.entidades.Autor;
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
public interface AutorRepositorio extends JpaRepository<Autor, Integer> {
    @Query("SELECT c FROM Autor c WHERE c.id = :id")
    public Autor buscarAutorPorId(@Param("id") Integer id);
    
    @Query("SELECT c FROM Autor c WHERE c.alta = true")
    public List<Autor> buscarAutorParaListado();
    
    @Query("SELECT c FROM Autor c WHERE c.nombre = :nombre")
    public Autor buscarAutorPorNombre(@Param("nombre") String nombre);
    
    @Query("SELECT c FROM Autor c WHERE c.alta = :alta")
    public List<Autor> buscarAutorPorEstado(@Param("alta") Boolean alta);
    
}
