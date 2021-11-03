package com.test.demo.beans;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "log_file_info")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class FileBean implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileName;

    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date processedDate;

}
