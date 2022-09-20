package com.winnie.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Base Paging Response Model Bean
 * @author Winnie
 * @date 2022/9/19
 */
@Data
public abstract class PagingRes implements Serializable {
    private static final long serialVersionUID = 1L;

    private int totalCount;
    private int pageNo;
}