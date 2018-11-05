package com.fbieck.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SymbolRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idsymbolrelation")
    private Integer id;

    private String symbol;

    private Long userid;
}
