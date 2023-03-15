package com.textorganicer.servicios;

import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.respositorios.FolderRepository;
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
    public Optional<Folder> findById(Integer id) {
        return this.repository.findById(id);
    }

    @Override
    public Optional<List<Folder>> getAllByUser(Integer idUser) {
        return this.repository.getAllByUser(idUser);
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
