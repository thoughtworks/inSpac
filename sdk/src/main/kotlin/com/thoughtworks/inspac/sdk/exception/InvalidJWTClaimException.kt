package com.thoughtworks.inspac.sdk.exception

/**
* Throw the exception with detail message when JWT Claim is invalid
*/
class InvalidJWTClaimException(message: String) : Throwable(message)
