package org.rioslab.patent.service.impl;

import org.rioslab.patent.entity.Publications;
import org.rioslab.patent.mapper.PublicationsMapper;
import org.rioslab.patent.service.IPublicationsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author thomas
 * @since 2023-04-28
 */
@Service
public class PublicationsServiceImpl extends ServiceImpl<PublicationsMapper, Publications> implements IPublicationsService {

}
