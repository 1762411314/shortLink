package org.sulong.project12306.services.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.sulong.project12306.framework.convention.result.Result;
import org.sulong.project12306.framework.idempotent.annotation.Idempotent;
import org.sulong.project12306.framework.idempotent.enums.IdempotentSceneEnum;
import org.sulong.project12306.framework.idempotent.enums.IdempotentTypeEnum;
import org.sulong.project12306.framework.web.Results;
import org.sulong.project12306.services.admin.remote.ShortLinkActualRemoteService;
import org.sulong.project12306.services.admin.remote.dto.req.ShortLinkBatchCreateReqDTO;
import org.sulong.project12306.services.admin.remote.dto.req.ShortLinkCreateReqDTO;
import org.sulong.project12306.services.admin.remote.dto.req.ShortLinkPageReqDTO;
import org.sulong.project12306.services.admin.remote.dto.req.ShortLinkUpdateReqDTO;
import org.sulong.project12306.services.admin.remote.dto.resp.ShortLinkBaseInfoRespDTO;
import org.sulong.project12306.services.admin.remote.dto.resp.ShortLinkBatchCreateRespDTO;
import org.sulong.project12306.services.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import org.sulong.project12306.services.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.sulong.project12306.services.admin.toolkit.EasyExcelWebUtil;

import java.util.List;

/**
 * 短链接后管控制层
 */
@RestController(value = "shortLinkControllerByAdmin")
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkActualRemoteService shortLinkActualRemoteService;

    /**
     * 创建短链接
     */
    @Idempotent(
            type = IdempotentTypeEnum.PARAM,
            scene = IdempotentSceneEnum.RESTAPI
    )
    @PostMapping("/api/short-link/admin/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return shortLinkActualRemoteService.createShortLink(requestParam);
    }

    /**
     * 批量创建短链接
     */
    @Idempotent(
            type = IdempotentTypeEnum.PARAM,
            scene = IdempotentSceneEnum.RESTAPI
    )
    @SneakyThrows
    @PostMapping("/api/short-link/admin/v1/create/batch")
    public void batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam, HttpServletResponse response) {
        Result<ShortLinkBatchCreateRespDTO> shortLinkBatchCreateRespDTOResult = shortLinkActualRemoteService.batchCreateShortLink(requestParam);
        if (shortLinkBatchCreateRespDTOResult.isSuccess()) {
            List<ShortLinkBaseInfoRespDTO> baseLinkInfos = shortLinkBatchCreateRespDTOResult.getData().getBaseLinkInfos();
            EasyExcelWebUtil.write(response, "批量创建短链接-SaaS短链接系统", ShortLinkBaseInfoRespDTO.class, baseLinkInfos);
        }
    }

    /**
     * 修改短链接
     */
    @Idempotent(
            type = IdempotentTypeEnum.PARAM,
            scene = IdempotentSceneEnum.RESTAPI
    )
    @PostMapping("/api/short-link/admin/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkActualRemoteService.updateShortLink(requestParam);
        return Results.success();
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/short-link/admin/v1/page")
    public Result<Page<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return shortLinkActualRemoteService.pageShortLink(requestParam.getGid(), requestParam.getOrderTag(), requestParam.getCurrent(), requestParam.getSize());
    }
}
