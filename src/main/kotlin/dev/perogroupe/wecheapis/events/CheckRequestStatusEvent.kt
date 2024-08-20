package dev.perogroupe.wecheapis.events

import dev.perogroupe.wecheapis.dtos.requests.CheckRequestStatusReq
import org.springframework.context.ApplicationEvent

class CheckRequestStatusEvent(source: Any,val  checkRequestStatusReq: CheckRequestStatusReq) : ApplicationEvent(source)