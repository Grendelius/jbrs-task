package com.jbrst.jbrstask.core

import org.apache.hc.core5.http.HttpStatus
import org.apache.hc.core5.http.HttpStatus.SC_NOT_FOUND
import retrofit2.Response
import strikt.api.Assertion


private val successStatus = HttpStatus.SC_OK..HttpStatus.SC_IM_USED
private val errorStatus = HttpStatus.SC_CLIENT_ERROR..HttpStatus.SC_NETWORK_AUTHENTICATION_REQUIRED

fun <T> Assertion.Builder<Response<T>>.isOk(): Assertion.Builder<Response<T>> =
    assert("response code is in $successStatus") {
        when (it.code()) {
            in successStatus -> pass()
            else -> fail(
                description = "in fact it is %s",
                actual = it.code()
            )
        }
    }

fun <T> Assertion.Builder<Response<T>>.isNotFound(): Assertion.Builder<Response<T>> =
    assert("response code is in $SC_NOT_FOUND") {
        when (it.code()) {
            SC_NOT_FOUND -> pass()
            else -> fail(
                description = "in fact it is %s",
                actual = it.code()
            )
        }
    }