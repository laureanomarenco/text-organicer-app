package com.textorganicer.servicios;

import com.textorganicer.excepciones.ErrorProcessException;
import com.textorganicer.negocio.dominios.Page;
import com.textorganicer.negocio.dto.PageDTO;

import java.util.List;

public interface PageService {
    List<PageDTO> getAll() throws ErrorProcessException;
    PageDTO findById(Integer id) throws ErrorProcessException;
    List<PageDTO> getAllByFolder(Integer idFolder) throws ErrorProcessException;
    PageDTO save(Page page, Integer idFolder) throws ErrorProcessException;
    public PageDTO update(Integer id, Page page) throws ErrorProcessException;
    boolean delete(Integer id) throws ErrorProcessException;

}
