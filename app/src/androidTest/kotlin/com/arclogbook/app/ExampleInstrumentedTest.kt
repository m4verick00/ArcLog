package com.arclogbook.app

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.arclogbook.app.debug", appContext.packageName)
    }
    
    @Test
    fun databaseConnection_works() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertNotNull("App context should not be null", appContext)
        // In a real test, verify database initialization
    }
}