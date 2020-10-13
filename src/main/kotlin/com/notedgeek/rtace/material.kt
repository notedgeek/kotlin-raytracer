package com.notedgeek.rtace

import com.notedgeek.rtace.pattern.BlankPattern
import com.notedgeek.rtace.pattern.Pattern

class Material(
    val colour: Colour = Colour(1.0, 1.0, 1.0),
    val pattern: Pattern = BlankPattern(colour),
    val ambient: Double = 0.1,
    val diffuse: Double = 0.9,
    val specular: Double = 0.9,
    val shininess: Double = 200.0,
    val reflective: Double = 0.0
) {

    fun withColour(colour: Colour) = copy(colour = colour).withPattern(BlankPattern(colour))

    fun withPattern(pattern: Pattern) = copy(pattern = pattern)

    fun withDiffuse(diffuse: Double) = copy(diffuse = diffuse)

    fun withSpecular(specular: Double) = copy(specular = specular)

    fun withShininess(shininess: Double) = copy(shininess = shininess)

    fun withReflective(reflective: Double) = copy(reflective = reflective)

    private fun copy(colour: Colour = this.colour, pattern: Pattern = this.pattern, ambient: Double = this.ambient,
             diffuse: Double = this.diffuse, specular: Double = this.specular, shininess: Double = this.shininess,
             reflective: Double = this.reflective) =
        Material(colour, pattern, ambient, diffuse, specular, shininess, reflective)

    override fun equals(other: Any?) = other is Material && colour == other.colour && ambient == other.ambient &&
            diffuse == other.diffuse && specular == other.specular && shininess == other.shininess

    override fun toString(): String {
        return "Material(colour=$colour, ambient=$ambient, diffuse=$diffuse," +
                "specular=$specular, shininess=$shininess)"
    }
}
