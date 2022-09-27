package com.winnie.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
public class DAOTransaction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column
    @NotBlank
    @Size(max = 50)
    private String id;
    @Column(updatable = false, nullable = false)
    @NotBlank
    @Pattern(regexp="^[A-Z]{3}$")
    private String currency;
    @Column(updatable = false, nullable = false)
    @NotNull
    private BigDecimal amount;
    @Column(updatable = false, nullable = false)
    @NotBlank
    @Size(max = 26)
    private String iban;
    @Column(updatable = false, nullable = false)
    @NotBlank
    @Pattern(regexp="^\\d{4}\\d{2}\\d{2}$")
    private String date;
    @Column(updatable = false, nullable = false)
    @Size(max = 20, message = "description is not longer than 20")
    private String description;
}
