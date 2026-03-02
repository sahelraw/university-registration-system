package com.springtest1.springtest1.teacher;

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
    public void addTeacher(@RequestBody Teacher teacher) {
        teacherService.addNewTeacher(teacher);
    }

    @DeleteMapping("/{teacherId}")
    public void deleteTeacher(@PathVariable int teacherId) {
        teacherService.deleteTeacher(teacherId);
    }

   @PutMapping("/{teacherId}")
public void updateTeacherFull(@PathVariable int teacherId,
                             @RequestBody Teacher teacher) {
    teacherService.updateTeacherFull(teacherId, teacher);
}

@PutMapping("/{teacherId}/partial")
public void updateTeacherPartial(@PathVariable int teacherId,
                                @RequestBody Teacher teacher) {
    teacherService.updateTeacherPartial(teacherId, teacher);
}

}
