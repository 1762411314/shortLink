package org.sulong.project12306.services.shortlinkservice.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.sulong.project12306.framework.convention.result.Result;
import org.sulong.project12306.framework.web.Results;
import org.sulong.project12306.services.shortlinkservice.dto.req.ShortLinkBatchCreateReqDTO;
import org.sulong.project12306.services.shortlinkservice.dto.req.ShortLinkCreateReqDTO;
import org.sulong.project12306.services.shortlinkservice.dto.req.ShortLinkPageReqDTO;
import org.sulong.project12306.services.shortlinkservice.dto.req.ShortLinkUpdateReqDTO;
import org.sulong.project12306.services.shortlinkservice.dto.resp.ShortLinkBatchCreateRespDTO;
import org.sulong.project12306.services.shortlinkservice.dto.resp.ShortLinkCreateRespDTO;
import org.sulong.project12306.services.shortlinkservice.dto.resp.ShortLinkGroupCountQueryRespDTO;
import org.sulong.project12306.services.shortlinkservice.dto.resp.ShortLinkPageRespDTO;
import org.sulong.project12306.services.shortlinkservice.handler.CustomBlockHandler;
import org.sulong.project12306.services.shortlinkservice.service.ShortLinkService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShortLinkController {
    private final ShortLinkService shortLinkService;

    /**
     * 创建短链接
     */
    @PostMapping("/api/short-link/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam){
        return Results.success(shortLinkService.createShortLink(requestParam));
    }

    /**
     * 短链接跳转原始链接
     */
    @GetMapping("/{short-uri}")
    public void restoreUrl(@PathVariable("short-uri") String shortUri, ServletRequest request, ServletResponse response) {
        shortLinkService.restoreUrl(shortUri, request, response);
    }
    /**
     * 通过分布式锁创建短链接
     */
    @PostMapping("/api/short-link/v1/create/by-lock")
    public Result<ShortLinkCreateRespDTO> createShortLinkByLock(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return Results.success(shortLinkService.createShortLinkByLock(requestParam));
    }

    /**
     * 批量创建短链接
     */
    @PostMapping("/api/short-link/v1/create/batch")
    public Result<ShortLinkBatchCreateRespDTO> batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam) {
        return Results.success(shortLinkService.batchCreateShortLink(requestParam));
    }

    /**
     * 修改短链接
     */
    @PostMapping("/api/short-link/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkService.updateShortLink(requestParam);
        return Results.success();
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/short-link/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return Results.success(shortLinkService.pageShortLink(requestParam));
    }

    /**
     * 查询短链接分组内数量
     */
    @GetMapping("/api/short-link/v1/count")
    public Result<List<ShortLinkGroupCountQueryRespDTO>> listGroupShortLinkCount(@RequestParam("requestParam") List<String> requestParam) {
        return Results.success(shortLinkService.listGroupShortLinkCount(requestParam));
    }
}
