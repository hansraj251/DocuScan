package com.docu.scan.library

import org.junit.Assert.assertEquals
import org.junit.Test

class PdfStorageTest {

    @Test
    fun scanDirectoryName_isStable() {
        assertEquals("scans", PdfStorage.SCAN_DIRECTORY)
    }
}
