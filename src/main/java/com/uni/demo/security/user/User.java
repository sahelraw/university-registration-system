package com.uni.demo.security.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data //generates getters for all fields, a useful toString method, and hashCode and equals implementations that
@Builder //build mo object in an easy way using a design pattern builder for creating instances of the class, which can make the code more readable and easier to maintain
@NoArgsConstructor //generates a no-arguments constructor for the class, which is required by some frameworks and libraries that use reflection to create instances of the class
@AllArgsConstructor //generates a constructor with one parameter for each field in the class, which
@Entity //to make this user class an entity that can be persisted in a database using JPA (Java Persistence API)
@Table(name = "_user") //if i didnt specify the name it will take the class name by defaul so we could have some ambiguity if we have another class with the same name in another package so we specify the name of the table in the database that will be used to store the user data

//when talking about authorization and security we need to have a user class that will represent the user in our system and will be used to authenticate and authorize the user
public class User implements UserDetails { //to implement the UserDetails interface which is a core interface in Spring Security that represents the user details required for authentication and authorization
    @Id // to specify that this field "specially the id" is the primary key of the entity and will be used to uniquely identify each user in the database
    @GeneratedValue //it will make this id or this object Auto generated wether a sequence a table or an identity column depending on the database we are using "incremental id"
    private Integer id;

private String firstname;

private String lastname;

//@Column(unique = true) //to ensure that the email is unique in the database
private String email;

private String password;

@Enumerated(EnumType.STRING) //to specify that this field is an enumerated type and will be stored in the database as a string representation of the enum value which is seen in Role.java
private Role role; //a file is there to specify the role of the user

//from UserDetails interface
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name())); //we will implement this method later when we will implement the roles and permissions... after... but for now we will return a list of authorities that contains the role of the user as a simple granted authority
}


@Override
public String getPassword() {
    return password; //we will return the password of the user for
}

@Override
public String getUsername() {
    return email; //we will use the email as the username for authentication because it's unique
}

@Override
public boolean isAccountNonExpired() {
    return true; //we will return true for now but we can implement this method later to check if the account is expired or not
}

@Override
public boolean isAccountNonLocked() {
    return true; //we will return true for now but we can implement this method later to check if the account is locked or not
}

@Override
public boolean isCredentialsNonExpired() {
    return true; //we will return true for now but we can implement this method later to check if the credentials are expired or not
}

@Override
public boolean isEnabled() {
    return true; //we will return true for now but we can implement this method later to check if the account is enabled or not
}
}

//after the user and role class we will implement the user repository and the user service and then we will implement the security configuration to secure our application and then we will implement the authentication and authorization process using JWT (JSON Web Token) for stateless authentication and authorization.