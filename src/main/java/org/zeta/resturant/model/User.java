package org.zeta.resturant.model;

import java.util.*;

public class User {
    private long id;
    private String username;
    private String password;
    private List<Role> roles;

    public User(long id, String username, String password, List<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
