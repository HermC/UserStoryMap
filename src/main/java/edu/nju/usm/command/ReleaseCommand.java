package edu.nju.usm.command;

import lombok.Data;

import java.util.Date;

@Data
public class ReleaseCommand {

    private long id;

    private long map_id;
    private String release_name;
    private Date deadline;

}
