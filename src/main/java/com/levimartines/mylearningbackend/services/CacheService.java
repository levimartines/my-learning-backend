package com.levimartines.mylearningbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CacheService {

    private final CacheManager cacheManager;

    public void evict(String name, String key) {
        Cache cache = cacheManager.getCache(name);
        if (cache == null) {
            log.error("Error removing value from cache. NAME [{}] KEY [{}]", name, key);
            return;
        }
        cache.evict(key);
    }
}
