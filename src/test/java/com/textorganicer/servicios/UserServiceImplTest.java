package com.textorganicer.servicios;

import com.textorganicer.datos.DatosDummy;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.respositorios.UserRepository;
import com.textorganicer.servicios.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceImplTest {

    private UserRepository repository;
    private UserService service;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        service = new UserServiceImpl(repository);
    }

    @Test
    void getAll() {
        //GIVEN
        when(repository.findAll())
                .thenReturn(Arrays.asList(
                        DatosDummy.getUserUno(),
                        DatosDummy.getUserDos()
                ));

        //WHEN
        List<User> users = service.getAll();

        assertThat(users.size()).isEqualTo(2);

        assertThat(users.isEmpty()).isFalse();

    }

    @Test
    void getById() {
        //WHEN
        when(repository.findById(1))
                .thenReturn(Optional.of(DatosDummy.uno));

        service.save(DatosDummy.uno);
        User user = service.findById(1);

        //THEN
        assertThat(user.equals(DatosDummy.uno))
                .isTrue();
    }

    @Test
    void getByUsername() {
        //WHEN
        when(repository.findByUsername("Usuario"))
                .thenReturn(Optional.of(DatosDummy.uno));

        service.save(DatosDummy.uno);
        User user = service.findByUsername("Usuario");


        //THEN
        assertThat(user.equals(DatosDummy.uno))
                .isTrue();

    }

    @Test
    void save() {
        //GIVEN
        User user = DatosDummy.getUserUno();

        //WHEN
        service.save(user);

        //THEN
        ArgumentCaptor<User> userArgumentCaptor
                = ArgumentCaptor.forClass(User.class);

        verify(repository).save(userArgumentCaptor.capture());

        User userCaptor = userArgumentCaptor.getValue();

        assertThat(userCaptor).isEqualTo(user);

    }

    @Test
    void userExists() {

        //WHEN
        when(repository.findByUsername("Usuario"))
                .thenReturn(Optional.of(DatosDummy.getUserUno()));

        User user = service.findByUsername("Usuario");
        boolean exists = service.userExists("Usuario");
        boolean notExists = service.userExists("Invalido");

        //THEN
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void delete() {
        // GIVEN
        User user = DatosDummy.getUserUno();
        doNothing().when(repository).delete(user);

        // WHEN
        service.delete(user);

        // THEN
        verify(repository, times(1)).delete(user);
    }

    @Test
    void getByToken() {
        //WHEN
        when(repository.findByToken(DatosDummy.uno.getToken()))
                .thenReturn(Optional.of(DatosDummy.uno));
        User user = service.findByToken(DatosDummy.uno.getToken());

        //THEN
        assertThat(user.equals(DatosDummy.uno))
                .isTrue();
    }
}
