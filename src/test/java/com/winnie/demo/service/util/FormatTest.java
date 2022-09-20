package com.winnie.demo.service.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FormatTest {

    @Test
    void dateFormat() {
        assertThat(Format.dateFormat("20-09-2022").equals("2022-09-20"));
        assertThat(Format.dateFormat("12-01-2022").equals("2022-01-12"));
        assertThat(Format.dateFormat("31-12-2022").equals("2022-12-31"));

        assertThrows(RuntimeException.class, () -> Format.dateFormat("31/02/2022"), "Expected RuntimeException to throw, but it didn't");
        assertThrows(RuntimeException.class, () -> Format.dateFormat("02.13.2022"), "Expected RuntimeException to throw, but it didn't");
    }
}