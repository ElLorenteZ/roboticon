package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.BasicUserCommand;
import io.lorentez.roboticon.model.security.User;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UserToBasicUserCommandConverter implements Converter<User, BasicUserCommand> {

    @Nullable
    @Synchronized
    @Override
    public BasicUserCommand convert(User user) {
        if(user == null){
            return null;
        }
        return BasicUserCommand.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .build();
    }

}
