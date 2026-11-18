package com.example.useless4370silencer.hook

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CellBroadcastFilterTest {
    private class FakeLocation(private val plmn: String) {
        fun getPlmn(): String = plmn
    }

    private class FakeMessage(
        private val location: FakeLocation,
        private val serviceCategory: Int,
    ) {
        fun getLocation(): FakeLocation = location
        fun getServiceCategory(): Int = serviceCategory
    }

    @Test
    fun matchesTargetMccAndMessageId() {
        assertTrue(
            CellBroadcastFilter.matches(
                FakeMessage(
                    location = FakeLocation(plmn = "45006"),
                    serviceCategory = CellBroadcastFilter.TARGET_MESSAGE_ID,
                )
            )
        )
    }

    @Test
    fun ignoresOtherMessageIds() {
        assertFalse(
            CellBroadcastFilter.matches(
                FakeMessage(
                    location = FakeLocation(plmn = "45006"),
                    serviceCategory = 4371,
                )
            )
        )
    }

    @Test
    fun ignoresOtherMccs() {
        assertFalse(
            CellBroadcastFilter.matches(
                FakeMessage(
                    location = FakeLocation(plmn = "310410"),
                    serviceCategory = CellBroadcastFilter.TARGET_MESSAGE_ID,
                )
            )
        )
    }
}
