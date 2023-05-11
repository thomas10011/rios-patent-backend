package org.rioslab.patent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author thomas
 * @since 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Publications对象", description="")
public class Publications implements Serializable {


    @TableField("Pub_num")
    @TableId(value = "Pub_num")
    private String pubNum;

    @TableField("RPC")
    private String rpc;

    @TableField("Title")
    private String Title;

    @TableField("Abstract")
    private String Abstract;

    @TableField("Description")
    private String Description;

    @TableField("Recommend_Pub_nums")
    private String recommendPubNums;


}
