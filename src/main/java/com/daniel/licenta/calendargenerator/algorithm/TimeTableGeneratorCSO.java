package com.daniel.licenta.calendargenerator.algorithm;

import com.daniel.licenta.calendargenerator.algorithm.core.ConfigCSO;
import com.daniel.licenta.calendargenerator.algorithm.core.CsoAlgorithm;
import com.daniel.licenta.calendargenerator.algorithm.inputmodel.ConfigData;
import com.daniel.licenta.calendargenerator.algorithm.inputmodel.DataInputRepresentation;
import com.daniel.licenta.calendargenerator.algorithm.model.CalendarData;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.CalendarOutput;
import com.daniel.licenta.calendargenerator.algorithm.util.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Comparator;

@Component
public class TimeTableGeneratorCSO {

    @Autowired
    private CsoAlgorithm globalMembers;

    @Autowired
    private DataMapper dataMapper;

    @Autowired
    private ConfigCSO configCSO;

    public CalendarOutput generateTimetable(DataInputRepresentation data, ConfigData configData) {
        configCSO.overrideConfigData(configData);
        CalendarData calendarData = dataMapper.mapToCalendarData(data);

        int[][][] result = globalMembers.runCSO(calendarData);

        CalendarOutput calendarOutput = dataMapper.mapToOutput(data, calendarData, result);

        printDataToFile(calendarOutput);

        return calendarOutput;
    }

    private void printDataToFile(CalendarOutput calendarOutput) {
        StringBuffer classBuffer = new StringBuffer();
        StringBuffer teacherBuffer = new StringBuffer();

        calendarOutput.getTeachers().forEach(teacherOutput -> {
            teacherBuffer.append("\n-----------------------------------------------------------------------------------");
            teacherBuffer.append("\nSchedule for teacher : " + teacherOutput.getName());
            teacherOutput.getSchedule()
                    .entrySet()
                    .stream()
                    .sorted(Comparator.comparing(o -> o.getKey()))
                    .forEach(entry -> {
                        teacherBuffer.append("\nSlot " + entry.getKey() + " (hour " + ((entry.getKey() % 6) * 2 + 8) + ")- Group: " + entry.getValue().getName());
                    });
        });

        calendarOutput.getStudentClassOutputs().forEach(classOutput -> {
            classBuffer.append("\n-----------------------------------------------------------------------------------");
            classBuffer.append("\nSchedule for class : " + classOutput.getName() + " - students nr: " + classOutput.getNumberOfStudents());
            classOutput.getSchedule().forEach((sessionOfWeek, teacherOutputRoomOutputPair) -> {
                if (sessionOfWeek % 6 == 0) {
                    classBuffer.append("\n");
                    classBuffer.append("\nDay " + sessionOfWeek / 6);
                }
                classBuffer.append("\nHour " + (sessionOfWeek % 6 == 0 ? "0" : "") + ((sessionOfWeek % 6) * 2 + 8) + " : ");
                if (teacherOutputRoomOutputPair.getKey().isFree()) {
                    classBuffer.append(" FREE");
                } else {
                    classBuffer.append(" teacher: " + teacherOutputRoomOutputPair.getKey().getName()
                            + " | room: " + teacherOutputRoomOutputPair.getValue().getName()
                            + " (capacity = " + teacherOutputRoomOutputPair.getValue().getCapacity() + ")");
                }
            });
        });

        try {
            PrintWriter pwClass = new PrintWriter("resultsClass.txt");
            PrintWriter pwTeacher = new PrintWriter("resultsTeacher.txt");
            pwClass.write(classBuffer.toString());
            pwClass.close();
            pwTeacher.write(teacherBuffer.toString());
            pwTeacher.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}