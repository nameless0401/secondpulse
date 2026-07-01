package com.secondpulse.app.domain

import org.junit.Assert.*
import org.junit.Test

class NarrativeEngineTest {
    private fun content() = StoryContent("t","b1", listOf(
        NarrativeBlock("b1","a","c","s","x","Acte",listOf("p"), Choice("ch","?",options=listOf(
            Option("o1","Visible",eligibility=Condition.Always,effects=listOf(Effect.SetVariable("k",Value.Text("v"))),nextBlockId="b2"),
            Option("secret","Secret",eligibility=Condition.VarEquals("never",Value.Bool(true)),nextBlockId="b2")
        ))), NarrativeBlock("b2","a","c","s","y","Acte",listOf("q"))
    ))

    @Test fun hidden_options_are_not_returned() {
        val e = NarrativeEngine(content())
        val c = content().blocks.first().choice!!
        assertEquals(listOf("o1"), e.visibleOptions(c, GameState(reading=ReadingPosition(blockId="b1"))).map { it.id })
    }

    @Test fun resolution_is_single_and_writes_state() {
        val e = NarrativeEngine(content())
        val initial = GameState(reading=ReadingPosition(blockId="b1"))
        val resolved = e.resolve(initial,"ch","o1").state
        assertEquals(Value.Text("v"), resolved.variables["k"])
        assertEquals("b2", resolved.reading.blockId)
        var rejected = false
        try { e.resolve(resolved,"ch","o1") } catch (_: NarrativeException) { rejected = true }
        assertTrue(rejected)
    }
}
