package org.sulong.project12306.services.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.sulong.project12306.framework.convention.result.Result;
import org.sulong.project12306.services.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.sulong.project12306.services.admin.remote.dto.resp.ShortLinkPageRespDTO;
/**
 * URL 回收站接口层
 */
public interface RecycleBinService {

    /**
     * 分页查询回收站短链接
     *
     * @param requestParam 请求参数
     * @return 返回参数包装
     */
    Result<Page<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO requestParam);
}
