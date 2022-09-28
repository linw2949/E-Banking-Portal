package com.winnie.demo.service.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FormatTest {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Test
    void dateFormat() {

        assertThat(Format.dateFormat("20220920","01").equals("2022-09-20"));
        assertThat(Format.dateFormat("20220112","01").equals("2022-01-12"));
        assertThat(Format.dateFormat("20221231","01").equals("2022-12-31"));

        assertThrows(RuntimeException.class, () -> Format.dateFormat("31/02/2022","01"), "Expected RuntimeException to throw, but it didn't");
        assertThrows(RuntimeException.class, () -> Format.dateFormat("02.13.2022","01"), "Expected RuntimeException to throw, but it didn't");
    }
}