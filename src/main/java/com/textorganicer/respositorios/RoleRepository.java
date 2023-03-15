package com.textorganicer.respositorios;

import com.textorganicer.negocio.dominios.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<List<Role>> getAllByFolderId(Integer id_folder);

    @Query("from Role r where r.user.id = ?1 AND r.role_type = 'collaborator'")
    Optional<List<Role>> findShared(Integer idUser);

    @Query("from Role r where r.user.id = ?1 AND r.folder.id = ?2")
    Optional<Role> findByIdAndFolder(Integer idUser, Integer idFolder);

}
