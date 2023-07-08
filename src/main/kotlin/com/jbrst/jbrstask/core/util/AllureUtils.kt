package com.jbrst.jbrstask.core.util

import com.google.common.collect.ImmutableMap
import mu.KLogging
import org.w3c.dom.Document
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerConfigurationException
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

object AllureUtils {

    fun writeUpAllureEnvironment() {
        val os = System.getProperty("os.name");
    }

    private object AllureEnvironmentWriter : KLogging() {

        private const val ALLURE_RESULTS_DIR = "/build/allure-results"
        private const val ENVIRONMENT_FILE = "environment.xml"
        private val TRANSFORMER = prepareTransformer()

        fun writeEnvInfo(
            environmentValuesSet: ImmutableMap<String, String>,
            customResultsPath: String = System.getProperty("user.dir") + ALLURE_RESULTS_DIR
        ) {
            // Write the content into xml file
            val allureResultsDir = File(customResultsPath)
            if (!allureResultsDir.exists()) {
                allureResultsDir.mkdirs()
            }
            val result = StreamResult(File(customResultsPath + ENVIRONMENT_FILE))
            try {
                with(TRANSFORMER) {
                    transform(prepareDomSource(createDocument(environmentValuesSet)), result)
                }
            } catch (e: TransformerException) {
                error("Could not transform the DOM source document")
            }
        }

        private fun createDocument(environmentValuesSet: ImmutableMap<String, String>): Document {
            try {
                val docFactory = DocumentBuilderFactory.newInstance()
                val docBuilder = docFactory.newDocumentBuilder()
                return docBuilder.newDocument().apply {
                    val environment = createElement("environment")
                    appendChild(environment)
                    environmentValuesSet.forEach { (k, v) ->
                        val parameter = createElement("parameter").apply {
                            appendChild(createElement("key").apply { appendChild(createTextNode(k)) })
                            appendChild(createElement("value").apply { appendChild(createTextNode(v)) })
                        }
                        environment.appendChild(parameter)
                    }
                }
            } catch (e: ParserConfigurationException) {
                error("Impossible to parse the provided environment params $environmentValuesSet")
            }
        }

        private fun prepareDomSource(document: Document): DOMSource {
            return DOMSource(document)
        }

        private fun prepareTransformer(): Transformer {
            val transformerFactory = TransformerFactory.newInstance()
            return try {
                transformerFactory.newTransformer()
            } catch (e: TransformerConfigurationException) {
                error("Impossible to create a XML transformer")
            }
        }

    }
}