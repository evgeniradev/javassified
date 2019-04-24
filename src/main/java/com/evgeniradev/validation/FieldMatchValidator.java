package com.evgeniradev.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

	private String firstField;
	private String secondField;
	private String message;

	@Override
  public void initialize(FieldMatch constraintAnnotation) {
		firstField = constraintAnnotation.firstField();
    secondField = constraintAnnotation.secondField();
    message = constraintAnnotation.message();
  }

	@Override
	public boolean isValid(Object value, final ConstraintValidatorContext context) {
		Object first = new BeanWrapperImpl(value).getPropertyValue(firstField);
		Object second = new BeanWrapperImpl(value).getPropertyValue(secondField);

		boolean valid = first.equals(second);

		if (!valid)
			context.buildConstraintViolationWithTemplate(message)
        .addPropertyNode(firstField)
        .addConstraintViolation()
        .disableDefaultConstraintViolation();

		return valid;
	}

}
