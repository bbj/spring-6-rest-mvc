package com.bbj.spring6restmvc.entities;

import com.bbj.spring6restmvc.model.BeerStyle;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

//@Data can generate perf/mem issues => @Getter @Setter instead
@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Beer {

    @Id
    @GeneratedValue(                        //JPA
            generator = "UUID")
    @GenericGenerator(                      //hibernate specific
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(                                //JPA
            length = 36,
            columnDefinition = "varchar",
            updatable = false,
            nullable = false)
    private UUID id;

    /**
     * version starts at 0, every update version++
     * hibernate checks the version in the db
     * if version is different => exception data changed by another process
     * and your process still has data with the old version
     *
     */
    @Version
    private Integer version;

    private String beerName;
    private BeerStyle beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}

