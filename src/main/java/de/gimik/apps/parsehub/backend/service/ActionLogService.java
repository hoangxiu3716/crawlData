package de.gimik.apps.parsehub.backend.service;


import org.springframework.data.domain.Page;

import de.gimik.apps.parsehub.backend.model.ActionLog;
import de.gimik.apps.parsehub.backend.model.search.ActionLogSearchInfo;

import java.util.Map;

public interface ActionLogService {
	void log(String object, String action, String content, String ip);
    void log(String object, String action, String content, String ip, String user);
	ActionLog getByID(long actionLogID);

    Page<ActionLog> getAll(int pageIndex, int pageSize, String sortField, String sortDirection, ActionLogSearchInfo searchInfo);
    Page<ActionLog> getAll(int pageIndex, int pageSize, String sortField, String sortDirection);
}