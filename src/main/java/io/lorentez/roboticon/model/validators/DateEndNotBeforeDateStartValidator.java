package io.lorentez.roboticon.model.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Arrays;
import java.util.List;

public class DateEndNotBeforeDateStartValidator implements ConstraintValidator<DateEndNotBeforeDateStart, Object> {

    private static final List<String> timeEndNames = List.of("dateEnd");
    private static final List<String> timeStartNames = List.of("dateStart");

    public boolean compare(ChronoLocalDate dateStart, ChronoLocalDate dateEnd) {
        return !dateEnd.isBefore(dateStart);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {

        Field[] fields = object.getClass().getDeclaredFields();
        Field dateEndField = Arrays.stream(fields)
                .sequential()
                .filter(field ->
                        timeEndNames.stream()
                                .anyMatch(dateEndName -> {
                                    String[] names = field.toString().split("\\.");
                                    return dateEndName.equals(names[names.length-1]);
                                }))
                .findFirst()
                .orElse(null);
        if (dateEndField != null){
            Field dateStartField = Arrays.stream(fields)
                    .sequential()
                    .filter(field ->
                            timeStartNames.stream()
                                    .anyMatch(dateStartName -> {
                                        String[] names = field.toString().split("\\.");
                                        return dateStartName.equals(names[names.length-1]);
                                    }))
                    .findFirst()
                    .orElse(null);
            if (dateStartField != null) {
                try{
                    dateStartField.setAccessible(true);
                    dateEndField.setAccessible(true);
                    Object dateStart = dateStartField.get(object);
                    Object dateEnd = dateEndField.get(object);
                    if (dateStart != null && dateEnd == null){
                        return true;
                    }
                    else if ( dateStart != null && dateEnd != null && Arrays.stream(dateStart.getClass().getInterfaces())
                            .anyMatch(i -> i.isAssignableFrom(ChronoLocalDate.class)) &&
                            Arrays.stream(dateEnd.getClass().getInterfaces())
                                    .anyMatch(i -> i.isAssignableFrom(ChronoLocalDate.class))){
                        ChronoLocalDate dateStartDate = (ChronoLocalDate) dateStart;
                        ChronoLocalDate dateEndDate = (ChronoLocalDate) dateEnd;
                        return compare(dateStartDate, dateEndDate);
                    }
                    else{
                        return false;
                    }
                }
                catch (Exception e) {
                    return false;
                }
            }
            else{
                return true;
            }
        }
        return true;
    }

}
