package com.duongw.commonservice.service.impl;

import com.duongw.common.config.i18n.Translator;
import com.duongw.common.exception.AlreadyExistedException;
import com.duongw.common.exception.InvalidDataException;
import com.duongw.common.exception.ResourceNotFoundException;
import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.commonservice.model.dto.request.user.*;
import com.duongw.commonservice.model.dto.response.user.UserDetailDTO;
import com.duongw.commonservice.model.dto.response.user.UserResponseDTO;
import com.duongw.commonservice.model.entity.Department;
import com.duongw.commonservice.model.entity.UserRole;
import com.duongw.commonservice.model.entity.Users;
import com.duongw.commonservice.repository.DepartmentRepository;
import com.duongw.commonservice.repository.UserRepository;
import com.duongw.commonservice.repository.search.UserSearchRepository;
import com.duongw.commonservice.service.IItemService;
import com.duongw.commonservice.service.IUserRoleService;
import com.duongw.commonservice.service.IUserService;
import com.duongw.commonservice.validator.UserValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    private final DepartmentRepository departmentRepository;
    private final UserValidator userValidator;


    @Autowired
    public UserService(UserRepository userRepository, IUserRoleService userRoleService, IItemService itemService, UserSearchRepository userSearchRepository, PasswordEncoder passwordEncoder, DepartmentRepository departmentRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.itemService = itemService;
        this.userSearchRepository = userSearchRepository;
        this.passwordEncoder = passwordEncoder;
        this.departmentRepository = departmentRepository;
        this.userValidator = userValidator;
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

        if (user.getDepartment() != null) {
            userResponseDTO.setDepartmentId(user.getDepartment().getDepartmentId());
        } else {
            userResponseDTO.setDepartmentId(null);
        }
        userResponseDTO.setStatus(user.getStatus());
        log.info("USER_SERVICE  -> convertToUserResponseDTO");
        log.info("UserResponseDTO: {}", userResponseDTO);
        return userResponseDTO;
    }


    @Override
    public List<UserResponseDTO> getAllUser() {
        log.info("USER_SERVICE  -> getAllUser");
        return userRepository.findAll().stream().map(this::convertToUserResponseDTO).toList();
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        log.info("USER_SERVICE  -> getUserById");
        Users user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("user.not-found")));
        return convertToUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        try {
            log.info("USER_SERVICE  -> getUserByUsername");
            Users user = userRepository.findByUsername(username);
            return convertToUserResponseDTO(user);
        } catch (Exception e) {
            log.error("USER_SERVICE  -> getUserByUsername fail");
            throw new ResourceNotFoundException(Translator.toLocate("user.not-found"));
        }
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        try {
            log.info("USER_SERVICE  -> getUserByEmail");
            Users user = userRepository.findByEmail(email);
            return convertToUserResponseDTO(user);
        } catch (Exception e) {
            log.error("USER_SERVICE  -> getUserByEmail fail");
            throw new ResourceNotFoundException(Translator.toLocate("user.not-found"));
        }
    }

    @Override
    public UserResponseDTO getUserByPhoneNumber(String phoneNumber) {
        Users user = userRepository.findByPhoneNumber(phoneNumber);
        return convertToUserResponseDTO(user);
    }






    // for admin
    @Override
    public UserResponseDTO createUser(CreateUserRequest user) {
        log.info("USER_SERVICE  -> createUser");
        userValidator.validateCreateUser(user);

        Users newUser = new Users();
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());

        String defaultPassword = user.getUsername() + "@123";
        newUser.setPassword(passwordEncoder.encode(defaultPassword));
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        if (user.getDepartmentId() != null) {
            Department department = departmentRepository.findById(user.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("department.not-found")));
            newUser.setDepartment(department);
        }
        //TODO: set status

        newUser.setStatus(1L);
        log.info("USER_SERVICE  -> createUser");
        //TODO: set user role
        UserRole userRole = new UserRole();
        userRole.setUserId(newUser.getUserId());
        userRole.setRoleId(8L);
        userRole.setCreatedByUser(1L);
        userRole.setUpdatedByUser(1L);
        log.info("USER_SERVICE  -> create user role");
        log.info("USER_SERVICE  -> create user success");
        return convertToUserResponseDTO(userRepository.save(newUser));
    }

    @Override
    public UserResponseDTO updateUser(Long id, UpdateUserRequest user) {
        try{
            log.info("USER_SERVICE  -> updateUser");
            userValidator.validateUpdateUser(user, id);
            Users updateUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("user.not-found")));
            updateUser.setUsername(user.getUsername());
            updateUser.setEmail(user.getEmail());
            updateUser.setPhoneNumber(user.getPhoneNumber());
            updateUser.setFirstName(user.getFirstName());
            updateUser.setLastName(user.getLastName());
            updateUser.setStatus(user.getStatus());


            if (user.getDepartmentId() != null) {
                Department department = departmentRepository.findById(user.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("department.not-found")));
                updateUser.setDepartment(department);
            }
            userRepository.save(updateUser);


            updateUser.setCreatedByUser(1L);
            updateUser.setUpdatedByUser(1L);
            return convertToUserResponseDTO(updateUser);

        }catch (AlreadyExistedException e){
            throw new AlreadyExistedException(e.getMessage());
        }catch (Exception e){
            throw new RuntimeException(e);
        }

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
    public UserResponseDTO changeUserPassword(Long id, ChangePasswordRequest changePasswordRequest) {
        if (!validateChangePassword(id, changePasswordRequest)) {
            log.error("USER_SERVICE  -> changeUserPassword fail");
            throw new InvalidDataException("Invalid data");
        }
        Users user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
        return convertToUserResponseDTO(userRepository.save(user));
    }

    @Override
    public UserDetailDTO changeUserInfo(Long id, UpdateUserInfoRequest updateUserInfoRequest) {

        userValidator.validateChangeUserInfo(updateUserInfoRequest, id);
        Users user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setFirstName(updateUserInfoRequest.getFirstName());
        user.setLastName(updateUserInfoRequest.getLastName());
        user.setDepartment(departmentRepository.findById(updateUserInfoRequest.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found")));
        user.setEmail(updateUserInfoRequest.getEmail());
        user.setPhoneNumber(updateUserInfoRequest.getPhoneNumber());
        userRepository.save(user);
        return convertToUserDetailDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        log.info("USER_SERVICE  -> deleteUser");
        Users findUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocate("user.not-found")));
        userRepository.delete(findUser);
    }

    @Override
    public void deleteUserList(List<Long> ids) {
        for (Long id : ids) {
            deleteUser(id);
        }
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
            log.error("USER_SERVICE  -> getUserDetailByUsername fail");
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

        if (user.getDepartment() != null) {
            userDetailDTO.setDepartmentId(user.getDepartment().getDepartmentId());
            userDetailDTO.setDepartmentName(user.getDepartment().getDepartmentName());
        } else {
            userDetailDTO.setDepartmentId(null);
            userDetailDTO.setDepartmentName(null);
        }

        userDetailDTO.setStatus(user.getStatus());
        userDetailDTO.setRoles(itemService.getRoleByUserId(user.getUserId()));
        return userDetailDTO;
    }

    @Transactional
    @Override
    public UserDetailDTO registerUser(RegisterRequest user) {
        log.info("USER_SERVICE  -> registerUser");
        userValidator.validateRegisterUser(user);
        Users newUser = new Users();
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setPhoneNumber(user.getPhoneNumber());

        newUser.setDepartment(null);

        //TODO: set status
        newUser.setStatus(1L);
        Users saveUser = userRepository.save(newUser);

        UserRole userRole = new UserRole();
        userRole.setUserId(newUser.getUserId());
        // Role default
        userRole.setRoleId(8L);

        Long userRoleId = userRoleService.save(userRole);
        log.info("USER_SERVICE  -> registerUser success");
        return convertToUserDetailDTO(saveUser);
    }



    @Override
    public PageResponse<?> searchUserByCriteria(int pageNo, int pageSize, String usernameSearch, String emailSearch, String phoneNumberSearch, String firstNameSearch, String sortBy, String sortDirection) {
        log.info("USER_SERVICE  -> searchUserByCriteria");
        if (pageNo < 1) {
            pageNo = 1;  // Đảm bảo page luôn bắt đầu từ 1
        }
        if (pageSize <= 0) {
            pageSize = 10;  // Default page size
        }
        return userSearchRepository.searchUserByCriteria(pageNo, pageSize, usernameSearch, emailSearch, phoneNumberSearch, firstNameSearch, sortBy, sortDirection);
    }

    private boolean validateChangePassword(Long id, ChangePasswordRequest changePasswordRequest) {
        if (changePasswordRequest.getCurrentPassword() == null || changePasswordRequest.getNewPassword() == null || changePasswordRequest.getConfirmNewPassword() == null) {
            log.error("USER_SERVICE  -> validateChangePassword fail");
            throw new InvalidDataException("Invalid data");
        }
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found")).getPassword())) {
            log.error("USER_SERVICE  -> validateChangePassword fail");
            throw new InvalidDataException("Invalid password");
        }
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
            log.error("USER_SERVICE  -> validateChangePassword fail");
            throw new InvalidDataException("Password not match");
        }
        return true;

    }
}
