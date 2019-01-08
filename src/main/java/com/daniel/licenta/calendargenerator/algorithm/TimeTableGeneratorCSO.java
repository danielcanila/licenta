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
        StringBuffer stringBuffer = new StringBuffer();

        calendarOutput.getStudentClassOutputs().forEach(classOutput -> {
            stringBuffer.append("\n-----------------------------------------------------------------------------------");
            stringBuffer.append("\nSchedule for class : " + classOutput.getName() + " - students nr: " + classOutput.getNumberOfStudents());
            classOutput.getSchedule().forEach((sessionOfWeek, teacherOutputRoomOutputPair) -> {
                if (sessionOfWeek % 6 == 0) {
                    stringBuffer.append("\n");
                    stringBuffer.append("\nDay " + sessionOfWeek / 6);
                }
                stringBuffer.append("\nHour " + (sessionOfWeek % 6 == 0 ? "0" : "") + ((sessionOfWeek % 6) * 2 + 8) + " : ");
                if (teacherOutputRoomOutputPair.getKey().isFree()) {
                    stringBuffer.append(" FREE");
                } else {
                    stringBuffer.append(" teacher: " + teacherOutputRoomOutputPair.getKey().getName()
                            + " | room: " + teacherOutputRoomOutputPair.getValue().getName()
                            + " (capacity = " + teacherOutputRoomOutputPair.getValue().getCapacity() + ")");
                }
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