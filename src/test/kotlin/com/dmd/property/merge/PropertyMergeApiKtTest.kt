package com.dmd.property.merge

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


internal class PropertyMergeApiKtTest {

    @Test
    fun propertyMergeService() {
        assertThat(PropertyMergeApi.propertyMergeService()).isNotNull
    }
}