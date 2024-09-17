package com.munywele.sms.utils

object NumberUtils {

    fun extractFirstAmountAsInt(text: String): Double {
        // Regular expression to match the first amount in the format KshX,XXX.XX
        val regex = Regex("""Ksh([\d,]+\.\d{2})""")
        // Find the first match
        val matchResult = regex.find(text)?.groups?.get(1)?.value
        // Remove commas and convert to an integer (by truncating the decimal part)
        val amountFound = matchResult?.replace(",", "")?.split(".")?.first()?.toDouble() ?: 0.0

        return amountFound
    }

    private fun extractAmountFromSms(body: String): Double {
        val regex = Regex("""Ksh[\s,]*(\d+(?:,\d{3})*(?:\.\d{2})?)""")
        val match = regex.find(body)
        val amountFound = match?.groups?.get(1)?.value?.replace(",", "")?.toDouble() ?: 0.0

        return amountFound
    }

}