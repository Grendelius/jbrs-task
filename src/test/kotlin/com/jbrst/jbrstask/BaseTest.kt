package com.jbrst.jbrstask

import com.jbrst.jbrstask.core.listeners.LogListener
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Listeners

@Listeners(LogListener::class)
@SpringBootTest(classes = [SpringTestApplication::class], webEnvironment = WebEnvironment.NONE)
open class BaseTest : AbstractTestNGSpringContextTests()