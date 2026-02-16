package com.br.sisgetrim.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class ImportProgressService {
    private final Map<String, Integer> progresses = new ConcurrentHashMap<>();

    public void setProgress(Long entidadeId, int count) {
        progresses.put(entidadeId.toString(), count);
    }

    public int getProgress(Long entidadeId) {
        return progresses.getOrDefault(entidadeId.toString(), 0);
    }

    public void clearProgress(Long entidadeId) {
        progresses.remove(entidadeId.toString());
    }
}
