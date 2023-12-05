package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.service.CommentService;

import java.util.List;


@RestController
@Validated
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comment/{comId}")
    public ResponseEntity<CommentDto> getById(@PathVariable Long comId) {
        return ResponseEntity.ok(commentService.getById(comId));
    }

    @GetMapping("/events/{eventId}/comment")
    public ResponseEntity<List<CommentShortDto>> getByEventId(@PathVariable Long eventId,
                                                              @RequestParam(defaultValue = "0") int from,
                                                              @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getByEventId(eventId, from, size));
    }
}