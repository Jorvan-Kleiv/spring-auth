package Immersive.kleiv.Services;

import Immersive.kleiv.Entity.User;
import Immersive.kleiv.Entity.Validation;
import Immersive.kleiv.Repositories.ValidationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@RequiredArgsConstructor
public class ValidationService {
    private final ValidationRepository validationRepository;

    public void generateCode(User user) {
        Validation validation = new Validation();
        validation.setUser(user);
        Random random = new Random();
        String code = String.format("%06d", random.nextInt(999999));
        validation.setCode(code);
        Instant createdAt = Instant.now();
        validation.setCreatedAt(createdAt);
        validation.setXpiredAt(createdAt.plus(15, MINUTES));
        this.validationRepository.save(validation);
    }

    public Validation getValidationByCode(String code) {
        return validationRepository.findByCode(code).orElseThrow(() -> new RuntimeException("not found"));
    }
}
