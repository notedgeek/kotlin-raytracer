package com.notedgeek.rtrace

import com.notedgeek.rtace.colour
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TestColour {

    @Test
    fun `colours are (red, green, blue) tuples`() {
        val c = colour(-0.5, 0.4, 1.7)
        assertThat(c.red).isEqualTo(-0.5)
        assertThat(c.green).isEqualTo(0.4)
        assertThat(c.blue).isEqualTo(1.7)
    }

    @Test
    fun `adding colours`() {
        val c1 = colour(0.9, 0.6, 0.75)
        val c2 = colour(0.7, 0.1, 0.25)
        assertThat(c1 + c2).isEqualTo(colour(1.6, 0.7, 1.0))
    }

    @Test
    fun `subtracting colours`() {
        val c1 = colour(0.9, 0.6, 0.75)
        val c2 = colour(0.7, 0.1, 0.25)
        assertThat(c1 - c2).isEqualTo(colour(0.2, 0.5, 0.5))
    }

    @Test
    fun `multiplying a colour by a scalar`() {
        val c = colour(0.2, 0.3, 0.4)
        assertThat(c * 2.0).isEqualTo(colour(0.4, 0.6, 0.8))
    }

    @Test
    fun `multiplying two colours`() {
        val c1 = colour(1.0, 0.2, 0.4)
        val c2 = colour(0.9, 1.0, 0.1)
        assertThat(c1 * c2).isEqualTo(colour(0.9, 0.2, 0.04))
    }
}