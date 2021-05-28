package io.lorentez.roboticon.model.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TimeEndAfterTimeStartValidator implements ConstraintValidator<TimeEndAfterTimeStart, Object> {

    private static final List<String> timeEndNames = List.of("timeTo", "timeEnd", "timeRemoved");
    private static final List<String> timeStartNames = List.of("timeFrom", "timeStart", "timeAdded");

    public boolean compare(ChronoLocalDateTime timeStart, ChronoLocalDateTime timeEnd){
        return timeStart.isBefore(timeEnd);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {

        Field[] fields = object.getClass().getDeclaredFields();
        Field timeEndField = Arrays.stream(fields)
                .sequential()
                .filter(field ->
                    timeEndNames.stream()
                            .anyMatch(timeEndName -> {
                                String[] names = field.toString().split("\\.");
                                return timeEndName.equals(names[names.length-1]);
                            }))
                .findFirst()
                .orElse(null);
        if (timeEndField != null){
            Field timeStartField = Arrays.stream(fields)
                    .sequential()
                    .filter(field ->
                            timeStartNames.stream()
                                    .anyMatch(timeStartName -> {
                                        String[] names = field.toString().split("\\.");
                                        return timeStartName.equals(names[names.length-1]);
                                    }))
                    .findFirst()
                    .orElse(null);
            if (timeStartField != null) {
                try{
                    timeStartField.setAccessible(true);
                    timeEndField.setAccessible(true);
                    Object timeStart = timeStartField.get(object);
                    Object timeEnd = timeEndField.get(object);
                    if (timeStart != null && timeEnd == null){
                        return true;
                    }
                    else if ( timeStart != null && timeEnd != null && Arrays.stream(timeStart.getClass().getInterfaces())
                                .anyMatch(i -> i.isAssignableFrom(ChronoLocalDateTime.class)) &&
                        Arrays.stream(timeEnd.getClass().getInterfaces())
                                .anyMatch(i -> i.isAssignableFrom(ChronoLocalDateTime.class))){
                        ChronoLocalDateTime timeStartDate = (ChronoLocalDateTime) timeStart;
                        ChronoLocalDateTime timeEndDate = (ChronoLocalDateTime) timeEnd;
                        return compare(timeStartDate, timeEndDate);
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
