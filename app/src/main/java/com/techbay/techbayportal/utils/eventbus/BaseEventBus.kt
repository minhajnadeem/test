package com.techbay.techbayportal.utils.eventbus

import org.greenrobot.eventbus.EventBus

object BaseEventBus {
    val eventBus: EventBus = EventBus.getDefault()
}