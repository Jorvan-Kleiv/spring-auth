package Immersive.kleiv.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class Validation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Instant createdAt;
    private Instant xpiredAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
