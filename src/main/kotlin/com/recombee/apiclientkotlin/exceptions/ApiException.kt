package com.recombee.apiclientkotlin.exceptions

/**
 * Base class for exceptions that occur because of errors in requests reported by API or because of a timeout
 */
public open class ApiException(message: String) : Exception(message)