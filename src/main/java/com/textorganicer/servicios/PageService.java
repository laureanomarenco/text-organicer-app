package com.textorganicer.servicios;

import com.textorganicer.negocio.dominios.Page;
import java.util.List;

public interface PageService {
    List<Page> getAll();
    Page findById(Integer id);
    List<Page> getAllByFolder(Integer idFolder);
    Page save(Page page);
    void delete(Page page);

}
