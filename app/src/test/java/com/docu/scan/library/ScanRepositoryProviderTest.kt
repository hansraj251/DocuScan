package com.docu.scan.library

import org.junit.Assert.assertEquals
import org.junit.Test

class ScanRepositoryProviderTest {

    @Test
    fun provider_hasExpectedDatabaseName() {
        assertEquals(
            ScanDatabaseProvider.DATABASE_NAME,
            ScanRepositoryProvider.DATABASE_NAME
        )
    }
}
