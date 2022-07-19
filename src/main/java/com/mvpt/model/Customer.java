package com.mvpt.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer extends BaseEntities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Digits(integer = 12, fraction = 0)
    @Column(updatable = false)
    private BigDecimal balance;

    @OneToOne
    @JoinColumn(name = "location_region_id", nullable = false)
    private LocationRegion locationRegion;

    @OneToMany(mappedBy = "customer")
    private Set<Deposit> deposits;

    @OneToMany(mappedBy = "customer")
    private Set<Withdraw> withdraws;

    @OneToMany(mappedBy = "sender")
    private Set<Transfer> sender;

    @OneToMany(mappedBy = "recipient")
    private Set<Transfer> recipient;

}
