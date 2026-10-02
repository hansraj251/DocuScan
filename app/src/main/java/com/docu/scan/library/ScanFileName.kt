package com.docu.scan.library

object ScanFileName {

    fun pdf(timestamp: Long): String {
        return "scan_$timestamp.pdf"
    }
}
