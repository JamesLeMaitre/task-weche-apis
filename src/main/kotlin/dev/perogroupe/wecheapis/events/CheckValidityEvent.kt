package dev.perogroupe.wecheapis.events

import dev.perogroupe.wecheapis.dtos.requests.CheckValidityRequest
import org.springframework.context.ApplicationEvent

class CheckValidityEvent(source: Any, val request: CheckValidityRequest) : ApplicationEvent(source)