package edu.nju.usm.command;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCommand {

    private String username;
    private String password;
    private String email;

}
