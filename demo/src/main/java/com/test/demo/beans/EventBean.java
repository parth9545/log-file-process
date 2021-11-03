package com.test.demo.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "log_file_data")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class EventBean {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String eventId;

    private Long startTime;

    private Long endTime;

    private Long duration;

    private String type;

    private String host;

    @Column(columnDefinition = "boolean default false")
    private Boolean alert;

}
