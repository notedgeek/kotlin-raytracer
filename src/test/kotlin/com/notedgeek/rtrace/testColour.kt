package com.notedgeek.rtrace

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TestColour {

    @Test
    fun `Colours are (red, green, blue) tuples`() {
        val c = Colour(-0.5, 0.4, 1.7)
        assertThat(c.red).isEqualTo(-0.5)
        assertThat(c.green).isEqualTo(0.4)
        assertThat(c.blue).isEqualTo(1.7)
    }

    @Test
    fun `adding Colours`() {
        val c1 = Colour(0.9, 0.6, 0.75)
        val c2 = Colour(0.7, 0.1, 0.25)
        assertThat(c1 + c2).isEqualTo(Colour(1.6, 0.7, 1.0))
    }

    @Test
    fun `subtracting Colours`() {
        val c1 = Colour(0.9, 0.6, 0.75)
        val c2 = Colour(0.7, 0.1, 0.25)
        assertThat(c1 - c2).isEqualTo(Colour(0.2, 0.5, 0.5))
    }

    @Test
    fun `multiplying a Colour by a scalar`() {
        val c = Colour(0.2, 0.3, 0.4)
        assertThat(c * 2.0).isEqualTo(Colour(0.4, 0.6, 0.8))
    }

    @Test
    fun `multiplying two Colours`() {
        val c1 = Colour(1.0, 0.2, 0.4)
        val c2 = Colour(0.9, 1.0, 0.1)
        assertThat(c1 * c2).isEqualTo(Colour(0.9, 0.2, 0.04))
    }
}