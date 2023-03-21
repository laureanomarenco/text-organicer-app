package com.textorganicer.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.textorganicer.datos.DatosDummy;
import com.textorganicer.servicios.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserController {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;
    private String endpoint;

    @BeforeEach
    void setUp() {
        this.endpoint = "/user";
        this.objectMapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getAllUsers() throws Exception {
        //GIVEN
        when(userService.getAll())
                .thenReturn(Arrays.asList(

                ));

        //WHEN
        mockMvc.perform(
                        get(this.endpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                //THEN
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value(DatosDummy.getUserUno().getUsername()));
    }
}
