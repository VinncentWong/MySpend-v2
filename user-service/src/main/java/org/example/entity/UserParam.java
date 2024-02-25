package org.example.entity;

import centwong.utility.annotation.ParamColumn;
import centwong.utility.response.HttpResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserParam {

    @ParamColumn(name = "id")
    private Long id;

    @ParamColumn(name = "id")
    private List<Long> ids;

    @ParamColumn(name = "email")
    private String email;

    @ParamColumn(name = "email")
    private List<String> emails;

    @ParamColumn(name = "name")
    private String name;

    @ParamColumn(name = "name")
    private List<String> names;

    @ParamColumn(name = "is_active")
    private Boolean isActive;

    private HttpResponse.PaginationParam pg;
}
