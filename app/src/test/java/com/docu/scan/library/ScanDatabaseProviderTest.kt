package com.docu.scan.library

import org.junit.Assert.assertEquals
import org.junit.Test

class ScanDatabaseProviderTest {

    @Test
    fun databaseName_isStable() {
        assertEquals(
            "docuscan.db",
            ScanDatabaseProvider.DATABASE_NAME
        )
    }
}
