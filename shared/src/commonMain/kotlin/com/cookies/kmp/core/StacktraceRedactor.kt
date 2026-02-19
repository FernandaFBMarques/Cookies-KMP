package com.cookies.kmp.core

fun redactStacktraceInBase64(stacktrace: String): String {
    val start = stacktrace.indexOf("WHERE")
    val redacted = if (start == -1) {
        stacktrace
    } else {
        val finish = stacktrace.substring(start).indexOf("\n") + start
        if (finish > start) {
            stacktrace.removeRange(start, finish)
        } else {
            stacktrace
        }
    }

    return encodeBase64UrlNoPadding(redacted.encodeToByteArray())
}

private fun encodeBase64UrlNoPadding(bytes: ByteArray): String {
    if (bytes.isEmpty()) return ""

    val table = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"
    val output = StringBuilder((bytes.size * 4 + 2) / 3)

    var index = 0
    while (index + 2 < bytes.size) {
        val b0 = bytes[index++].toInt() and 0xFF
        val b1 = bytes[index++].toInt() and 0xFF
        val b2 = bytes[index++].toInt() and 0xFF

        output.append(table[b0 ushr 2])
        output.append(table[((b0 and 0x03) shl 4) or (b1 ushr 4)])
        output.append(table[((b1 and 0x0F) shl 2) or (b2 ushr 6)])
        output.append(table[b2 and 0x3F])
    }

    val remaining = bytes.size - index
    if (remaining == 1) {
        val b0 = bytes[index].toInt() and 0xFF
        output.append(table[b0 ushr 2])
        output.append(table[(b0 and 0x03) shl 4])
    } else if (remaining == 2) {
        val b0 = bytes[index++].toInt() and 0xFF
        val b1 = bytes[index].toInt() and 0xFF
        output.append(table[b0 ushr 2])
        output.append(table[((b0 and 0x03) shl 4) or (b1 ushr 4)])
        output.append(table[(b1 and 0x0F) shl 2])
    }

    return output.toString()
}
