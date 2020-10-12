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
    fun withColour(colour: Colour) = Material(colour, BlankPattern(colour), ambient, diffuse,
        specular, shininess, reflective)

    fun withPattern(pattern: Pattern) = Material(colour, pattern, ambient, diffuse,
        specular, shininess, reflective)

    fun withDiffuse(diffuse: Double) = Material(colour, pattern, ambient, diffuse,
        specular, shininess, reflective)

    fun withSpecular(specular: Double) = Material(colour, pattern, ambient, diffuse,
        specular, shininess, reflective)

    fun withShininess(shininess: Double) = Material(colour, pattern, ambient, diffuse,
        specular, shininess, reflective)

    fun withReflective(reflective: Double) = Material(colour, pattern, ambient, diffuse,
        specular, shininess, reflective)


    override fun equals(other: Any?) = other is Material && colour == other.colour && ambient == other.ambient &&
            diffuse == other.diffuse && specular == other.specular && shininess == other.shininess

    override fun toString(): String {
        return "Material(colour=$colour, ambient=$ambient, diffuse=$diffuse," +
                "specular=$specular, shininess=$shininess)"
    }
}
