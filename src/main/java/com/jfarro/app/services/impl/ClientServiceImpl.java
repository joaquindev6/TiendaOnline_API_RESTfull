package com.jfarro.app.services.impl;

import com.jfarro.app.exceptions.FindByIdException;
import com.jfarro.app.exceptions.NotExistException;
import com.jfarro.app.models.entity.Client;
import com.jfarro.app.models.entity.DocumentType;
import com.jfarro.app.repositories.ClientRepository;
import com.jfarro.app.repositories.DocumentTypeRepository;
import com.jfarro.app.services.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final DocumentTypeRepository documentTypeRepository;

    public ClientServiceImpl(ClientRepository clientRepository, DocumentTypeRepository documentTypeRepository) {
        this.clientRepository = clientRepository;
        this.documentTypeRepository = documentTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAllClient() {
        return this.clientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Client> findAllPagesClient(Pageable pageable) {
        return this.clientRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findByIdClient(Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error findByIdClient: El ID no puede ser null o menor igual a 0");
        }
        return this.clientRepository.findById(id);
    }

    @Override
    @Transactional
    public Client saveClient(Client client) {
        if (client == null) {
            throw new NullPointerException("Error saveClient: El objeto Client es null");
        }
        return this.clientRepository.save(client);
    }

    @Override
    @Transactional
    public void updateStateClient(Byte state, Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error updateStateClient: El ID no puede ser null o menor igual a 0");
        }
        this.clientRepository.findById(id).orElseThrow(() -> new NotExistException("Error updateStateClient: El cliente no existe en la base de datos."));
        this.clientRepository.updateState(state, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentType> findAllDocumentTypes() {
        return this.documentTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentType> findAllDocumentType(Pageable pageable) {
        return this.documentTypeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentType> findByIdDocumentType(Long id) {
        if (id == null || id <= 0) {
            throw new FindByIdException("Error findByIdDocumentType: El ID no puede ser null o menor igual a 0");
        }
        return this.documentTypeRepository.findById(id);
    }
}
