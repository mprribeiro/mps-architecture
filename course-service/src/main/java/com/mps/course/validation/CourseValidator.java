package com.mps.course.validation;


import com.mps.course.configs.security.AuthCurrentUserService;
import com.mps.course.dtos.CourseDto;
import com.mps.course.enums.UserType;
import com.mps.course.models.UserModel;
import com.mps.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @Autowired
    UserService userService;

    @Autowired
    AuthCurrentUserService authCurrentUserService;

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        CourseDto courseDto = (CourseDto) o;
        validator.validate(courseDto, errors);
        if(!errors.hasErrors()){
            validateUserInstructor(courseDto.getUserInstructor(), errors);
        }
    }

    private void validateUserInstructor(UUID userInstructor, Errors errors){
        UUID currentUserId = authCurrentUserService.getCurrentUser().getUserId();
        if(currentUserId.equals(userInstructor)) {
            Optional<UserModel> userModelOptional = userService.findById(userInstructor);
            if (userModelOptional.isEmpty()) {
                errors.rejectValue("userInstructor", "UserInstructorError", "Instructor not found.");
            }
            if (userModelOptional.get().getUserType().equals(UserType.STUDENT.toString())) {
                errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN.");
            }
        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }
}
