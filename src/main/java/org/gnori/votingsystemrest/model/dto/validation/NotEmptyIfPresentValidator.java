package org.gnori.votingsystemrest.model.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.Map;

public class NotEmptyIfPresentValidator implements ConstraintValidator<NotEmptyIfPresent, Object> {

  @Override
  public boolean isValid(Object field, ConstraintValidatorContext context) {

    if (field == null) return true;

    if (field instanceof CharSequence charSequence) {
      return !charSequence.isEmpty();
    }

    if (field instanceof Collection<?> collection) {
      return !collection.isEmpty();
    }

    if (field instanceof Map<?,?> map) {
      return !map.isEmpty();
    }

    if (field instanceof Object[] array) {
      return 0 != array.length;
    }

    return true;
  }
}
