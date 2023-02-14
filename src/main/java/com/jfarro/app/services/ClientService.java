package com.jfarro.app.services;

import com.jfarro.app.models.entity.Client;
import com.jfarro.app.models.entity.DocumentType;
import com.jfarro.app.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<Client> findAllClient();
    Page<Client> findAllPagesClient(Pageable pageable);
    Optional<Client> findByIdClient(Long id);
    Client saveClient(Client client);
    void updateStateClient(Byte state, Long id);

    List<DocumentType> findAllDocumentTypes();
    Page<DocumentType> findAllDocumentType(Pageable pageable);
    Optional<DocumentType> findByIdDocumentType(Long id);
}
