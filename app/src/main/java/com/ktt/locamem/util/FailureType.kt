package com.ktt.locamem.util

enum class FailureType {
    EMPTY_USER_NAME,
    INVALID_USERNAME,
    EMPTY_PASSWORD,
    WRONG_PASSWORD,
    USER_EXIST,
    INVALID_PASSWORD,
    RETYPE_EMPTY_PASSWORD,
    RETYPE_PASSWORD_NOT_MATCHED,
    EMPTY_CAPTCHA,
    CAPTCHA_NOT_MATCHED
}