package com.thoughtworks.sea.token

import com.thoughtworks.sea.model.ParsedTokenResult
import com.thoughtworks.sea.model.TokenResponse

class SCTokenParser {
    fun parseToken(token: TokenResponse): ParsedTokenResult {
        print(token)
        // TODO Yi: will implement parse method
        return ParsedTokenResult("S3000024B", null)
    }
}
