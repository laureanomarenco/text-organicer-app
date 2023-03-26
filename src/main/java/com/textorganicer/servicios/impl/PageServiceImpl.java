package com.textorganicer.servicios.impl;

import com.textorganicer.excepciones.NotFoundException;
import com.textorganicer.negocio.dominios.Page;
import com.textorganicer.respositorios.PageRepository;
import com.textorganicer.servicios.PageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PageServiceImpl implements PageService {

    private final PageRepository repository;

    public PageServiceImpl(PageRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Page> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Page findById(Integer id) {
        Optional<Page> page = this.repository.findById(id);
        if (page.isEmpty()) throw new NotFoundException("No hay ninguna página con ese id");
        return page.get();
    }

    @Override
    public List<Page> getAllByFolder(Integer idFolder) {
        Optional<List<Page>> all = this.repository.getAllByFolder(idFolder);
        if(all.isEmpty()) throw new NotFoundException("No hay páginas en esta carpeta");

        return all.get();
    }

    @Override
    public Page save(Page page) {
        return this.repository.save(page);
    }

    @Override
    public void delete(Page page) {
        this.repository.delete(page);
    }
}
