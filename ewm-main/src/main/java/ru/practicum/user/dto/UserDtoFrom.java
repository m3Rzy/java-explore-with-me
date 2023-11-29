package ru.practicum.user.dto;

import lombok.*;
import javax.validation.constraints.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoFrom {
    private Long id;
    @Email
    @Size(min = 6, max = 255)
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 2, max = 255)
    private String name;
}
