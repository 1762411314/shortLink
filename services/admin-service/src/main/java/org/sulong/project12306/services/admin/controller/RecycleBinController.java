package org.sulong.project12306.services.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.sulong.project12306.framework.convention.result.Result;
import org.sulong.project12306.framework.idempotent.annotation.Idempotent;
import org.sulong.project12306.framework.idempotent.enums.IdempotentSceneEnum;
import org.sulong.project12306.framework.idempotent.enums.IdempotentTypeEnum;
import org.sulong.project12306.framework.web.Results;
import org.sulong.project12306.services.admin.dto.req.RecycleBinRecoverReqDTO;
import org.sulong.project12306.services.admin.dto.req.RecycleBinRemoveReqDTO;
import org.sulong.project12306.services.admin.dto.req.RecycleBinSaveReqDTO;
import org.sulong.project12306.services.admin.remote.ShortLinkActualRemoteService;
import org.sulong.project12306.services.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.sulong.project12306.services.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.sulong.project12306.services.admin.service.RecycleBinService;

/**
 * 回收站管理控制层
 */
@RestController(value = "recycleBinControllerByAdmin")
@RequiredArgsConstructor
public class RecycleBinController {

    private final RecycleBinService recycleBinService;
    private final ShortLinkActualRemoteService shortLinkActualRemoteService;

    /**
     * 保存回收站
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam) {
        shortLinkActualRemoteService.saveRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 分页查询回收站短链接
     */
    @GetMapping("/api/short-link/admin/v1/recycle-bin/page")
    public Result<Page<ShortLinkPageRespDTO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        return recycleBinService.pageRecycleBinShortLink(requestParam);
    }

    /**
     * 恢复短链接
     */
    @Idempotent(
            type = IdempotentTypeEnum.SPEL,
            scene = IdempotentSceneEnum.RESTAPI,
            uniqueKeyPrefix = "short-link:recycle-bin:recover",
            key = "#requestParam.getFullShortUrl()"
    )
    @PostMapping("/api/short-link/admin/v1/recycle-bin/recover")
    public Result<Void> recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam) {
        shortLinkActualRemoteService.recoverRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 移除短链接
     */
    @Idempotent(
            type = IdempotentTypeEnum.SPEL,
            scene = IdempotentSceneEnum.RESTAPI,
            uniqueKeyPrefix = "short-link:recycle-bin:remove",
            key = "#requestParam.getFullShortUrl()"
    )
    @PostMapping("/api/short-link/admin/v1/recycle-bin/remove")
    public Result<Void> removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam) {
        shortLinkActualRemoteService.removeRecycleBin(requestParam);
        return Results.success();
    }
}
