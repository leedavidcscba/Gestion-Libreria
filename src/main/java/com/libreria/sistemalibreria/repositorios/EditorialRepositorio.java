/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.sistemalibreria.repositorios;
import com.libreria.sistemalibreria.entidades.Editorial;
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
public interface EditorialRepositorio extends JpaRepository<Editorial, Integer> {
    @Query("SELECT c FROM Editorial c WHERE c.id = :id")
    public Editorial buscarEditorialPorId(@Param("id") Integer id);
    
    @Query("SELECT c FROM Editorial c WHERE c.alta = true")
    public List<Editorial> buscarEditorialParaListado();
    
    @Query("SELECT c FROM Editorial c WHERE c.nombre = :nombre")
    public Editorial buscarEditorialPorNombre(@Param("nombre") String nombre);
    
    @Query("SELECT c FROM Editorial c WHERE c.alta = :alta")
    public List<Editorial> buscarEditorialPorEstado(@Param("alta") Boolean alta);
}
