package io.github.bhhan.server.config.dashboard;

import io.github.bhhan.server.domain.user.AccountRepository;
import io.github.bhhan.server.domain.user.RoleRepository;
import io.github.bhhan.server.service.user.AccountService;
import io.github.bhhan.server.service.user.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class DashboardConfiguration {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    @Bean
    public AccountService accountService(RoleService roleService, PasswordEncoder passwordEncoder){
        return new AccountService(accountRepository, passwordEncoder, roleService);
    }

    @Bean
    public RoleService roleService(){
        return new RoleService(roleRepository);
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
