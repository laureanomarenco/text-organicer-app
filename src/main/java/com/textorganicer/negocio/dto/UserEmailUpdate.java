package com.textorganicer.negocio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEmailUpdate {
    Integer id;
    String mail;
}
