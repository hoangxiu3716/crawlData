package de.gimik.apps.parsehub.backend.service;

public interface ThreadingService {
    void execute(Runnable thread);

    void executeIgnoreError(Runnable thread);
}
