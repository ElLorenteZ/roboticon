package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.BasicUserCommand;
import io.lorentez.roboticon.model.security.User;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;

@Component
public class UserToBasicUserCommandConverter implements Converter<User, BasicUserCommand> {

    @Nullable
    @Synchronized
    @Override
    public BasicUserCommand convert(User user) {
        if(user == null){
            return null;
        }
        BasicUserCommand userCommand = new BasicUserCommand();
        userCommand.setId(user.getId());
        userCommand.setName(user.getName());
        userCommand.setSurname(user.getSurname());
        userCommand.setEmail(user.getEmail());
        return userCommand;
    }

}
