package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDtoFrom;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.service.CommentPrivateService;

@RestController
@Validated
@RequiredArgsConstructor
public class CommentPrivateController {

    private final CommentPrivateService commentPrivateService;

    @PostMapping("/users/{userId}/events/{eventId}/comment")
    public ResponseEntity<CommentDto> create(@PathVariable Long userId, @PathVariable Long eventId,
                                             @RequestBody @Validated CommentDtoFrom commentDtoFrom) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentPrivateService.create(userId, eventId, commentDtoFrom));
    }

    @DeleteMapping("/users/{userId}/comment/{comId}")
    public ResponseEntity<String> delete(@PathVariable Long userId, @PathVariable Long comId) {
        commentPrivateService.delete(userId, comId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Комментарий c id " + comId + " был успешно удалён!");
    }

    @PatchMapping("/users/{userId}/comment/{comId}")
    public ResponseEntity<CommentDto> update(@PathVariable Long userId, @PathVariable Long comId,
                                            @RequestBody @Validated CommentDtoFrom commentDtoFrom) {
        return ResponseEntity.ok(commentPrivateService.update(userId, comId, commentDtoFrom));
    }
}
