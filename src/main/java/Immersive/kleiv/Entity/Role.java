package Immersive.kleiv.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleType label;
    @OneToMany(mappedBy = "role")
    private List<User> users;

}
