package io.github.bhhan.server.service.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Objects;

public class TimeRangeValidator implements ConstraintValidator<TimeRange, io.github.bhhan.server.service.common.dto.TimeRange> {
    @Override
    public boolean isValid(io.github.bhhan.server.service.common.dto.TimeRange timeRange, ConstraintValidatorContext context) {
        if(Objects.isNull(timeRange)){
            return false;
        }

        String startDate = timeRange.getStartDate();
        String endDate = timeRange.getEndDate();

        if(Objects.isNull(startDate) || Objects.isNull(endDate) || startDate.equals("") || endDate.equals("")){
            return false;
        }

        return !LocalDate.parse(startDate).isAfter(LocalDate.parse(endDate));
    }
}

