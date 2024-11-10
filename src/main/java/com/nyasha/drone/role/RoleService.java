package com.nyasha.drone.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    public void create(RoleRequest request) {
        var role = Role.builder()
                .name(request.getName())
                .build();
        roleRepository.save(role);
    }

    public Role findById(int id) {
        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public void delete(int id) {
        roleRepository.deleteById(id);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public RoleRequest getRole(int id, RoleRequest request) {
        var role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        request.setName(role.getName());
        request.setId(role.getId());
        return request;
    }

    public void update(int id, RoleRequest request) {
        var role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        role.setName(request.getName());
        roleRepository.save(role);
    }
}
