package com.docu.scan.library

import org.junit.Assert.assertEquals
import org.junit.Test

class ScanRepositoryTest {

    @Test
    fun defaultTitle_isUntitledScan() {
        assertEquals(
            "Untitled Scan",
            ScanRepository.DEFAULT_TITLE
        )
    }
}
