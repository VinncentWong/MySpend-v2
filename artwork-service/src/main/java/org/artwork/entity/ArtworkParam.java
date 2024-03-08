package org.artwork.entity;

import centwong.utility.annotation.ParamColumn;
import centwong.utility.response.HttpResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ArtworkParam {

    @ParamColumn(name = "id")
    private Long id;

    @ParamColumn(name = "id")
    private List<Long> ids;

    @ParamColumn(name = "name")
    private String name;

    @ParamColumn(name = "name")
    private List<String> names;

    @ParamColumn(name = "description")
    private String description;

    @ParamColumn(name = "description")
    private List<String> descriptions;

    @ParamColumn(name = "category")
    private String category;

    @ParamColumn(name = "category")
    private List<String> categories;

    @ParamColumn(name = "stock")
    private Long stock;

    @ParamColumn(name = "stock")
    private List<Long> stocks;

    @ParamColumn(name = "weight")
    private Double weight;

    @ParamColumn(name = "weight")
    private List<Double> weights;

    @ParamColumn(name = "dimension_x")
    private Double dimensionX;

    @ParamColumn(name = "dimension_x")
    private List<Double> dimensionXs;

    @ParamColumn(name = "dimension_y")
    private Double dimensionY;

    @ParamColumn(name = "dimension_y")
    private List<Double> dimensionYs;

    @ParamColumn(name = "dimension_z")
    private Double dimensionZ;

    @ParamColumn(name = "dimension_z")
    private List<Double> dimensionZs;

    @ParamColumn(name = "is_preorder")
    private Boolean isPreorder;

    @ParamColumn(name = "is_active")
    private Boolean isActive;

    @ParamColumn(name = "fk_user_id")
    private Long fkUserId;

    @ParamColumn(name = "fk_user_id")
    private List<Long> fkUserIds;

    private HttpResponse.PaginationParam pgParam;
}
