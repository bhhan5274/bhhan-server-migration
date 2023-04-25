package io.github.bhhan.server.config.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@EnableCaching
public class CacheConfiguration {
    @Bean
    public CacheManager cacheManager(){
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        final ConcurrentMapCache skillCache = new ConcurrentMapCache("skill");
        final ConcurrentMapCache skillsCache = new ConcurrentMapCache("skills");
        final ConcurrentMapCache pagingCache = new ConcurrentMapCache("paging");
        final ConcurrentMapCache projectCache = new ConcurrentMapCache("project");

        simpleCacheManager.setCaches(Arrays.asList(projectCache, skillsCache, skillCache, pagingCache));

        return simpleCacheManager;
    }
}
