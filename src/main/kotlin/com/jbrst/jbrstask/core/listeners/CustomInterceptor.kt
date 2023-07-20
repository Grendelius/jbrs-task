package com.jbrst.jbrstask.core.listeners

import com.jbrst.jbrstask.core.annotations.DesktopTest
import org.testng.IMethodInstance
import org.testng.IMethodInterceptor
import org.testng.ITestContext

class CustomInterceptor : IMethodInterceptor {

    override fun intercept(list: MutableList<IMethodInstance>, cntx: ITestContext): MutableList<IMethodInstance> {
        return list.filter {
            !(it.method.constructorOrMethod.method.isAnnotationPresent(DesktopTest::class.java)
                    && cntx.currentXmlTest.getParameter("browser")?.contains("_MOBILE") == true)
        }.toMutableList()
    }

}