// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package com.faculty.controller;

import com.faculty.model.Course;
import java.util.List;
import model.dao.CourseDAO;

public class CourseController {
   private final CourseDAO courseDAO = new CourseDAO();

   public CourseController() {
   }

   public List<Course> getAllCourses() {
      return this.courseDAO.getAllCourses();
   }

   public List<Course> getCoursesByStudentUserId(int var1) {
      return this.courseDAO.getCoursesByStudentUserId(var1);
   }

   public List<Course> getCoursesByLecturerId(int var1) {
      return this.courseDAO.getCoursesByLecturerId(var1);
   }

   public void saveBatch(List<Course> var1, List<Course> var2, List<Course> var3) throws Exception {
      for(Course var5 : var1) {
         this.courseDAO.addCourse(var5);
      }

      for(Course var8 : var2) {
         this.courseDAO.updateCourse(var8);
      }

      for(Course var9 : var3) {
         this.courseDAO.deleteCourse(var9.getCourseCode());
      }

   }

   public List<Course> getCoursesByYearAndSemester(int var1, int var2) {
      return this.courseDAO.getCoursesByYearAndSemester(var1, var2);
   }

   public void enrollStudent(int var1, int var2) throws Exception {
      this.courseDAO.enrollStudent(var1, var2);
   }
}
