package com.mvpt.model;

import com.mvpt.model.dto.DepositDTO;
import com.mvpt.model.dto.WithdrawDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "withdraws")
@Accessors(chain = true)
public class Withdraw extends BaseEntities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Digits(integer = 12, fraction = 0)
    @Column(name = "transaction_amount", nullable = false)
    private BigDecimal transactionAmount;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public WithdrawDTO toWithdrawDTO() {
        return new WithdrawDTO()
                .setId(String.valueOf(id))
                .setTransactionAmount(String.valueOf(transactionAmount));
    }

}
