package com.daniel.licenta.calendargenerator.algorithm.model;


import static com.daniel.licenta.calendargenerator.algorithm.util.CSOConstants.MAX_NUMBER_OF_TEACHERS;

public class StudentGroup {

    public int classSequence;
    public String className;
    public int numberOfStudents;
    public int numberOfTeachers = 0;

    // matrix with the teachers, the teaching hours and the number of lessons that are taught in the class
    public int[][] teachersOfClassAndHours = new int[MAX_NUMBER_OF_TEACHERS][2];
}