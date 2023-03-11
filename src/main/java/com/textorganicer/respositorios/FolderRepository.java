package com.textorganicer.respositorios;

import com.textorganicer.negocio.dominios.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Integer> {

}
