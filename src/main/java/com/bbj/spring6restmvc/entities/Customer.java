package com.bbj.spring6restmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

//@Data can generate perf/mem issues => @Getter @Setter instead
@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(                        //JPA
            generator = "UUID")
    @GenericGenerator(                      //hibernate specific
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(                                //JPA
            length = 36,                    //UUID take space but ensure really unique
            columnDefinition = "varchar",
            updatable = false,
            nullable = false)
    private UUID id;

    private String name;

    /**
     * version starts at 0, every update version++
     * hibernate checks the version in the db
     * if version is different => exception data changed by another process
     * and your process still has data with the old version
     *
     */
    @Version
    private Integer version;

    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
