package com.daniel.licenta.calendargenerator.algorithm;

import com.daniel.licenta.calendargenerator.algorithm.extra.Data;
import com.daniel.licenta.calendargenerator.algorithm.inputmodel.DataInputRepresentation;
import com.daniel.licenta.calendargenerator.algorithm.inputmodel.StudentClass;
import com.daniel.licenta.calendargenerator.algorithm.model.CalendarData;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.CalendarOutput;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.RoomOutput;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.StudentClassOutput;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.TeacherOutput;
import com.daniel.licenta.calendargenerator.algorithm.util.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@Component
public class MainRunnableClass {

    @Autowired
    private CoreCSO globalMembers;

    @Autowired
    private DataMapper dataMapper;

    @PostConstruct
    public void main() {
        DataInputRepresentation data = Data.facultyExampleTwo();

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



}