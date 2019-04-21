package com.github.shaad.backend.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface WithLogger {
    val log: Logger
        get() = LoggerFactory.getLogger(this::class.java)
}