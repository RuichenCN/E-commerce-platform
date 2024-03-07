package org.skillup.apiPresentation;

import org.skillup.apiPresentation.dto.in.UserInDto;
import org.skillup.apiPresentation.dto.out.UserOutDto;
import org.skillup.apiPresentation.dto.in.UserPin;
import org.skillup.apiPresentation.util.SkillUpCommon;
import org.skillup.apiPresentation.util.SkillUpResponse;
import org.skillup.domain.user.UserDomain;
import org.skillup.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/account") // 设置公共部分的路径
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<SkillUpResponse> createUser(@RequestBody UserInDto userInDto) {
        UserDomain userDomain;
        // insert data into user table
        try {
            userDomain = userService.createUser(toDomain(userInDto));
            return ResponseEntity.status(SkillUpCommon.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(userDomain)).msg(null).build());
        } catch (Exception e) {
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(SkillUpResponse.builder().result(null).msg(String.format(SkillUpCommon.USER_EXISTS, userInDto.getUserName())).build());
        }

    }

    @GetMapping("/id/{id}")
    public ResponseEntity<SkillUpResponse> readAccountById(@PathVariable("id") String accountId) {
        UserDomain userDomain = userService.readAccountById(accountId);
        // handle userDomain is null
        if (Objects.isNull(userDomain)) {
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(SkillUpCommon.USER_ID_WRONG, accountId)).build());
        }
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(userDomain)).build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SkillUpResponse> readAccountByName(@PathVariable("name") String userName) {
        UserDomain userDomain = userService.readAccountByName(userName);
        // handle userDomain is null
        if (Objects.isNull(userDomain)) {
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(SkillUpCommon.USER_NAME_WRONG, userName)).build());
        }
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(userDomain)).build());
    }

    @PostMapping("/login")
    public ResponseEntity<SkillUpResponse> login(@RequestBody UserInDto userInDto) {
        // 1 get user by name 1.1 userName is wrong 400
        UserDomain userDomain = userService.readAccountByName(userInDto.getUserName());
        if (Objects.isNull(userDomain)) {
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(SkillUpCommon.USER_NAME_WRONG, userInDto.getUserName())).build());
        }
        // 2 check password match 2.1 password is wrong 400
        if (!userInDto.getPassword().equals(userDomain.getPassword())) {
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(SkillUpResponse.builder().msg(SkillUpCommon.PASSWORD_NOT_MATCH).build());
        }
        // 3 password match, return 200
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(userDomain)).build());
    }
    @PutMapping("/password")
    public ResponseEntity<SkillUpResponse> updatePassword(@RequestBody UserPin userPin) {
        // 1 get user, 1.1 userName is wrong 400
        UserDomain userDomain = userService.readAccountByName(userPin.getUserName());
        if (Objects.isNull(userDomain)) {
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(SkillUpCommon.USER_NAME_WRONG, userPin.getUserName())).build());
        }
        // 2 check old password match 2.1 password is wrong 400
        if (!userPin.getOldPassword().equals(userDomain.getPassword())) {
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(SkillUpResponse.builder().msg(SkillUpCommon.PASSWORD_NOT_MATCH).build());
        }
        // 3 if matched, update password and save, return 200
        userDomain.setPassword(userPin.getNewPassword());
        userService.updateUser(userDomain);
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(userDomain)).build());
    }


    private UserDomain toDomain(UserInDto userInDto) {
        return UserDomain.builder()
                .userId(UUID.randomUUID().toString())
                .userName(userInDto.getUserName())
                .password(userInDto.getPassword())
                .build();
    }

    private UserOutDto toOutDto(UserDomain userDomain) {
        return UserOutDto.builder()
                .userId(userDomain.getUserId())
                .userName(userDomain.getUserName())
                .build();
    }
}
