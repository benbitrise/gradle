/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.com.h0tk3y.kotlin.staticObjectNotation.analysis

import com.h0tk3y.kotlin.staticObjectNotation.Adding
import com.h0tk3y.kotlin.staticObjectNotation.analysis.ErrorReason
import com.h0tk3y.kotlin.staticObjectNotation.analysis.ResolutionResult
import com.h0tk3y.kotlin.staticObjectNotation.demo.resolve
import com.h0tk3y.kotlin.staticObjectNotation.schemaBuilder.schemaFromTypes
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertTrue

object LambdaTest {
    @Test
    fun `if a lambda is required, missing lambda is reported as an error`() {
        schema.resolve("lambdaRequired(0)").isError(ErrorReason.MissingConfigureLambda::class)
    }

    @Test
    fun `if a lambda is required, a lambda is accepted`() {
        schema.resolve("lambdaRequired(0) { }").isSuccessful()
    }

    @Test
    fun `if a lambda is optional, a missing lambda is ok`() {
        schema.resolve("lambdaOptional(0)").isSuccessful()
    }

    @Test
    fun `if a lambda is optional, a lambda is accepted`() {
        schema.resolve("lambdaOptional(0) { }").isSuccessful()
    }

    @Test
    fun `if a lambda is not allowed, missing lambda is ok`() {
        schema.resolve("lambdaNotAllowed(0)").isSuccessful()
    }

    @Test
    fun `if a lambda is not allowed, a lambda is reported as an error`() {
        schema.resolve("lambdaNotAllowed(0) { }").isError(ErrorReason.UnusedConfigureLambda::class)
    }

    private fun ResolutionResult.isSuccessful() {
        assertTrue { errors.isEmpty() }
        assertTrue { additions.size == 1 }
    }

    private fun ResolutionResult.isError(errorReason: KClass<out ErrorReason>) {
        assertTrue { errors.any { errorReason.isInstance(it.errorReason) } }
    }

    private val schema = schemaFromTypes(Receiver::class, listOf(Receiver::class))

    @Suppress("unused")
    private abstract class Receiver {
        @Adding
        abstract fun lambdaRequired(x: Int, configure: Receiver.() -> Unit): Receiver

        @Adding
        abstract fun lambdaOptional(x: Int, configure: Receiver.() -> Unit = { }): Receiver

        @Adding
        abstract fun lambdaNotAllowed(x: Int): Receiver
    }
}
