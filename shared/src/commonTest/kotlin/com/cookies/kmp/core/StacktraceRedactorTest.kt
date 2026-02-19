package com.cookies.kmp.core

import kotlin.test.Test
import kotlin.test.assertEquals

class StacktraceRedactorTest {

    @Test
    fun whenStacktraceContainsWhereClause_thenWhereClauseIsRedactedAndBase64Encoded() {
        val stacktrace =
            """
            Error on line 1
            UPDATE test
            SET test=1
            WHERE test=0
            This is a test error
            """.trimIndent()

        val encoded = redactStacktraceInBase64(stacktrace)

        assertEquals(
            "RXJyb3Igb24gbGluZSAxClVQREFURSB0ZXN0ClNFVCB0ZXN0PTEKClRoaXMgaXMgYSB0ZXN0IGVycm9y",
            encoded,
        )
    }
}
