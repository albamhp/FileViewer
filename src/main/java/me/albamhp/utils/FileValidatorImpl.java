package me.albamhp.utils;

import java.io.File;
import java.util.Set;
import java.util.function.Predicate;

public class FileValidatorImpl implements FileValidator {
    
    protected final Set<Predicate<File[]>> validations;
    
    protected FileValidatorImpl(Set<Predicate<File[]>> validations) {
        this.validations = validations;
    }
    
    public boolean isDragOk(File[] files) {
        return validations.size() == 0 || validations.stream().allMatch(p -> p.test(files));
    }

    public boolean hasValidations() {
        return validations.size() > 0;
    }
    
}