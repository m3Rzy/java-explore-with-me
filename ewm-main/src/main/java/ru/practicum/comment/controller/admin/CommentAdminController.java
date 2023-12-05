package ru.practicum.comment.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.service.admin.CommentAdminService;

import java.util.List;


@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class CommentAdminController {

    private final CommentAdminService commentAdminService;

    @GetMapping("comment/search")
    public ResponseEntity<List<CommentDto>> search(@RequestParam String text) {
        return ResponseEntity.ok(commentAdminService.findByText(text));
    }

    @GetMapping("users/{userId}/comment")
    public ResponseEntity<List<CommentDto>> get(@PathVariable Long userId) {
        return ResponseEntity.ok(commentAdminService.findByUserId(userId));
    }

    @DeleteMapping("comment/{comId}")
    public ResponseEntity<String> delete(@PathVariable Long comId) {
        commentAdminService.delete(comId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Комментарий c id " + comId + " был успешно удалён!");
    }
}