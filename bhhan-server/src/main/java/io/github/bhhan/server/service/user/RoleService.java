package io.github.bhhan.server.service.user;

import io.github.bhhan.server.domain.user.Role;
import io.github.bhhan.server.domain.user.RoleRepository;
import io.github.bhhan.server.service.user.exception.RoleException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getRole(String roleName){
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleException(String.format("%s을 찾을 수 없습니다.", roleName)));
    }

    public Role addRole(String roleName){
        try{
            return roleRepository.save(Role.builder().name(roleName).build());
        }catch (Exception e){
            throw new RoleException(String.format("%s 추가에 실패했습니다.", roleName));
        }
    }

    public void removeRole(String roleName){
        try {
            Long count = roleRepository.removeByName(roleName);

            if(count <= 0){
                throw new IllegalArgumentException(String.format("%s 삭제에 실패했습니다.", roleName));
            }
        }catch(Exception e){
            throw new RoleException(e.getMessage());
        }
    }
}
