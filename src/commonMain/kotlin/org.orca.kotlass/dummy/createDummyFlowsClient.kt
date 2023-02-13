package org.orca.kotlass.dummy

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.orca.kotlass.FlowKotlassClient
import org.orca.kotlass.KotlassClient

fun createDummyFlowsClient(
    dispatcher: CoroutineDispatcher,
    fakeWaitTime: Long = 0L
) =
    FlowKotlassClient(
        object : KotlassClient.CompassClientCredentials
        {
            override val cookie = ""
            override val domain = ""
            override val userId = 0
        },
        CoroutineScope(dispatcher),
        kotlassClient = DummyKotlassClient(
            "",
            fakeWaitTime
        )
        )