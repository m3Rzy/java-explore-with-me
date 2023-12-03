package ru.practicum.compilation.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.UniqueException;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationAdminServiceImpl implements CompilationAdminService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;


    @Override
    public CompilationDto addCompilation(NewCompilationDto request) {
        if (compilationRepository.existsByTitle(request.getTitle())) {
            throw new UniqueException("Такое название сборки уже существует!");
        }

        Set<Event> events;
        events = (request.getEvents() != null && !request.getEvents().isEmpty()) ?
                new HashSet<>(eventRepository.findAllById(request.getEvents())) : new HashSet<>();

        Compilation compilation = Compilation.builder()
                .pinned(request.getPinned() != null && request.getPinned())
                .title(request.getTitle())
                .events(events)
                .build();

        return CompilationMapper.toCompilationsDtoFromCompilation(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilationById(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Сборки с таким id " + compId + " не существует!");
        }
        compilationRepository.deleteById(compId);
    }


    @Override
    public CompilationDto updateCompilation(Long compId, NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Сборки с таким id " + compId + " не существует!"));

        if (newCompilationDto.getTitle() != null) {
            compilation.setTitle(newCompilationDto.getTitle());
        }
        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        }
        if (newCompilationDto.getEvents() != null) {
            HashSet<Event> events = new HashSet<>(eventRepository.findAllById(newCompilationDto.getEvents()));
            compilation.setEvents(events);
        }

        Compilation updatedCompilation = compilationRepository.save(compilation);

        return CompilationMapper.toCompilationsDtoFromCompilation(updatedCompilation);
    }
}
