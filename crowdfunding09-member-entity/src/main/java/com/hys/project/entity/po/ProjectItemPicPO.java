package com.hys.project.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectItemPicPO {
    private Integer id;

    private Integer projectid;

    private String itemPicPath;

    
}