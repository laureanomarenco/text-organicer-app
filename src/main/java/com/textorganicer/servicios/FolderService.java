package com.textorganicer.servicios;


import com.textorganicer.excepciones.ErrorProcessException;
import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dto.FolderDTO;

import java.util.List;


public interface FolderService {
    List<FolderDTO> getAll() throws ErrorProcessException;
    FolderDTO findById(Integer id) throws ErrorProcessException;
    List<FolderDTO> getAllByUser(Integer idUser) throws ErrorProcessException;
    FolderDTO save(Folder folder, Integer idUser) throws ErrorProcessException;
    FolderDTO update(Folder folder, Integer id) throws ErrorProcessException;
    boolean delete(Integer id) throws ErrorProcessException;

}
