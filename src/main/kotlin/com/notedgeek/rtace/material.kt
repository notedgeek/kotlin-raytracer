package com.notedgeek.rtace

class Material(
    val colour: Colour = Colour(1.0, 1.0, 1.0),
    val ambient: Double = 0.1,
    val diffuse: Double = 0.9,
    val specular: Double = 0.9,
    val shininess: Double = 200.0
) {
    override fun equals(other: Any?) = other is Material && colour == other.colour && ambient == other.ambient &&
            diffuse == other.diffuse && specular == other.specular && shininess == other.shininess

    override fun toString(): String {
        return "Material(colour=$colour, ambient=$ambient, diffuse=$diffuse, specular=$specular, shininess=$shininess)"
    }
}
