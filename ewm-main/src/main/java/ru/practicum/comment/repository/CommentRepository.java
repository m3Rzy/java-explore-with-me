package ru.practicum.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.comment.model.Comment;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByAuthorId(Long userId);

    List<Comment> findAllByEventId(Long eventId, Pageable pageable);

    @Query("SELECT c " +
            "FROM Comment AS c " +
            "WHERE LOWER(c.text) LIKE CONCAT('%', LOWER(?1), '%')")
    List<Comment> findByText(String text);
}
