package com.daniel.licenta.calendargenerator.business.model.json;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"assignedStudentClasses", "unavailabilityIntervals"})
public class TeacherData implements Serializable {

    Long identifier;
    String name;
    Set<ClassAssignment> assignedStudentClasses = new HashSet<>();
    Map<Integer, List<Integer>> unavailabilityIntervals = new HashMap<>();

    public Long retrieveNumberOfAssignedHours() {
        return assignedStudentClasses.stream().mapToLong(ClassAssignment::getNumberOfHours).sum();
    }

    public void addUnavailabilityInterval(int day, int hour) {
        if (unavailabilityIntervals.containsKey(day)) {
            unavailabilityIntervals.get(day).add(hour);
        } else {
            unavailabilityIntervals.put(day, Arrays.asList(hour));
        }
    }

    public void addStudentClass(StudentData studentData, int numberOfHours, long lectureId) {
        assignedStudentClasses.add(new ClassAssignment(studentData.getIdentifier(), numberOfHours, lectureId));

    }

    public ClassAssignment retrieveAssignmentByClassId(Long classId) {
        return assignedStudentClasses.stream()
                .filter(studentDataIntegerPair -> studentDataIntegerPair.getClassId().equals(classId))
                .findAny()
                .orElse(null);
    }
}
