package org.rioslab.patent.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
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
@ApiModel(value="PublicationsVO对象", description="")
public class MavenCompileVO implements Serializable {

    private String output;

    private String codeID;

    private int exitCode;

}
