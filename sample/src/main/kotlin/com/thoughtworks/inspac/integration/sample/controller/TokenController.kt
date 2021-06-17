package com.thoughtworks.inspac.integration.sample.controller

import com.thoughtworks.inspac.integration.sample.service.TokenService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/token")
class TokenController(private val tokenService: TokenService) {

    @GetMapping("/oidc")
    fun issueOIDCToken(
        @RequestParam @Validated code: String,
        @RequestParam @Validated state: String,
        model: Model
    ): String {
        model.addAttribute("userInfo", tokenService.getServiceToken(code, state))
        return "userInfo-oidc"
    }
}
