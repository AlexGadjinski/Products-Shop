package com.example.json_processing.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ValidatorUtilImpl implements ValidatorUtil {
    private final Validator validator;

    public ValidatorUtilImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public <E> Set<ConstraintViolation<E>> validate(E e) {
        return validator.validate(e);
    }

    @Override
    public <E> boolean isValid(E e) {
        return validate(e).isEmpty();
    }
}
