package org.sulong.project12306.services.shortlinkservice.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.sulong.project12306.services.shortlinkservice.dao.entity.ShortLinkDO;
import org.sulong.project12306.services.shortlinkservice.dao.entity.ShortLinkGotoDO;
import org.sulong.project12306.services.shortlinkservice.dao.mapper.ShortLinkGotoMapper;
import org.sulong.project12306.services.shortlinkservice.dao.mapper.ShortLinkMapper;
import org.sulong.project12306.services.shortlinkservice.dto.req.RecycleBinRecoverReqDTO;
import org.sulong.project12306.services.shortlinkservice.dto.req.RecycleBinRemoveReqDTO;
import org.sulong.project12306.services.shortlinkservice.dto.req.RecycleBinSaveReqDTO;
import org.sulong.project12306.services.shortlinkservice.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.sulong.project12306.services.shortlinkservice.dto.resp.ShortLinkPageRespDTO;
import org.sulong.project12306.services.shortlinkservice.service.RecycleBinService;

import static org.sulong.project12306.services.shortlinkservice.common.constant.RedisKeyConstant.GOTO_IS_NULL_SHORT_LINK_KEY;
import static org.sulong.project12306.services.shortlinkservice.common.constant.RedisKeyConstant.GOTO_SHORT_LINK_KEY;

@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements RecycleBinService {

    private final StringRedisTemplate stringRedisTemplate;
    private final ShortLinkGotoMapper shortLinkGotoMapper;

    @Override
    public void saveRecycleBin(RecycleBinSaveReqDTO requestParam) {
        LambdaUpdateWrapper<ShortLinkDO> updateWrapper= Wrappers.lambdaUpdate(ShortLinkDO.class)
                .eq(ShortLinkDO::getFullShortUrl,requestParam.getFullShortUrl())
                .eq(ShortLinkDO::getGid,requestParam.getGid())
                .eq(ShortLinkDO::getDelFlag,0)
                .eq(ShortLinkDO::getEnableStatus,0);
        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .enableStatus(1)
                .build();
        baseMapper.update(shortLinkDO, updateWrapper);
        stringRedisTemplate.delete(String.format(GOTO_SHORT_LINK_KEY, requestParam.getFullShortUrl()));
    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        IPage<ShortLinkDO> resultPage = baseMapper.pageRecycleBinLink(requestParam);
        return resultPage.convert(each -> {
            ShortLinkPageRespDTO result = BeanUtil.toBean(each, ShortLinkPageRespDTO.class);
            result.setDomain("http://" + result.getDomain());
            return result;
        });
    }

    @Override
    public void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam) {
        LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getEnableStatus, 1)
                .eq(ShortLinkDO::getDelFlag, 0);
        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .enableStatus(0)
                .build();
        baseMapper.update(shortLinkDO, updateWrapper);
        stringRedisTemplate.delete(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, requestParam.getFullShortUrl()));
    }

    @Override
    public void removeRecycleBin(RecycleBinRemoveReqDTO requestParam) {
        LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getEnableStatus, 1)
                .eq(ShortLinkDO::getDelTime, 0L)
                .eq(ShortLinkDO::getDelFlag, 0);
        ShortLinkDO delShortLinkDO = ShortLinkDO.builder()
                .delTime(System.currentTimeMillis())
                .build();
        delShortLinkDO.setDelFlag(1);
        baseMapper.update(delShortLinkDO, updateWrapper);

        LambdaQueryWrapper<ShortLinkGotoDO> queryWrapper=Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                .eq(ShortLinkGotoDO::getFullShortUrl,requestParam.getFullShortUrl())
                .eq(ShortLinkGotoDO::getGid,requestParam.getGid());
        shortLinkGotoMapper.delete(queryWrapper);
    }
}
