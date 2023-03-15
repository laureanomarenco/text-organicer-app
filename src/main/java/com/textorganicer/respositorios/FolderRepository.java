package com.textorganicer.respositorios;

import com.textorganicer.negocio.dominios.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder, Integer> {

    @Query("from Folder f where f.user.id = ?1")
    Optional<List<Folder>> getAllByUser(Integer idUser);
}
