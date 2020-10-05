package com.notedgeek.rtrace

import com.notedgeek.rtace.Colour
import com.notedgeek.rtace.Material
import com.notedgeek.rtace.`object`.Sphere
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TestMaterial {

    @Test
    fun `default material`() {
        val m = Material()
        assertThat(m.colour).isEqualTo(Colour(1.0, 1.0, 1.0))
        assertThat(m.ambient).isEqualTo(0.1)
        assertThat(m.diffuse).isEqualTo(0.9)
        assertThat(m.specular).isEqualTo(0.9)
        assertThat(m.shininess).isEqualTo(200.0)
    }

    @Test
    fun `Sphere has default material`() {
        val s = Sphere()
        assertThat(s.material).isEqualTo(Material())
    }

    @Test
    fun `a Sphere may be assigned a material`() {
        val m = Material(ambient = 1.0)
        val s = Sphere().withMaterial(m)
        assertThat(s.material).isEqualTo(m)
    }
}