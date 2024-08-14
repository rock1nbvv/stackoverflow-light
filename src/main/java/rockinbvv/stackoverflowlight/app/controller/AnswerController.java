package rockinbvv.stackoverflowlight.app.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rockinbvv.stackoverflowlight.app.data.dto.answer.AnswerCreateDto;
import rockinbvv.stackoverflowlight.app.data.dto.answer.FullAnswerResponseDto;
import rockinbvv.stackoverflowlight.app.data.model.Answer;
import rockinbvv.stackoverflowlight.app.service.AnswerService;

@RestController
@RequestMapping("/api/answer")
@RequiredArgsConstructor
@Tag(name = "Answer")
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Answer createAnswer(@RequestBody AnswerCreateDto answerCreateDto) {
        return answerService.saveAnswer(answerCreateDto);
    }

    @GetMapping("/{id}")
    public FullAnswerResponseDto getAnswerById(@PathVariable Long id) {
        return answerService.getAnswerById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        answerService.deleteAnswer(id);
        return ResponseEntity.noContent().build();
    }
}
