package com.icinemas.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.icinemas.enums.UserRole;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@Document(collection = "user")
@NoArgsConstructor
public class User {
    @JsonCreator
    public User(
            @JsonProperty("userId") String userId,
            @JsonProperty("userName") String userName,
            @JsonProperty("password") String password,
            @JsonProperty("email") String email,
            @JsonProperty("role") UserRole role
    ) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    @Id
    @Setter(AccessLevel.NONE)
    private String userId;
    private String userName;
    private String password;
    private String email;
    private UserRole role;
}




