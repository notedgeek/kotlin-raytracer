package com.notedgeek.rtrace

import com.notedgeek.rtrace.maths.Point
import com.notedgeek.rtrace.maths.Vector
import com.notedgeek.rtrace.maths.normalise
import com.notedgeek.rtrace.obj.SceneObject
import kotlin.math.pow

fun surfaceColour(
        material: Material, light: PointLight, point: Point, eyeV: Vector,
        normal: Vector, obj: SceneObject, inShadow: Boolean = false,
): Colour {
    val effectiveColour = material.pattern.colourAtObject(obj, point) * light.intensity
    val lightV = normalise(light.position - point)
    val ambient = effectiveColour * material.ambient
    val lightDotNormal = lightV dot normal
    val diffuse: Colour
    val specular: Colour
    if (inShadow || lightDotNormal < 0.0) {
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