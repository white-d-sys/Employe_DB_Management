package com.prodevans.QRBaseLogin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User_Detail {
    private String sessionid;
    private String username;
    private String password;

}
