package com.mps.course.repositories;

import com.mps.course.models.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseModel, UUID>, JpaSpecificationExecutor<CourseModel> {

    @Query(value="select case when count(tcu) > 0 THEN true ELSE false END FROM courses_users tcu WHERE tcu.course_id= :courseId and tcu.user_id= :userId",nativeQuery = true)
    boolean existsByCourseAndUser(@Param("courseId") UUID courseId, @Param("userId") UUID userId);

    @Modifying
    @Query(value="insert into courses_users (course_id, user_id) values (:courseId,:userId)",nativeQuery = true)
    void saveCourseUser(@Param("courseId") UUID courseId, @Param("userId") UUID userId);

    @Modifying
    @Query(value="delete from courses_users where course_id= :courseId",nativeQuery = true)
    void deleteCourseUserByCourse(@Param("courseId") UUID courseId);

    @Modifying
    @Query(value="delete from courses_users where user_id= :userId",nativeQuery = true)
    void deleteCourseUserByUser(@Param("userId") UUID userId);

}
