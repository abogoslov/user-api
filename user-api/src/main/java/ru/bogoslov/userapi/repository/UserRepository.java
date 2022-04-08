package ru.bogoslov.userapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bogoslov.userapi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findDistinctByLoginAndPassword(String login, String password);
}
