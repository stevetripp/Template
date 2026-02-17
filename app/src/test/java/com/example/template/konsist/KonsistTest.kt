package com.example.template.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.print
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test

class KonsistTest {

    private val konsistScope = Konsist
        .scopeFromProject()
        .classes()
        .withNameEndingWith("UseCase")

    @Test
    fun `every use case class is public`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("UseCase")
            .print()
            .assertTrue { it.hasPublicOrDefaultModifier }
    }

    @Test
    fun `every use case has operator method named invoke`() {
        konsistScope
            .assertTrue { it.hasFunction { function -> function.name == "invoke" && function.hasOperatorModifier } }
    }

    @Test
    fun `every use case has one public invoke method`() {
        konsistScope
            .assertTrue {
                it.countFunctions { function -> function.hasPublicOrDefaultModifier } == 1
            }
    }
}
