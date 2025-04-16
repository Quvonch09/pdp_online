package com.example.pdponline.payload.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResPageable {

    private int page;
    private int size;
    private int totalPage;
    private long totalElements;
    private Object body;
}
