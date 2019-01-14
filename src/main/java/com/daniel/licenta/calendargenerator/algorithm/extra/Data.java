package com.daniel.licenta.calendargenerator.algorithm.extra;

import com.daniel.licenta.calendargenerator.algorithm.inputmodel.*;

import java.util.Arrays;
import java.util.List;

public class Data {

    public static DataInputRepresentation facultyExample() {
        StudentClassInput g1 = new StudentClassInput(1111, "G1", 30);
        StudentClassInput g2 = new StudentClassInput(1112, "G2", 30);
        StudentClassInput g3 = new StudentClassInput(1113, "G3", 30);
        StudentClassInput g4 = new StudentClassInput(1114, "G4", 30);
        StudentClassInput g5 = new StudentClassInput(1115, "G5", 30);
        StudentClassInput g6 = new StudentClassInput(1116, "G6", 30);
        StudentClassInput g7 = new StudentClassInput(1117, "G7", 30);
        StudentClassInput g8 = new StudentClassInput(1118, "G8", 30);
        StudentClassInput SA = new StudentClassInput(1119, "SA", 100);
        StudentClassInput SB = new StudentClassInput(1120, "SB", 100);

        List<StudentClassInput> studentGroups = Arrays.asList(g1, g2, g3, g4, g5, g6, g7, g8, SA, SB);

        CourseGroupRelationship semianA = new CourseGroupRelationship(SA);
        semianA.getStudentGroups().addAll(Arrays.asList(g1, g2, g3, g4));
        CourseGroupRelationship semianB = new CourseGroupRelationship(SB);
        semianB.getStudentGroups().addAll(Arrays.asList(g5, g6, g7, g8));

        List<CourseGroupRelationship> courseGroupRelationships = Arrays.asList(semianA, semianB);

        TeacherInput t01 = new TeacherInput(111, "T01");
        t01.addUnavailabilityInterval(0, 0, 1, 2);
        t01.addUnavailabilityInterval(1, 0, 1, 2);
        t01.addUnavailabilityInterval(2, 0, 1, 2);
        t01.addUnavailabilityInterval(3, 0, 1, 2);
        t01.addUnavailabilityInterval(4, 0, 1, 2);
        t01.addStudentClass(g1, 1);
        t01.addStudentClass(g2, 1);
        t01.addStudentClass(g3, 1);
        t01.addStudentClass(g4, 1);
        t01.addStudentClass(SA, 1);

        TeacherInput t02 = new TeacherInput(112, "T02");
        t02.addStudentClass(g1, 1);
        t02.addStudentClass(g2, 1);
        t02.addStudentClass(g3, 1);
        t02.addStudentClass(g4, 1);
        t02.addStudentClass(SA, 1);

        TeacherInput t03 = new TeacherInput(113, "T03");
        t03.addStudentClass(g1, 1);
        t03.addStudentClass(g2, 1);
        t03.addStudentClass(g3, 1);
        t03.addStudentClass(g4, 1);
        t03.addStudentClass(SA, 1);

        TeacherInput t04 = new TeacherInput(114, "T04");
        t04.addStudentClass(g1, 1);
        t04.addStudentClass(g2, 1);
        t04.addStudentClass(g3, 1);
        t04.addStudentClass(g4, 1);
        t04.addStudentClass(SA, 1);
        t04.addStudentClass(SB, 1);

        TeacherInput t05 = new TeacherInput(115, "T05");
        t05.addUnavailabilityInterval(3, 0, 1, 2, 3, 4, 5);
        t05.addStudentClass(g1, 1);
        t05.addStudentClass(g2, 1);
        t05.addStudentClass(g3, 1);
        t05.addStudentClass(g4, 1);
        t05.addStudentClass(SA, 1);

        TeacherInput t06 = new TeacherInput(121, "T06");
        t06.addStudentClass(g1, 1);
        t06.addStudentClass(g2, 1);
        t06.addStudentClass(g3, 1);
        t06.addStudentClass(g4, 1);
        t06.addStudentClass(g5, 1);
        t06.addStudentClass(g6, 1);
        t06.addStudentClass(g7, 1);
        t06.addStudentClass(g8, 1);

        TeacherInput t07 = new TeacherInput(122, "T07");
        t07.addUnavailabilityInterval(4, 0, 1, 2, 3, 4, 5);
        t07.addStudentClass(g1, 1);
        t07.addStudentClass(g2, 1);
        t07.addStudentClass(g3, 1);
        t07.addStudentClass(g4, 1);
        t07.addStudentClass(g5, 1);
        t07.addStudentClass(g6, 1);
        t07.addStudentClass(g7, 1);
        t07.addStudentClass(g8, 1);

        TeacherInput t08 = new TeacherInput(123, "T08");
        t08.addStudentClass(g1, 1);
        t08.addStudentClass(g2, 1);
        t08.addStudentClass(g3, 1);
        t08.addStudentClass(g4, 1);
        t08.addStudentClass(g5, 1);
        t08.addStudentClass(g6, 1);
        t08.addStudentClass(g7, 1);
        t08.addStudentClass(g8, 1);

        TeacherInput t09 = new TeacherInput(124, "T09");
        t09.addStudentClass(g1, 1);
        t09.addStudentClass(g2, 1);
        t09.addStudentClass(g3, 1);
        t09.addStudentClass(g4, 1);
        t09.addStudentClass(g5, 1);
        t09.addStudentClass(g6, 1);
        t09.addStudentClass(g7, 1);
        t09.addStudentClass(g8, 1);

        TeacherInput t15 = new TeacherInput(130, "T15");
        t15.addStudentClass(g1, 1);
        t15.addStudentClass(g2, 1);
        t15.addStudentClass(g3, 1);
        t15.addStudentClass(g4, 1);
        t15.addStudentClass(g5, 1);
        t15.addStudentClass(g6, 1);
        t15.addStudentClass(g7, 1);
        t15.addStudentClass(g8, 1);

        TeacherInput t16 = new TeacherInput(136, "T16");
        t16.addStudentClass(g5, 1);
        t16.addStudentClass(g6, 1);
        t16.addStudentClass(g7, 1);
        t16.addStudentClass(g8, 1);
        t16.addStudentClass(SA, 1);
        t16.addStudentClass(SB, 1);

        TeacherInput t17 = new TeacherInput(137, "T17");
        t17.addStudentClass(g5, 1);
        t17.addStudentClass(g6, 1);
        t17.addStudentClass(g7, 1);
        t17.addStudentClass(g8, 1);
        t17.addStudentClass(SB, 1);


        TeacherInput t18 = new TeacherInput(138, "T18");
        t18.addStudentClass(g5, 1);
        t18.addStudentClass(g6, 1);
        t18.addStudentClass(g7, 1);
        t18.addStudentClass(g8, 1);
        t18.addStudentClass(SB, 1);


        TeacherInput t19 = new TeacherInput(139, "T19");
        t19.addStudentClass(g5, 1);
        t19.addStudentClass(g6, 1);
        t19.addStudentClass(g7, 1);
        t19.addStudentClass(g8, 1);
        t19.addStudentClass(SB, 1);

        TeacherInput t20 = new TeacherInput(140, "T20");
        t20.addStudentClass(g5, 1);
        t20.addStudentClass(g6, 1);
        t20.addStudentClass(g7, 1);
        t20.addStudentClass(g8, 1);
        t20.addStudentClass(SB, 1);


        List<TeacherInput> teacherInputs =
                Arrays.asList(t01, t02, t03, t04, t05, t06, t07, t08, t09, t15, t16, t17, t18, t19, t20);

        DataInputRepresentation dataInputRepresentation = new DataInputRepresentation(6);
        dataInputRepresentation.setStudentClasses(studentGroups);
        dataInputRepresentation.setTeachers(teacherInputs);
        dataInputRepresentation.setRelationships(courseGroupRelationships);

        return dataInputRepresentation;
    }


}
