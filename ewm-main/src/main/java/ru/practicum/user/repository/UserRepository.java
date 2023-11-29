package ru.practicum.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean isExistEmail(String email);

    @Query("SELECT * FROM User")
    List<User> findUsersWithPageable(Pageable pageable);

    @Query("SELECT user FROM User user WHERE user.id IN ?1")
    List<User> findUserByIdWithPageable(List<Long> id, Pageable pageable);
}
