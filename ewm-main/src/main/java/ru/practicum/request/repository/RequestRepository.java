package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.request.dto.ConfirmedRequestShortDto;
import ru.practicum.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    boolean existsByRequesterIdAndEventId(long userId, long eventId);

    List<Request> findAllByRequesterId(long userId);

    List<Request> findAllByEventId(long eventId);

    @Query("SELECT new ru.practicum.request.dto.ConfirmedRequestShortDto(r.event.id , count(r.id)) " +
            "FROM Request r " +
            "WHERE r.event.id in ?1 " +
            "AND r.status = 'CONFIRMED' " +
            "GROUP BY r.event.id ")
    List<ConfirmedRequestShortDto> countByEventId(List<Long> longs);
}
