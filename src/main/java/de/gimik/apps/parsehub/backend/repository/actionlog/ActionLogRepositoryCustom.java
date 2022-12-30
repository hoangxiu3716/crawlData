package de.gimik.apps.parsehub.backend.repository.actionlog;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.gimik.apps.parsehub.backend.model.ActionLog;
import de.gimik.apps.parsehub.backend.model.search.ActionLogSearchInfo;

public interface ActionLogRepositoryCustom {
    public Page<ActionLog> findAll(Pageable pageable, ActionLogSearchInfo searchInfo);
}
