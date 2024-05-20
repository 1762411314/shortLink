package org.sulong.project12306.services.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sulong.project12306.framework.convention.exception.ServiceException;
import org.sulong.project12306.framework.convention.result.Result;
import org.sulong.project12306.framework.user.core.UserContext;
import org.sulong.project12306.services.admin.dao.entity.GroupDO;
import org.sulong.project12306.services.admin.dao.mapper.GroupMapper;
import org.sulong.project12306.services.admin.remote.ShortLinkActualRemoteService;
import org.sulong.project12306.services.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.sulong.project12306.services.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.sulong.project12306.services.admin.service.RecycleBinService;

import java.util.List;

@Service(value = "recycleBinServiceImplByAdmin")
@RequiredArgsConstructor
public class RecycleBinServiceImpl implements RecycleBinService {

    private final ShortLinkActualRemoteService shortLinkActualRemoteService;
    private final GroupMapper groupMapper;

    @Override
    public Result<Page<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUserName())
                .eq(GroupDO::getDelFlag, 0);
        List<GroupDO> groupDOList = groupMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(groupDOList)) {
            throw new ServiceException("用户无分组信息");
        }
        requestParam.setGidList(groupDOList.stream().map(GroupDO::getGid).toList());
        return shortLinkActualRemoteService.pageRecycleBinShortLink(requestParam.getGidList(), requestParam.getCurrent(), requestParam.getSize());
    }
}
