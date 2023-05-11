package org.rioslab.patent.controller;


import io.swagger.annotations.ApiOperation;
import org.rioslab.patent.api.CommonResult;
import org.rioslab.patent.entity.Publications;
import org.rioslab.patent.service.IPublicationsService;
import org.rioslab.patent.vo.PublicationsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author thomas
 * @since 2023-04-28
 */
@RestController
@RequestMapping("/patent/publications")
@CrossOrigin
public class PublicationsController {
    @Autowired
    IPublicationsService pubService;

    @ApiOperation("根据ID查询专利信息")
    @GetMapping("/{pubID}")
    CommonResult<?> getPublications(@PathVariable("pubID") String pubID) {
        System.out.println(pubID);
        Publications pub = pubService.getById(pubID);
        String[] rec = pub.getRecommendPubNums().split(", ");
        List<String> stripped = new ArrayList<>(10);
        for (String text : rec) {
            stripped.add(text.substring(1, text.length() - 1));
        }
        PublicationsVO ret = new PublicationsVO();
        ret
            .setAbstract(pub.getAbstract())
            .setPubNum(pub.getPubNum())
            .setTitle(pub.getTitle())
            .setRpc(pub.getRpc())
            .setRecommendPubNums(stripped)
            .setDescription(pub.getDescription())
            ;
        return CommonResult.success().append(ret);
    }

}
