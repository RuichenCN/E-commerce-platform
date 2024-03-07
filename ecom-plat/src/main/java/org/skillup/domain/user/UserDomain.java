package org.skillup.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDomain {
    private String userId;
    private String userName;
    private String password;
    // lombok 可以起到getter和setter的作用

}
