package org.skillup.infrastructure.repoImpl;

import org.jooq.DSLContext;
import org.skillup.domain.user.UserDomain;
import org.skillup.domain.user.UserRepository;
import org.skillup.infrastructure.jooq.tables.records.UserRecord;
import org.skillup.infrastructure.jooq.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JooqUserRepo implements UserRepository {
    @Autowired
    DSLContext dslContext;
    public static final User USER_T = new User();
    @Override
    public void createUser(UserDomain userDomain) {
        dslContext.executeInsert(toRecord(userDomain));
    }

    @Override
    public UserDomain readUserById(String id) {
        Optional<UserDomain> userDomainOptional = dslContext.selectFrom(USER_T).where(USER_T.USER_ID.eq(id)).fetchOptional(this:: toDomain);
        return userDomainOptional.orElse(null);
    }

    @Override
    public UserDomain readUserByName(String name) {
        Optional<UserDomain> userDomainOptional = dslContext.selectFrom(USER_T).where(USER_T.USERNAME.eq(name)).fetchOptional(this:: toDomain);
        return userDomainOptional.orElse(null);
    }

    @Override
    public void updateUser(UserDomain userDomain) {
        dslContext.executeUpdate(toRecord(userDomain));
    }

    private UserRecord toRecord(UserDomain userDomain) {
        // 此处的UserRecord是jooq生成的类，所以无法使用lombok的builder
        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(userDomain.getUserId());
        userRecord.setUsername(userDomain.getUserName());
        userRecord.setPassword(userDomain.getPassword());
        return userRecord;
    }

    private UserDomain toDomain(UserRecord userRecord) {
        return UserDomain.builder()
                .userId(userRecord.getUserId())
                .userName(userRecord.getUsername())
                .password(userRecord.getPassword())
                .build();
    }
}
