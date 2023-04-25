package io.github.bhhan.server.service.user;

import io.github.bhhan.server.domain.user.Account;
import io.github.bhhan.server.domain.user.AccountRepository;
import io.github.bhhan.server.domain.user.Role;
import io.github.bhhan.server.service.user.exception.AccountException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public Account addAccount(String email, String password, List<String> roles){
        try{
            List<Role> findRoles = findRoles(roles);

            Account account = Account.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .roles(findRoles)
                    .build();

            return accountRepository.save(account);

        }catch(Exception e){
            throw new AccountException("ACCOUNT 추가에 실패했습니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        return new User(account.getEmail(), account.getPassword(), getGrantedAuthorities(account));
    }

    private List<Role> findRoles(List<String> roles){
        List<Role> findRoles = new ArrayList<>();

        for (String role : roles) {
            findRoles.add(roleService.getRole(role));
        }

        return findRoles;
    }

    private List<SimpleGrantedAuthority> getGrantedAuthorities(Account account){
        return account.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());
    }
}
