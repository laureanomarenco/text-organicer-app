package com.textorganicer.servicios;


import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.User;

import java.util.List;
import java.util.Optional;

public interface FolderService {
    List<Folder> getAll();
    Folder findById(Integer id);
    List<Folder> getAllByUser(Integer idUser);
    Folder save(Folder folder);
    void delete(Folder folder);

}
