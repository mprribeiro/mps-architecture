package com.mps.course.controllers;

import com.mps.course.dtos.LessonDto;
import com.mps.course.models.LessonModel;
import com.mps.course.models.ModuleModel;
import com.mps.course.services.LessonService;
import com.mps.course.services.ModuleService;
import com.mps.course.specifications.SpecificationTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    @Autowired
    LessonService lessonService;

    @Autowired
    ModuleService moduleService;

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@PathVariable(value="moduleId") UUID moduleId,
                                             @RequestBody @Valid LessonDto lessonDto){
        log.debug("POST saveLesson lessonDto received {} ", lessonDto.toString());
        Optional<ModuleModel> moduleModelOptional = moduleService.findById(moduleId);
        if(moduleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found.");
        }
        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(moduleModelOptional.get());
        lessonService.save(lessonModel);
        log.debug("POST saveLesson lessonId saved {} ", lessonModel.getLessonId());
        log.info("Lesson saved successfully lessonId {} ", lessonModel.getLessonId());
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonModel);
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable(value="moduleId") UUID moduleId,
                                               @PathVariable(value="lessonId") UUID lessonId){
        log.debug("DELETE deleteLesson lessonId received {} ", lessonId);
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if(lessonModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module.");
        }
        lessonService.delete(lessonModelOptional.get());
        log.debug("DELETE deleteLesson lessonId deleted {} ", lessonId);
        log.info("Lesson deleted successfully lessonId {} ", lessonId);
        return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully.");
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable(value="moduleId") UUID moduleId,
                                               @PathVariable(value="lessonId") UUID lessonId,
                                               @RequestBody @Valid LessonDto lessonDto){
        log.debug("PUT updateLesson lessonDto received {} ", lessonDto.toString());
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if(lessonModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module.");
        }
        var lessonModel = lessonModelOptional.get();
        lessonModel.setTitle(lessonDto.getTitle());
        lessonModel.setDescription(lessonDto.getDescription());
        lessonModel.setVideoUrl(lessonDto.getVideoUrl());
        lessonService.save(lessonModel);
        log.debug("PUT updateLesson lessonId saved {} ", lessonModel.getLessonId());
        log.info("Lesson updated successfully lessonId {} ", lessonModel.getLessonId());
        return ResponseEntity.status(HttpStatus.OK).body(lessonModel);
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Page<LessonModel>> getAllLessons(@PathVariable(value="moduleId") UUID moduleId,
                                                           SpecificationTemplate.LessonSpec spec,
                                                           @PageableDefault(page = 0, size = 10, sort = "lessonId", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findAllByModule(SpecificationTemplate.lessonModuleId(moduleId).and(spec), pageable));
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneLesson(@PathVariable(value="moduleId") UUID moduleId,
                                               @PathVariable(value="lessonId") UUID lessonId){
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        return lessonModelOptional.<ResponseEntity<Object>>map(lessonModel -> ResponseEntity.status(HttpStatus.OK).body(lessonModel)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module."));
    }
}
