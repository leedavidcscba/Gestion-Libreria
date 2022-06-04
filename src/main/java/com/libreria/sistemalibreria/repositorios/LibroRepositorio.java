/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.sistemalibreria.repositorios;
import com.libreria.sistemalibreria.entidades.Libro;
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
public interface LibroRepositorio extends JpaRepository<Libro, String>{
    @Query("SELECT c FROM Libro c WHERE c.id = :id")
    public List<Libro> buscarLibroPorId(@Param("id") String id);
    
    @Query("SELECT c FROM Libro c WHERE c.alta = true")
    public List<Libro> buscarLibroParaListado();
    
    @Query("SELECT c FROM Libro c WHERE c.autor.id = :id")
    public List<Libro> buscarLibroPorAutor(@Param("id") Integer id);
    
     @Query("SELECT c FROM Libro c WHERE c.editorial.id = :id")
    public List<Libro> buscarLibroPorEditorial(@Param("id") Integer id);
    
    @Query("SELECT c FROM Libro c WHERE c.titulo = :titulo")
    public Libro buscarLibroPorTitulo(@Param("titulo") String titulo);
    
    @Query("SELECT c FROM Libro c WHERE c.alta = :alta")
    public List<Libro> buscarLibroPorEstado(@Param("alta") Boolean alta);
}
