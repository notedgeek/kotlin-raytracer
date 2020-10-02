package com.notedgeek.rtrace

import com.notedgeek.rtace.colour
import com.notedgeek.rtace.material
import com.notedgeek.rtace.objects.sphere
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TestMaterial {

    @Test
    fun `default material`() {
        val m = material()
        assertThat(m.colour).isEqualTo(colour(1.0, 1.0, 1.0))
        assertThat(m.ambient).isEqualTo(0.1)
        assertThat(m.diffuse).isEqualTo(0.9)
        assertThat(m.specular).isEqualTo(0.9)
        assertThat(m.shininess).isEqualTo(200.0)
    }

    @Test
    fun `sphere has default material`() {
        val s = sphere()
        assertThat(s.material).isEqualTo(material())
    }

    @Test
    fun `a sphere may be assigned a material`() {
        val m = material(ambient = 1.0)
        val s = sphere().material(m)
        assertThat(s.material).isEqualTo(m)
    }
}