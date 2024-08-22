package com.etiya.bayi_satis.service;

import com.etiya.bayi_satis.dto.BasketDto;
import com.etiya.bayi_satis.dto.RegistrationDto;
import com.etiya.bayi_satis.entity.Role;
import com.etiya.bayi_satis.entity.Sale;
import com.etiya.bayi_satis.entity.User;
import com.etiya.bayi_satis.repository.RoleRepository;
import com.etiya.bayi_satis.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUsers(){
        List<User> users = userRepository.findAll();
        return users;
    }

    public void saveUser(RegistrationDto registrationDto) {
        User user = new User();

        Role role = roleRepository.findByRolename("USER")
                .orElseThrow(() -> new RuntimeException());

        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRoles(Arrays.asList(role));


        userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void updateAccount(User user){
        if(user == null){
            throw new RuntimeException();
        }
        Optional<User> existingUser = userRepository.findById(user.getId());

        if(!existingUser.isPresent()){
            throw new RuntimeException("Wanted user could not found with id:" + user.getId());
        }
        existingUser.get().setUsername(user.getUsername());
        existingUser.get().setEmail(user.getEmail());

        userRepository.save(existingUser.get());
    }

    public boolean deleteUser(String userId){
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()){
            userRepository.delete(user.get());
            return true;
        }else{
            return false;
        }
    }

}
