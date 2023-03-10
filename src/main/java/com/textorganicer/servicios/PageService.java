package com.textorganicer.servicios;

import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.Page;

import java.util.List;
import java.util.Optional;

public interface PageService {
    List<Page> getAll();
    Optional<Page> findById(Integer id);
    Page save(Page page);
    void delete(Page page);
}
