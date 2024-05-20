package org.sulong.project12306.framework.idempotent.core.token;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sulong.project12306.framework.convention.result.Result;
import org.sulong.project12306.framework.web.Results;

@RestController
@RequiredArgsConstructor
public class IdempotentTokenController {
    private final IdempotentTokenService idempotentTokenService;

    @GetMapping("/token")
    public Result<String> createToken(){
        return Results.success(idempotentTokenService.createToken());
    }
}
