package com.daniel.licenta.calendargenerator.algorithm;

import com.daniel.licenta.calendargenerator.algorithm.inputmodel.StudentClass;
import com.daniel.licenta.calendargenerator.algorithm.inputmodel.DataInputRepresentation;
import com.daniel.licenta.calendargenerator.algorithm.inputmodel.RoomInput;
import com.daniel.licenta.calendargenerator.algorithm.inputmodel.TeacherInput;
import com.daniel.licenta.calendargenerator.algorithm.model.CalendarData;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.CalendarOutput;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.StudentClassOutput;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.RoomOutput;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.TeacherOutput;
import com.daniel.licenta.calendargenerator.algorithm.util.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MainRunnableClass {

    @Autowired
    private CoreCSO globalMembers;

    @Autowired
    private DataMapper dataMapper;

    @PostConstruct
    public void main() {
        DataInputRepresentation data = facultyExample();


        CalendarData calendarData = dataMapper.mapToCalendarData(data);

        int[][][] result = globalMembers.runCSO(333, calendarData);

        CalendarOutput calendarOutput = new CalendarOutput(calendarData.getNumberOfSlotsPerDay());
        data.getStudentClasses()
                .stream()
                .map(classInput -> new StudentClassOutput(classInput.getIdentifier(), classInput.getName(), classInput.getNumberOfStudents()))
                .collect(Collectors.toCollection(calendarOutput::getStudentClassOutputs));

        data.getTeachers()
                .stream()
                .map(teacherInput -> new TeacherOutput(teacherInput.getIdentifier(), teacherInput.getName()))
                .collect(Collectors.toCollection(calendarOutput::getTeachers));

        data.getRooms()
                .stream()
                .map(roomInput -> new RoomOutput(roomInput.getIdentifier(), roomInput.getName(), roomInput.getCapacity()))
                .collect(Collectors.toCollection(calendarOutput::getRooms));


        for (int classIndex = 0; classIndex < data.getStudentClasses().size(); classIndex++) {
            StudentClass classInput = data.getStudentClassByIndex(classIndex);
            StudentClassOutput classOutput = calendarOutput.getStudentClassByIdentifier(classInput.getIdentifier());


            for (int j = 0; j < data.getTimeslotsPerDay() * 5; j++) {
                int teacherPosition = result[classIndex][j][0];
                int roomPosition = result[classIndex][j][1];

                TeacherOutput teacherOutput = calendarOutput.getTeacherByIdentifier(data.getTeacherByIndex(teacherPosition).getIdentifier());
                RoomOutput roomOutput = calendarOutput.getRoomByIdentifier(data.getRoomByIndex(roomPosition).getIdentifier());
                classOutput.addTeachingSession(teacherOutput, j / calendarData.getNumberOfSlotsPerDay(), j % calendarData.getNumberOfSlotsPerDay(), roomOutput);
            }
        }

        StringBuffer stringBuffer = new StringBuffer();

        calendarOutput.getStudentClassOutputs().forEach(classOutput -> {
            stringBuffer.append("\n-----------------------------------------------------------------------------------");
            stringBuffer.append("\nSchedule for class : " + classOutput.getName() + " - students nr: " + classOutput.getNumberOfStudents());
            classOutput.getSchedule().forEach((integer, pairs) -> {
                stringBuffer.append("\n");
                stringBuffer.append("\nDay " + integer);
                pairs.forEach(integerTeacherOutputPair -> {
                    stringBuffer.append("\nHour " + (integerTeacherOutputPair.getKey() == 0 ? "0" : "") + (integerTeacherOutputPair.getKey() * 2 + 8) + " : ");
                    if (integerTeacherOutputPair.getValue().getKey().isFree()) {
                        stringBuffer.append(" FREE");
                    } else {
                        stringBuffer.append(" teacher: " + integerTeacherOutputPair.getValue().getKey().getName()
                                + " | room: " + integerTeacherOutputPair.getValue().getValue().getName()
                                + " (capacity = " + integerTeacherOutputPair.getValue().getValue().getCapacity() + ")");
                    }
                });
            });
        });

        try {
            PrintWriter pw = new PrintWriter("results.txt");
            pw.write(stringBuffer.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private static DataInputRepresentation testTwo() {
        StudentClass c1 = new StudentClass(1111, "C1", 30);
        StudentClass c2 = new StudentClass(1112, "C2", 30);
        StudentClass c3 = new StudentClass(1113, "C3", 50);
        StudentClass c4 = new StudentClass(1114, "C4", 50);
        StudentClass c5 = new StudentClass(1115, "C5", 50);

        TeacherInput t1 = new TeacherInput(111, "T1");
        TeacherInput t2 = new TeacherInput(112, "T2");
        TeacherInput t3 = new TeacherInput(113, "T3");
        TeacherInput t4 = new TeacherInput(114, "T4");
        TeacherInput t5 = new TeacherInput(115, "T5");
        TeacherInput t6 = new TeacherInput(116, "T6");
        TeacherInput t7 = new TeacherInput(117, "T7");
        TeacherInput t8 = new TeacherInput(118, "T8");
        TeacherInput t9 = new TeacherInput(119, "T9");


        t1.addStudentClass(c1, 1, 1);
        t1.addStudentClass(c2, 1, 1);
        t1.addStudentClass(c3, 1, 1);
        t1.addStudentClass(c4, 1, 1);
        t1.addStudentClass(c5, 1, 1);

        t2.addStudentClass(c1, 1, 1);
        t2.addStudentClass(c2, 1, 1);
        t2.addStudentClass(c3, 1, 1);
        t2.addStudentClass(c4, 1, 1);
        t2.addStudentClass(c5, 1, 1);

        t3.addStudentClass(c1, 1, 1);
        t3.addStudentClass(c2, 1, 1);
        t3.addStudentClass(c3, 1, 1);
        t3.addStudentClass(c4, 1, 1);
        t3.addStudentClass(c5, 1, 1);

        t4.addStudentClass(c1, 1, 1);
        t4.addStudentClass(c2, 1, 1);
        t4.addStudentClass(c3, 1, 1);
        t4.addStudentClass(c4, 1, 1);
        t4.addStudentClass(c5, 1, 1);

        t5.addStudentClass(c1, 1, 1);
        t5.addStudentClass(c2, 1, 1);
        t5.addStudentClass(c3, 1, 1);
        t5.addStudentClass(c4, 1, 1);
        t5.addStudentClass(c5, 1, 1);

        t6.addStudentClass(c1, 1, 1);
        t6.addStudentClass(c2, 1, 1);
        t6.addStudentClass(c3, 1, 1);
        t6.addStudentClass(c4, 1, 1);
        t6.addStudentClass(c5, 1, 1);

        t7.addStudentClass(c1, 1, 1);
        t7.addStudentClass(c2, 1, 1);
        t7.addStudentClass(c3, 1, 1);
        t7.addStudentClass(c4, 1, 1);
        t7.addStudentClass(c5, 1, 1);

        t8.addStudentClass(c1, 1, 1);
        t8.addStudentClass(c2, 1, 1);
        t8.addStudentClass(c3, 1, 1);
        t8.addStudentClass(c4, 1, 1);
        t8.addStudentClass(c5, 1, 1);

        t9.addStudentClass(c1, 1, 1);
        t9.addStudentClass(c2, 1, 1);
        t9.addStudentClass(c3, 1, 1);
        t9.addStudentClass(c4, 1, 1);
        t9.addStudentClass(c5, 1, 1);


        DataInputRepresentation dataInputRepresentation1 = new DataInputRepresentation(6);
        dataInputRepresentation1.setStudentClasses(Arrays.asList(c1, c2, c3, c4, c5));
        dataInputRepresentation1.setTeachers(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9));


        return dataInputRepresentation1;
    }

    private static DataInputRepresentation testOne() {
        StudentClass c1 = new StudentClass(1111, "C1", 30);
        StudentClass c2 = new StudentClass(1112, "C2", 30);
        StudentClass c3 = new StudentClass(1113, "C3", 30);
        StudentClass c4 = new StudentClass(1114, "C4", 30);
        StudentClass c5 = new StudentClass(1115, "C5", 30);

        TeacherInput t1 = new TeacherInput(111, "T1");
        TeacherInput t2 = new TeacherInput(112, "T2");
        TeacherInput t3 = new TeacherInput(113, "T3");
        TeacherInput t4 = new TeacherInput(114, "T4");
        TeacherInput t5 = new TeacherInput(115, "T5");
        TeacherInput t6 = new TeacherInput(116, "T6");
        TeacherInput t7 = new TeacherInput(117, "T7");
        TeacherInput t8 = new TeacherInput(118, "T8");
        TeacherInput t9 = new TeacherInput(119, "T9");


        t1.addStudentClass(c1, 10, 1);
        t1.addStudentClass(c2, 10, 1);

        t2.addStudentClass(c2, 10, 1);
        t2.addStudentClass(c3, 5, 1);

        t3.addStudentClass(c3, 5, 1);
        t3.addStudentClass(c4, 10, 1);

        t4.addStudentClass(c4, 10, 1);
        t4.addStudentClass(c5, 10, 1);

        t5.addStudentClass(c5, 10, 1);
        t5.addStudentClass(c1, 10, 1);

        t6.addStudentClass(c1, 10, 1);
        t6.addStudentClass(c2, 5, 1);

        t7.addStudentClass(c2, 5, 1);
        t7.addStudentClass(c3, 10, 1);

        t8.addStudentClass(c3, 10, 1);
        t8.addStudentClass(c4, 5, 1);

        t9.addStudentClass(c4, 5, 1);
        t9.addStudentClass(c5, 10, 1);


        RoomInput[] roomRecords = new RoomInput[4];
        for (int i = 0; i < 2; i++) {
            roomRecords[i] = new RoomInput(i, "Room" + i, 30);
        }
        for (int i = 2; i < 4; i++) {
            roomRecords[i] = new RoomInput(i, "Room" + i, 50);
        }


        DataInputRepresentation dataInputRepresentation1 = new DataInputRepresentation(6);
        dataInputRepresentation1.setStudentClasses(Arrays.asList(c1, c2, c3, c4, c5));
        dataInputRepresentation1.setTeachers(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9));
        dataInputRepresentation1.setRooms(Arrays.asList(roomRecords));
        return dataInputRepresentation1;
    }

    private static DataInputRepresentation facultyExample() {
        StudentClass g1 = new StudentClass(1111, "G1", 30);
        StudentClass g2 = new StudentClass(1112, "G2", 20);
        StudentClass g3 = new StudentClass(1113, "G3", 30);
        StudentClass g4 = new StudentClass(1114, "G4", 20);
        StudentClass g5 = new StudentClass(1115, "G5", 30);
        StudentClass g6 = new StudentClass(1116, "G6", 20);
        StudentClass g7 = new StudentClass(1117, "G7", 30);
        StudentClass g8 = new StudentClass(1118, "G8", 20);
        StudentClass SA = new StudentClass(1119, "SA", 100);
        StudentClass SB = new StudentClass(1120, "SB", 100);

        List<StudentClass> studentGroups = Arrays.asList(g1, g2, g3, g4, g5, g6, g7, g8, SA, SB);

        RoomInput r1 = new RoomInput(1, "C201", 20);
        RoomInput r2 = new RoomInput(2, "C202", 20);
        RoomInput r3 = new RoomInput(3, "C203", 20);
        RoomInput r4 = new RoomInput(4, "C204", 20);
        RoomInput r5 = new RoomInput(5, "C205", 20);
        RoomInput r6 = new RoomInput(6, "C206", 20);
        RoomInput r7 = new RoomInput(7, "C207", 20);
        RoomInput r8 = new RoomInput(8, "C208", 20);

        RoomInput r9 = new RoomInput(9, "C901", 30);
        RoomInput r10 = new RoomInput(10, "C902", 30);
        RoomInput r11 = new RoomInput(11, "C903", 30);
        RoomInput r12 = new RoomInput(12, "C904", 30);
        RoomInput r13 = new RoomInput(13, "C905", 30);
        RoomInput r14 = new RoomInput(14, "C906", 30);
        RoomInput r15 = new RoomInput(15, "C907", 30);
        RoomInput r16 = new RoomInput(16, "C908", 30);

        RoomInput r17 = new RoomInput(17, "C1", 100);
        RoomInput r18 = new RoomInput(18, "C2", 100);

        List<RoomInput> roomInputs = Arrays.asList(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18);

        TeacherInput t01 = new TeacherInput(111, "T01");
        t01.addStudentClass(g1, 1, 1);
        t01.addStudentClass(g2, 1, 1);
        t01.addStudentClass(g3, 1, 1);
        t01.addStudentClass(g4, 1, 1);
        t01.addStudentClass(SA, 1, 1);
        t01.addStudentClass(SB, 1, 1);

        TeacherInput t02 = new TeacherInput(112, "T02");
        t02.addStudentClass(g1, 1, 1);
        t02.addStudentClass(g2, 1, 1);
        t02.addStudentClass(g3, 1, 1);
        t02.addStudentClass(g4, 1, 1);
        t02.addStudentClass(SA, 1, 1);
        t02.addStudentClass(SB, 1, 1);

        TeacherInput t03 = new TeacherInput(113, "T03");
        t03.addStudentClass(g1, 1, 1);
        t03.addStudentClass(g2, 1, 1);
        t03.addStudentClass(g3, 1, 1);
        t03.addStudentClass(g4, 1, 1);

        TeacherInput t04 = new TeacherInput(114, "T04");
        t04.addStudentClass(g1, 1, 1);
        t04.addStudentClass(g2, 1, 1);
        t04.addStudentClass(g2, 1, 1);
        t04.addStudentClass(g3, 1, 1);

        TeacherInput t05 = new TeacherInput(115, "T05");
        t05.addStudentClass(g1, 1, 1);
        t05.addStudentClass(g2, 1, 1);
        t05.addStudentClass(g2, 1, 1);
        t05.addStudentClass(g3, 1, 1);

        TeacherInput t06 = new TeacherInput(116, "T06");
        t06.addStudentClass(g1, 1, 1);
        t06.addStudentClass(g2, 1, 1);
        t06.addStudentClass(g2, 1, 1);
        t06.addStudentClass(g3, 1, 1);

        TeacherInput t07 = new TeacherInput(117, "T07");
        t07.addStudentClass(g1, 1, 1);
        t07.addStudentClass(g2, 1, 1);
        t07.addStudentClass(g2, 1, 1);
        t07.addStudentClass(g3, 1, 1);

        TeacherInput t08 = new TeacherInput(118, "T08");
        t08.addStudentClass(g1, 1, 1);
        t08.addStudentClass(g2, 1, 1);
        t08.addStudentClass(g2, 1, 1);
        t08.addStudentClass(g3, 1, 1);


        TeacherInput t09 = new TeacherInput(119, "T09");
        t09.addStudentClass(g1, 1, 1);
        t09.addStudentClass(g2, 1, 1);
        t09.addStudentClass(g2, 1, 1);
        t09.addStudentClass(g3, 1, 1);


        TeacherInput t10 = new TeacherInput(120, "T10");
        t10.addStudentClass(g1, 1, 1);
        t10.addStudentClass(g2, 1, 1);
        t10.addStudentClass(g2, 1, 1);
        t10.addStudentClass(g3, 1, 1);

        TeacherInput t11 = new TeacherInput(121, "T11");
        t11.addStudentClass(g1, 1, 1);
        t11.addStudentClass(g2, 1, 1);
        t11.addStudentClass(g2, 1, 1);
        t11.addStudentClass(g3, 1, 1);
        t11.addStudentClass(g5, 1, 1);
        t11.addStudentClass(g6, 1, 1);
        t11.addStudentClass(g7, 1, 1);
        t11.addStudentClass(g8, 1, 1);

        TeacherInput t12 = new TeacherInput(122, "T12");
        t12.addStudentClass(g1, 1, 1);
        t12.addStudentClass(g2, 1, 1);
        t12.addStudentClass(g2, 1, 1);
        t12.addStudentClass(g3, 1, 1);
        t12.addStudentClass(g5, 1, 1);
        t12.addStudentClass(g6, 1, 1);
        t12.addStudentClass(g7, 1, 1);
        t12.addStudentClass(g8, 1, 1);

        TeacherInput t13 = new TeacherInput(123, "T13");
        t13.addStudentClass(g1, 1, 1);
        t13.addStudentClass(g2, 1, 1);
        t13.addStudentClass(g2, 1, 1);
        t13.addStudentClass(g3, 1, 1);
        t13.addStudentClass(g5, 1, 1);
        t13.addStudentClass(g6, 1, 1);
        t13.addStudentClass(g7, 1, 1);
        t13.addStudentClass(g8, 1, 1);

        TeacherInput t14 = new TeacherInput(124, "T14");
        t14.addStudentClass(g1, 1, 1);
        t14.addStudentClass(g2, 1, 1);
        t14.addStudentClass(g2, 1, 1);
        t14.addStudentClass(g3, 1, 1);
        t14.addStudentClass(g5, 1, 1);
        t14.addStudentClass(g6, 1, 1);
        t14.addStudentClass(g7, 1, 1);
        t14.addStudentClass(g8, 1, 1);

        TeacherInput t15 = new TeacherInput(125, "T15");
        t15.addStudentClass(g1, 1, 1);
        t15.addStudentClass(g2, 1, 1);
        t15.addStudentClass(g2, 1, 1);
        t15.addStudentClass(g3, 1, 1);
        t15.addStudentClass(g5, 1, 1);
        t15.addStudentClass(g6, 1, 1);
        t15.addStudentClass(g7, 1, 1);
        t15.addStudentClass(g8, 1, 1);

        TeacherInput t16 = new TeacherInput(126, "T16");
        t16.addStudentClass(g1, 1, 1);
        t16.addStudentClass(g2, 1, 1);
        t16.addStudentClass(g2, 1, 1);
        t16.addStudentClass(g3, 1, 1);
        t16.addStudentClass(g5, 1, 1);
        t16.addStudentClass(g6, 1, 1);
        t16.addStudentClass(g7, 1, 1);
        t16.addStudentClass(g8, 1, 1);

        TeacherInput t17 = new TeacherInput(127, "T17");
        t17.addStudentClass(g1, 1, 1);
        t17.addStudentClass(g2, 1, 1);
        t17.addStudentClass(g2, 1, 1);
        t17.addStudentClass(g3, 1, 1);
        t17.addStudentClass(g5, 1, 1);
        t17.addStudentClass(g6, 1, 1);
        t17.addStudentClass(g7, 1, 1);
        t17.addStudentClass(g8, 1, 1);

        TeacherInput t18 = new TeacherInput(128, "T18");
        t18.addStudentClass(g1, 1, 1);
        t18.addStudentClass(g2, 1, 1);
        t18.addStudentClass(g2, 1, 1);
        t18.addStudentClass(g3, 1, 1);
        t18.addStudentClass(g5, 1, 1);
        t18.addStudentClass(g6, 1, 1);
        t18.addStudentClass(g7, 1, 1);
        t18.addStudentClass(g8, 1, 1);

        TeacherInput t19 = new TeacherInput(129, "T19");
        t19.addStudentClass(g1, 1, 1);
        t19.addStudentClass(g2, 1, 1);
        t19.addStudentClass(g2, 1, 1);
        t19.addStudentClass(g3, 1, 1);
        t19.addStudentClass(g5, 1, 1);
        t19.addStudentClass(g6, 1, 1);
        t19.addStudentClass(g7, 1, 1);
        t19.addStudentClass(g8, 1, 1);

        TeacherInput t20 = new TeacherInput(130, "T20");
        t20.addStudentClass(g1, 1, 1);
        t20.addStudentClass(g2, 1, 1);
        t20.addStudentClass(g2, 1, 1);
        t20.addStudentClass(g3, 1, 1);
        t20.addStudentClass(g5, 1, 1);
        t20.addStudentClass(g6, 1, 1);
        t20.addStudentClass(g7, 1, 1);
        t20.addStudentClass(g8, 1, 1);

        TeacherInput t21 = new TeacherInput(131, "T21");
        t21.addStudentClass(g5, 1, 1);
        t21.addStudentClass(g6, 1, 1);
        t21.addStudentClass(g7, 1, 1);
        t21.addStudentClass(g8, 1, 1);

        TeacherInput t22 = new TeacherInput(132, "T22");
        t22.addStudentClass(g5, 1, 1);
        t22.addStudentClass(g6, 1, 1);
        t22.addStudentClass(g7, 1, 1);
        t22.addStudentClass(g8, 1, 1);

        TeacherInput t23 = new TeacherInput(133, "T23");
        t23.addStudentClass(g5, 1, 1);
        t23.addStudentClass(g6, 1, 1);
        t23.addStudentClass(g7, 1, 1);
        t23.addStudentClass(g8, 1, 1);

        TeacherInput t24 = new TeacherInput(134, "T24");
        t24.addStudentClass(g5, 1, 1);
        t24.addStudentClass(g6, 1, 1);
        t24.addStudentClass(g7, 1, 1);
        t24.addStudentClass(g8, 1, 1);

        TeacherInput t25 = new TeacherInput(135, "T25");
        t25.addStudentClass(g5, 1, 1);
        t25.addStudentClass(g6, 1, 1);
        t25.addStudentClass(g7, 1, 1);
        t25.addStudentClass(g8, 1, 1);

        TeacherInput t26 = new TeacherInput(136, "T26");
        t26.addStudentClass(g5, 1, 1);
        t26.addStudentClass(g6, 1, 1);
        t26.addStudentClass(g7, 1, 1);
        t26.addStudentClass(g8, 1, 1);

        TeacherInput t27 = new TeacherInput(137, "T27");
        t27.addStudentClass(g5, 1, 1);
        t27.addStudentClass(g6, 1, 1);
        t27.addStudentClass(g7, 1, 1);
        t27.addStudentClass(g8, 1, 1);

        TeacherInput t28 = new TeacherInput(138, "T28");
        t28.addStudentClass(g5, 1, 1);
        t28.addStudentClass(g6, 1, 1);
        t28.addStudentClass(g7, 1, 1);
        t28.addStudentClass(g8, 1, 1);

        TeacherInput t29 = new TeacherInput(139, "T29");
        t29.addStudentClass(g5, 1, 1);
        t29.addStudentClass(g6, 1, 1);
        t29.addStudentClass(g7, 1, 1);
        t29.addStudentClass(g8, 1, 1);
        t29.addStudentClass(SA, 1, 1);
        t29.addStudentClass(SB, 1, 1);

        TeacherInput t30 = new TeacherInput(140, "T30");
        t30.addStudentClass(g5, 1, 1);
        t30.addStudentClass(g6, 1, 1);
        t30.addStudentClass(g7, 1, 1);
        t30.addStudentClass(g8, 1, 1);
        t30.addStudentClass(SA, 1, 1);
        t30.addStudentClass(SB, 1, 1);

        List<TeacherInput> teacherInputs =
                Arrays.asList(t01, t02, t03, t04, t05, t06, t07, t08, t09, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29, t30);

        DataInputRepresentation dataInputRepresentation = new DataInputRepresentation(6);
        dataInputRepresentation.setRooms(roomInputs);
        dataInputRepresentation.setStudentClasses(studentGroups);
        dataInputRepresentation.setTeachers(teacherInputs);

        return dataInputRepresentation;
    }

    private static DataInputRepresentation testThree() {
        StudentClass c1 = new StudentClass(1111, "C1", 30);
        StudentClass c2 = new StudentClass(1112, "C2", 50);
        StudentClass c3 = new StudentClass(1113, "C3", 50);
        StudentClass c4 = new StudentClass(1114, "C4", 50);
        StudentClass c5 = new StudentClass(1115, "C5", 50);
        StudentClass c6 = new StudentClass(1116, "C6", 30);

        TeacherInput t1 = new TeacherInput(111, "T1");
        TeacherInput t2 = new TeacherInput(112, "T2");
        TeacherInput t3 = new TeacherInput(113, "T3");
        TeacherInput t4 = new TeacherInput(114, "T4");
        TeacherInput t5 = new TeacherInput(115, "T5");
        TeacherInput t6 = new TeacherInput(116, "T6");
        TeacherInput t7 = new TeacherInput(117, "T7");
        TeacherInput t8 = new TeacherInput(118, "T8");
        TeacherInput t9 = new TeacherInput(119, "T9");
        TeacherInput t10 = new TeacherInput(120, "T10");
        TeacherInput t11 = new TeacherInput(121, "T11");
        TeacherInput t12 = new TeacherInput(122, "T12");
        TeacherInput t13 = new TeacherInput(123, "T13");
        TeacherInput t14 = new TeacherInput(124, "T14");
        TeacherInput t15 = new TeacherInput(125, "T15");


        t1.addStudentClass(c1, 3, 1);
        t1.addStudentClass(c2, 1, 1);
        t1.addStudentClass(c6, 1, 1);
        t1.addUnavailabilityInterval(0, 0, 1, 2, 3, 4, 5);

        t2.addStudentClass(c1, 3, 1);
        t2.addStudentClass(c2, 1, 1);
        t2.addStudentClass(c3, 2, 1);
        t2.addStudentClass(c4, 2, 1);

        t3.addStudentClass(c1, 2, 1);
        t3.addStudentClass(c2, 2, 1);
        t3.addStudentClass(c3, 2, 1);
        t3.addStudentClass(c4, 2, 1);

        t4.addStudentClass(c1, 2, 1);
        t4.addStudentClass(c2, 2, 1);
        t4.addStudentClass(c3, 2, 1);
        t4.addStudentClass(c4, 2, 1);

        t5.addStudentClass(c1, 2, 1);
        t5.addStudentClass(c2, 2, 1);
        t5.addStudentClass(c3, 2, 1);
        t5.addStudentClass(c4, 2, 1);

        t6.addStudentClass(c1, 2, 1);
        t6.addStudentClass(c2, 2, 1);
        t6.addStudentClass(c3, 2, 1);
        t6.addStudentClass(c4, 2, 1);

        t7.addStudentClass(c1, 2, 1);
        t7.addStudentClass(c2, 2, 1);
        t7.addStudentClass(c3, 2, 1);
        t7.addStudentClass(c4, 2, 1);

        t8.addStudentClass(c1, 2, 1);
        t8.addStudentClass(c2, 2, 1);
        t8.addStudentClass(c3, 2, 1);
        t8.addStudentClass(c4, 2, 1);

        t9.addStudentClass(c1, 2, 1);
        t9.addStudentClass(c2, 2, 1);
        t9.addStudentClass(c3, 2, 1);
        t9.addStudentClass(c4, 2, 1);

        t10.addStudentClass(c3, 4, 1);
        t10.addStudentClass(c4, 4, 1);
        t10.addStudentClass(c6, 4, 1);

        t11.addStudentClass(c1, 2, 1);
        t11.addStudentClass(c2, 2, 1);
        t11.addStudentClass(c3, 2, 1);
        t11.addStudentClass(c4, 2, 1);

        t12.addStudentClass(c1, 4, 1);
        t12.addStudentClass(c2, 4, 1);
        t12.addStudentClass(c5, 4, 1);

        t13.addStudentClass(c1, 2, 1);
        t13.addStudentClass(c2, 4, 1);
        t13.addStudentClass(c3, 2, 1);
        t13.addStudentClass(c4, 2, 1);

        t14.addStudentClass(c3, 6, 1);

        t15.addStudentClass(c1, 2, 1);
        t15.addStudentClass(c4, 6, 1);


        RoomInput[] roomRecords = new RoomInput[7];
        roomRecords[0] = new RoomInput(1, "Room " + 1, 50);
        roomRecords[1] = new RoomInput(2, "Room " + 2, 50);
        roomRecords[2] = new RoomInput(3, "Room " + 3, 50);
        roomRecords[3] = new RoomInput(4, "Room " + 4, 30);
        roomRecords[4] = new RoomInput(5, "Room " + 5, 30);
        roomRecords[5] = new RoomInput(6, "Room " + 6, 50);
        roomRecords[6] = new RoomInput(7, "Room " + 7, 50);


        DataInputRepresentation dataInputRepresentation1 = new DataInputRepresentation(6);
        dataInputRepresentation1.setStudentClasses(Arrays.asList(c1, c2, c3, c4, c5, c6));
        dataInputRepresentation1.setTeachers(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
        dataInputRepresentation1.setRooms(Arrays.asList(roomRecords));
        return dataInputRepresentation1;
    }


}