package io.github.bhhan.server.config.storage;

import io.github.bhhan.storage.local.FileSystemStorageConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Import({FileSystemStorageConfiguration.class})
public class LocalStorageConfiguration {
}
