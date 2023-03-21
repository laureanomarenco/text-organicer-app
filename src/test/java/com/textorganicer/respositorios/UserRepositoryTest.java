package com.textorganicer.respositorios;

import com.textorganicer.datos.DatosDummy;
import com.textorganicer.negocio.dominios.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setUp() {

    }
    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Repository - User - getByUsername")
    void getByUsername() {
        //GIVEN
        this.repository.save(DatosDummy.getUserUno());

        //WHEN
        Optional<User> userByUsername = this.repository.findByUsername("Usuario");

        //THEN
        assertThat(userByUsername.isPresent())
                .isTrue();
    }

    @Test
    @DisplayName("Repository - User - getByToken")
    void getByToken() {
        //GIVEN
        User user = this.repository.save(DatosDummy.getUserUno());

        //WHEN
        Optional<User> userByToken = this.repository.findByToken(user.getToken());

        //THEN
        assertThat(userByToken.isPresent())
                .isTrue();
    }


}
