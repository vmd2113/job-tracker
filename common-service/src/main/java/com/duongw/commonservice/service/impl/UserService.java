package com.duongw.commonservice.service.impl;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.exception.AlreadyExistedException;
import com.duongw.common.exception.ResourceNotFoundException;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.commonservice.model.dto.request.user.CreateUserRequest;
import com.duongw.commonservice.model.dto.request.user.UpdateUserRequest;
import com.duongw.commonservice.model.dto.response.user.UserDetailDTO;
import com.duongw.commonservice.model.dto.response.user.UserResponseDTO;
import com.duongw.commonservice.model.entity.UserRole;
import com.duongw.commonservice.model.entity.Users;
import com.duongw.commonservice.repository.UserRepository;
import com.duongw.commonservice.repository.search.UserSearchRepository;
import com.duongw.commonservice.service.IItemService;
import com.duongw.commonservice.service.IUserRoleService;
import com.duongw.commonservice.service.IUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final IUserRoleService userRoleService;
    private final IItemService itemService;
    private final UserSearchRepository userSearchRepository;


    @Autowired
    public UserService(UserRepository userRepository, IUserRoleService userRoleService, IItemService itemService, UserSearchRepository userSearchRepository) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.itemService = itemService;
        this.userSearchRepository = userSearchRepository;
    }

    private UserResponseDTO convertToUserResponseDTO(Users user) {
        if (user == null) {
            return null;
        }
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUserId(user.getUserId());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setPassword(user.getPassword());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setPhoneNumber(user.getPhoneNumber());
        userResponseDTO.setFirstName(user.getFirstName());
        userResponseDTO.setLastName(user.getLastName());
        userResponseDTO.setDepartmentId(user.getDepartmentId());
        userResponseDTO.setStatus(user.getStatus());
        return userResponseDTO;
    }




    @Override
    public List<UserResponseDTO> getAllUser() {
        return userRepository.findAll().stream().map(this::convertToUserResponseDTO).toList();
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        Users user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("user.not-found")));
        return convertToUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        try {
            Users user = userRepository.findByUsername(username);
            return convertToUserResponseDTO(user);
        } catch (Exception e) {
            throw new ResourceNotFoundException(Translator.toLocate("user.not-found"));
        }
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        try {
            Users user = userRepository.findByEmail(email);
            return convertToUserResponseDTO(user);
        } catch (Exception e) {
            throw new ResourceNotFoundException(Translator.toLocate("user.not-found"));
        }
    }

    @Override
    public UserResponseDTO getUserByPhoneNumber(String phoneNumber) {
        Users user = userRepository.findByPhoneNumber(phoneNumber);
        return convertToUserResponseDTO(user);
    }


    private boolean validateUser(@Valid CreateUserRequest user) {
        log.info("USER_SERVICE  -> validateUser");

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AlreadyExistedException(Translator.toLocate("validate.email.exist"));
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new AlreadyExistedException(Translator.toLocate("validate.username.exist"));
        }
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new AlreadyExistedException(Translator.toLocate("validate.phone-number.exist"));
        }
        return true;
    }

    @Override
    public UserResponseDTO createUser(CreateUserRequest user) {
        log.info("USER_SERVICE  -> createUser");

        if (!validateUser(user)) {
            throw new AlreadyExistedException(Translator.toLocate("user.exist"));
        }

        Users newUser = new Users();
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());

        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setDepartmentId(user.getDepartmentId());
        //TODO: set status
//        newUser.setStatus(user.getStatus());


        newUser.setStatus(1L);
        //TODO: set user role
        UserRole userRole = new UserRole();
        userRole.setUserId(newUser.getUserId());
        userRole.setRoleId(7L);
        userRole.setCreatedByUser(1L);
        userRole.setUpdatedByUser(1L);


        return convertToUserResponseDTO(userRepository.save(newUser));
    }

    @Override
    public UserResponseDTO updateUser(Long id, UpdateUserRequest user) {
        log.info("USER_SERVICE  -> updateUser");
        Users updateUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("user.not-found")));
        updateUser.setFirstName(user.getFirstName());
        updateUser.setLastName(user.getLastName());
        updateUser.setDepartmentId(user.getDepartmentId());
        userRepository.save(updateUser);

        updateUser.setCreatedByUser(1L);
        updateUser.setUpdatedByUser(1L);
        return convertToUserResponseDTO(updateUser);
    }

    @Override
    public UserResponseDTO updateUserStatus(Long id, String status) {
        log.info("USER_SERVICE  -> updateUserStatus");
        Users updateUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("user.not-found")));
        //TODO: set status
        updateUser.setStatus(1L);
        userRepository.save(updateUser);
        updateUser.setCreatedByUser(1L);
        updateUser.setUpdatedByUser(1L);
        return convertToUserResponseDTO(updateUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("USER_SERVICE  -> deleteUser");
        Users findUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("user.not-found")));
        userRepository.delete(findUser);
    }


    // internal

    @Override
    public UserDetailDTO getUserDetail(Long id) {
        return convertToUserDetailDTO(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("user.not-found"))));
    }

    @Override
    public UserDetailDTO getUserDetailByUsername(String username) {
        log.info("USER_SERVICE  -> getUserDetailByUsername");
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException(Translator.toLocate("user.not-found"));
        }
        return convertToUserDetailDTO(user);

    }


    private UserDetailDTO convertToUserDetailDTO(Users user) {
        log.info("USER_SERVICE  -> convertToUserDetailDTO");
        UserDetailDTO userDetailDTO = new UserDetailDTO();
        userDetailDTO.setUserId(user.getUserId());
        userDetailDTO.setUsername(user.getUsername());
        userDetailDTO.setEmail(user.getEmail());
        userDetailDTO.setPassword(user.getPassword());
        userDetailDTO.setPhoneNumber(user.getPhoneNumber());
        userDetailDTO.setFirstName(user.getFirstName());
        userDetailDTO.setLastName(user.getLastName());
        userDetailDTO.setDepartmentId(user.getDepartmentId());
        userDetailDTO.setStatus(user.getStatus());
        userDetailDTO.setRoles(itemService.getRoleByUserId(user.getUserId()));
        return userDetailDTO;
    }

    @Transactional
    @Override
    public UserDetailDTO registerUser(CreateUserRequest user) {
        log.info("USER_SERVICE  -> registerUser");
        Users newUser = new Users();
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());
//        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setDepartmentId(user.getDepartmentId());
        //TODO: set status
        newUser.setStatus(1L);
        Users saveUser = userRepository.save(newUser);

        UserRole userRole = new UserRole();
        userRole.setUserId(newUser.getUserId());
        userRole.setRoleId(7L);
        Long userRoleId = userRoleService.save(userRole);
        return convertToUserDetailDTO(saveUser);
    }

    @Override
    public UserDetailDTO updatePassword(String username, String password) {
        log.info("USER_SERVICE  -> updatePassword");
        Users user = userRepository.findByUsername(username);
//        user.setPassword(passwordEncoder.encode(password));
        return convertToUserDetailDTO(userRepository.save(user));
    }

    @Override
    public PageResponse<?> searchUserByCriteria(int pageNo, int pageSize, String usernameSearch, String emailSearch, String phoneNumberSearch, String firstNameSearch, String sortBy, String sortDirection) {
        log.info("USER_SERVICE  -> searchUserByCriteria");
        return userSearchRepository.searchUserByCriteria(pageNo, pageSize, usernameSearch, emailSearch, phoneNumberSearch, firstNameSearch, sortBy, sortDirection);
    }
}
