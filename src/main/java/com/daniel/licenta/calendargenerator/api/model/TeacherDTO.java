package com.daniel.licenta.calendargenerator.api.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDTO implements Serializable {

    private Long id;

    private String name;

}
