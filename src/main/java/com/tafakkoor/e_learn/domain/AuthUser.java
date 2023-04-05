package com.tafakkoor.e_learn.domain;

import com.tafakkoor.e_learn.enums.Levels;
import com.tafakkoor.e_learn.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
public class AuthUser extends Auditable {
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Image image;
//    @NotBlank(message = "Username cannot be blank")
//    @Pattern(regexp="^\\S+$", message="Username cannot contain whitespace")
//    @Pattern(regexp="^[a-zA-Z0-9_]{3,15}$", message="Invalid username")
    @Column(unique = true, nullable = false)
    private String username;
//    @NotBlank(message = "Password cannot be blank")
//    @Pattern(regexp="^\\S+$", message="Password cannot contain whitespace")
//    @Pattern(regexp="^(?=.*[0-9])(?=.*[a-z]).{8,}$", message="Choose a stronger password")
    @Column(nullable = false)
    private String password;
//    @NotBlank(message = "First name cannot be blank")
//    @Pattern(regexp="^[a-zA-Z]{2,30}$", message="Invalid first name")
    private String firstName;
//    @NotBlank(message = "Last name cannot be blank")
//    @Pattern(regexp="^[a-zA-Z]{2,30}$", message="Invalid last name")
    private String lastName;
//    @NotBlank(message = "Email cannot be blank")
//    @Pattern(regexp="^[A-Za-z0-9+_.-]+@(.+)$", message="Invalid email")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @ManyToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "auth_user_roles",
            joinColumns = @JoinColumn(name = "auth_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "auth_role_id", referencedColumnName = "id")
    )
    private Collection<AuthRole> authRoles;
    @Builder.Default
    private Status status = Status.INACTIVE;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Levels level = Levels.DEFAULT;
    private LocalDate birthDate;
    private LocalDateTime lastLogin;
    @Column(nullable = false, columnDefinition = "integer default 0")
    @Builder.Default
    private Integer score = 0;

}
