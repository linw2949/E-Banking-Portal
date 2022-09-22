package com.winnie.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank
    @Size(max = 40)
    private String id;
    @NotBlank
    @Pattern(regexp="^[A-Z]{3}$")
    private String currency;
    @NotNull
    private BigDecimal amount;
    @NotBlank
    @Size(max = 26)
    private String iban;
    @NotBlank
    @Pattern(regexp="^\\d{4}\\d{2}\\d{2}$")
    private String date;
    @Size(max = 20, message = "description is not longer than 20")
    private String description;
}
