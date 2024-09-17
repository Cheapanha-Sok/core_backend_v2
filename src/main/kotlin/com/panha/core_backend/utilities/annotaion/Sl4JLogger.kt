package com.panha.core_backend.utilities.annotaion

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Sl4JLogger {
    companion object {
        inline var <reified T> T.log: Logger
            get() = LoggerFactory.getLogger(T::class.java)
            set(value) {}
    }
}