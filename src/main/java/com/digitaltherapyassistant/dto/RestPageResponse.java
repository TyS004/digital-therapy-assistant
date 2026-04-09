package com.digitaltherapyassistant.dto;

import java.util.List;
import lombok.Data;

@Data
public class RestPageResponse<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int number;
    private int size;
    private boolean last;
    private boolean first;
}