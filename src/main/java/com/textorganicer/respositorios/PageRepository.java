package com.textorganicer.respositorios;

import com.textorganicer.negocio.dominios.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PageRepository extends JpaRepository<Page, Integer> {
    @Query("from Page p where p.folder.id = ?1")
    Optional<List<Page>> getAllByFolder(Integer idFolder);
}
