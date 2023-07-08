package com.jbrst.jbrstask.core.listeners

import com.jbrst.jbrstask.core.util.AllureUtils
import mu.KLogging
import org.testng.ITestContext
import org.testng.ITestListener
import org.testng.ITestResult

class LogListener : ITestListener {

    companion object : KLogging()

    override fun onTestStart(result: ITestResult?) {
        logger.info { "[TEST][STARTING] {${result?.method?.methodName}}. Arguments [${result?.parameters}]" }
    }

    override fun onTestSuccess(result: ITestResult?) {
        logger.info { "[TEST][PASSED] {${result?.method?.methodName}}. Arguments [${result?.parameters}]" }
    }

    override fun onTestFailure(result: ITestResult?) {
        logger.info {
            "[TEST][FAILED] {${result?.method?.methodName}}. Arguments [${result?.parameters}]. " +
                    "Reason: ${result?.throwable?.localizedMessage}"
        }
    }

    override fun onTestSkipped(result: ITestResult?) {
        logger.info {
            "[TEST][SKIPPED] {${result?.method?.methodName}}. Arguments [${result?.parameters}]. " +
                    "Reason: ${result?.throwable?.message}"
        }
    }

    override fun onFinish(context: ITestContext?) {
        AllureUtils.writeUpAllureEnvironment()
    }

}