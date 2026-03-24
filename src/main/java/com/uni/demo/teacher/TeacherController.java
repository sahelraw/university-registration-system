package com.uni.demo.teacher;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/teacherAll")
    public List<Teacher> getTeachers() {
        return teacherService.getTeachers();
    }

    @GetMapping("/{teacherId}")
    public Teacher getTeacherById(@PathVariable int teacherId) {
        return teacherService.getTeacherById(teacherId);
    }

    @PostMapping("/teacherAdd")
    public ResponseEntity<String> addTeacher(@RequestBody Teacher teacher) {
        teacherService.addNewTeacher(teacher);
        return ResponseEntity.ok("Teacher added successfully.");
    }

    @DeleteMapping("/{teacherId}")
    public ResponseEntity<String> deleteTeacher(@PathVariable int teacherId) {
        teacherService.deleteTeacher(teacherId);
        return ResponseEntity.ok("Teacher " + teacherId + " deleted successfully.");
    }

   @PutMapping("/{teacherId}")
    public ResponseEntity<String> updateTeacherFull(@PathVariable int teacherId,
                             @RequestBody Teacher teacher) {
        teacherService.updateTeacherFull(teacherId, teacher);
        return ResponseEntity.ok("Teacher " + teacherId + " updated successfully.");
    }

    @PutMapping("/{teacherId}/partial")
    public ResponseEntity<String> updateTeacherPartial(@PathVariable int teacherId,
                                @RequestBody Teacher teacher) {
        teacherService.updateTeacherPartial(teacherId, teacher);
        return ResponseEntity.ok("Teacher " + teacherId + " partially updated successfully.");
    }

}
