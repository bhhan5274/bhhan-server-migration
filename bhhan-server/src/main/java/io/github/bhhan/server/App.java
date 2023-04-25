package io.github.bhhan.server;

import io.github.bhhan.server.config.cache.CacheConfiguration;
import io.github.bhhan.server.config.dashboard.DashboardConfiguration;
import io.github.bhhan.server.config.dashboard.UserConfiguration;
import io.github.bhhan.server.config.security.CorsConfiguration;
import io.github.bhhan.server.config.security.SecurityConfiguration;
import io.github.bhhan.server.config.storage.LocalStorageConfiguration;
import io.github.bhhan.server.config.storage.S3StorageConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({
        ServerConfig.class,
        S3StorageConfiguration.class,
        CacheConfiguration.class,
        LocalStorageConfiguration.class,
        DashboardConfiguration.class,
        UserConfiguration.class,
        SecurityConfiguration.class,
        CorsConfiguration.class
})
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
