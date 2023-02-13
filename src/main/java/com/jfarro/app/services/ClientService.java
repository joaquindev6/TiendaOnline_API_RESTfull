package com.jfarro.app.services;

import com.jfarro.app.models.entity.Client;
import com.jfarro.app.models.entity.DocumentType;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<Client> findAllClient();
    Optional<Client> findByIdClient(Long id);
    Client saveClient(Client client);
    void updateStateClient(Byte state, Long id);

    List<DocumentType> findAllDocumentTypes();
    Optional<DocumentType> findByIdDocumentType(Long id);
}
