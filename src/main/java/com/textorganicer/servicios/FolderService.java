package com.textorganicer.servicios;


import com.textorganicer.negocio.dominios.Folder;

import java.util.List;


public interface FolderService {
    List<Folder> getAll();
    Folder findById(Integer id);
    List<Folder> getAllByUser(Integer idUser);
    Folder save(Folder folder);
    void delete(Folder folder);

}
