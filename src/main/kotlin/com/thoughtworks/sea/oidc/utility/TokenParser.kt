package com.thoughtworks.sea.oidc.utility

import com.thoughtworks.sea.oidc.model.ParsedTokenResult
import com.thoughtworks.sea.oidc.model.TokenResponse

class TokenParser {

    companion object {

        @JvmStatic
        fun parseToken(token: TokenResponse): ParsedTokenResult {
            print(token)
            // TODO Yi: will implement parse method
            return ParsedTokenResult("S3000024B", null)
        }
    }
}
