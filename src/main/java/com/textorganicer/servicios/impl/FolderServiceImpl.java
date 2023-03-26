package com.textorganicer.servicios.impl;

import com.textorganicer.excepciones.NotFoundException;
import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.respositorios.FolderRepository;
import com.textorganicer.servicios.FolderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FolderServiceImpl implements FolderService {

    private final FolderRepository repository;

    public FolderServiceImpl(FolderRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Folder> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Folder findById(Integer id) {
        Optional<Folder> folder = this.repository.findById(id);
        if(folder.isEmpty()) throw new NotFoundException("No hay ninguna carpeta con ese id");

        return folder.get();
    }

    @Override
    public List<Folder> getAllByUser(Integer idUser) {
        Optional<List<Folder>> all = this.repository.getAllByUser(idUser);
        if(all.isEmpty()) throw new NotFoundException("Este usuario no tiene carpetas");
        return all.get();
    }

    @Override
    public Folder save(Folder folder) {
        return this.repository.save(folder);
    }

    @Override
    public void delete(Folder folder) {
        this.repository.delete(folder);
    }


}
