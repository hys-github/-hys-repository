package com.hys.project.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberConfirmInfoPO {
    private Integer id;

    private Integer memberId;

    private String payNum;

    private String cardNum;

   
}