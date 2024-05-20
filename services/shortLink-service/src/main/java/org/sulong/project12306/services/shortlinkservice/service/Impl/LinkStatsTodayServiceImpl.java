package org.sulong.project12306.services.shortlinkservice.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.sulong.project12306.services.shortlinkservice.dao.entity.LinkStatsTodayDO;
import org.sulong.project12306.services.shortlinkservice.dao.mapper.LinkStatsTodayMapper;
import org.sulong.project12306.services.shortlinkservice.service.LinkStatsTodayService;

/**
 * 短链接今日统计接口实现层
 */
@Service
public class LinkStatsTodayServiceImpl extends ServiceImpl<LinkStatsTodayMapper, LinkStatsTodayDO> implements LinkStatsTodayService {
}
