package org.sulong.project12306.services.shortlinkservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sulong.project12306.services.shortlinkservice.dao.entity.ShortLinkDO;
import org.sulong.project12306.services.shortlinkservice.dto.req.RecycleBinRecoverReqDTO;
import org.sulong.project12306.services.shortlinkservice.dto.req.RecycleBinRemoveReqDTO;
import org.sulong.project12306.services.shortlinkservice.dto.req.RecycleBinSaveReqDTO;
import org.sulong.project12306.services.shortlinkservice.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.sulong.project12306.services.shortlinkservice.dto.resp.ShortLinkPageRespDTO;


/**
 * 回收站管理接口层
 */
public interface RecycleBinService extends IService<ShortLinkDO> {

    /**
     * 保存回收站
     *
     * @param requestParam 请求参数
     */
    void saveRecycleBin(RecycleBinSaveReqDTO requestParam);

    /**
     * 分页查询短链接
     *
     * @param requestParam 分页查询短链接请求参数
     * @return 短链接分页返回结果
     */
    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam);

    /**
     * 从回收站恢复短链接
     *
     * @param requestParam 恢复短链接请求参数
     */
    void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam);

    /**
     * 从回收站移除短链接
     *
     * @param requestParam 移除短链接请求参数
     */
    void removeRecycleBin(RecycleBinRemoveReqDTO requestParam);
}
