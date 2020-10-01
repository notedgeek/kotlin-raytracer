package com.notedgeek.rtace

import kotlin.math.pow

fun lighting(material: Material, light: PointLight, point: Point, eyeV: Vector, normal: Vector): Colour {
    val effectiveColour = material.colour * light.intensity
    val lightV = normalise(light.position - point)
    val ambient = effectiveColour * material.ambient
    val lightDotNormal = lightV dot normal
    val diffuse: Colour
    val specular: Colour
    if (lightDotNormal < 0.0) {
        diffuse = BLACK
        specular = BLACK
    } else {
        diffuse = effectiveColour * material.diffuse * lightDotNormal
        val reflectV = reflect(-lightV, normal)
        val reflectDotEye = reflectV dot eyeV
        specular = if (reflectDotEye <= 0.0) {
            BLACK
        } else {
            val factor = reflectDotEye.pow(material.shininess)
            light.intensity * material.specular * factor
        }
    }
    return ambient + diffuse + specular
}