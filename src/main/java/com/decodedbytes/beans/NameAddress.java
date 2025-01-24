package com.decodedbytes.beans;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "NAME_ADDRESS")
@NamedQuery(name = "fetchAllRows", query = "Select n from NameAddress n")
public class NameAddress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "house_number")
    private String houseNumber;

    private String city;

    private String province;

    @Column(name = "postal_code")
    private String postalCode;

}
