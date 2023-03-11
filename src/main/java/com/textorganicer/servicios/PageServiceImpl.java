package com.textorganicer.servicios;

import com.textorganicer.negocio.dominios.Page;
import com.textorganicer.respositorios.PageRepository;
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
    public Optional<Page> findById(Integer id) {
        return this.repository.findById(id);
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
