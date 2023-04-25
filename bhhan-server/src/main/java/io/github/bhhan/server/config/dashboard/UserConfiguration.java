package io.github.bhhan.server.config.dashboard;

import io.github.bhhan.server.domain.user.Role;
import io.github.bhhan.server.service.user.AccountService;
import io.github.bhhan.server.service.user.RoleService;
import io.github.bhhan.server.service.user.exception.RoleException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

@RequiredArgsConstructor
public class UserConfiguration {
    private final AccountService accountService;
    private final RoleService roleService;

    @Value("${user.username}")
    private String username;

    @Value("${user.password}")
    private String password;

    @Bean
    public ApplicationRunner initUser(){
        return args -> {
            registerRoles();
            registerAccount();
        };
    }

    private void registerRoles(){
        try{
            roleService.getRole(Role.ADMIN);
        }catch(RoleException e){
            roleService.addRole(Role.ADMIN);
        }
    }

    private void registerAccount(){
        System.out.println(username + " : " + password);
        try{
            accountService.loadUserByUsername(username);
        }catch(UsernameNotFoundException e){
            accountService.addAccount(username, password, Collections.singletonList(Role.ADMIN));
        }
    }
}
