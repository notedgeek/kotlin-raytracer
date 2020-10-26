package com.notedgeek.rtrace.obj

import com.notedgeek.rtrace.maths.EPSILON
import com.notedgeek.rtrace.maths.Point
import com.notedgeek.rtrace.maths.Vector
import com.notedgeek.rtrace.rotationX
import com.notedgeek.rtrace.rotationY
import com.notedgeek.rtrace.rotationZ
import java.io.File
import java.util.regex.Pattern

fun objectFileGroup(name: String, smoothIfPossible: Boolean = true, bound: Boolean = true, split: Boolean = true): Group {

    val lines = File("src/main/resources/$name.obj").readLines()

    val vertices = ArrayList<Point>()
    val triangles = ArrayList<Triangle>()
    val normals = ArrayList<Vector>()

    val pattern = Pattern.compile("\\s+")

    var xMin = Double.POSITIVE_INFINITY
    var xMax = Double.NEGATIVE_INFINITY
    var yMin = Double.POSITIVE_INFINITY
    var yMax = Double.NEGATIVE_INFINITY
    var zMin = Double.POSITIVE_INFINITY
    var zMax = Double.NEGATIVE_INFINITY

    for(line in lines) {
        val fields = line.trim().split(pattern)
        if(fields[0] == "v") {
            val x = fields[1].toDouble()
            val y = fields[2].toDouble()
            val z = fields[3].toDouble()
            if(x < xMin) {
                xMin = x
            }
            if(x > xMax) {
                xMax = x
            }
            if(y < yMin) {
                yMin = y
            }
            if(y > yMax) {
                yMax = y
            }
            if(z < zMin) {
                zMin = z
            }
            if(z > zMax) {
                zMax = z
            }
            vertices.add(Point(x, y, z))
        }
        if(fields[0] == "vn" && smoothIfPossible) {
            normals.add(Vector(fields[1].toDouble(), fields[2].toDouble(), fields[3].toDouble()))
        }
        if(fields[0] == "f") {
            val ai = 1
            var bi = 2
            var ci = 3
            for(i in 1..(fields.size - 3)) {
                val d1 = fields[ai].split("/")
                val d2 = fields[bi].split("/")
                val d3 = fields[ci].split("/")
                val v1 = vertices[d1[0].toInt() - 1]
                val v2 = vertices[d2[0].toInt() - 1]
                val v3 = vertices[d3[0].toInt() - 1]
                if(smoothIfPossible && d1.size > 2) {
                    val n1 = normals[d1[2].toInt() - 1]
                    val n2 = normals[d2[2].toInt() - 1]
                    val n3 = normals[d3[2].toInt() - 1]
                    triangles.add(SmoothTriangle(v1, v2, v3, n1, n2, n3))
                } else {
                    triangles.add(Triangle(v1, v2, v3))
                }
                bi ++
                ci ++
            }
        }
    }

    println("vertices: ${vertices.size} normals: ${normals.size} triangles: ${triangles.size}")
    println("xMin: $xMin, xMax: $xMax, yMin: $yMin, yMax: $yMax, zMin: $zMin, zMax: $zMax")

//    val boundingBox = if(bound) BoundingBox(Point(xMin, yMin, zMin), Point(xMax, yMax, zMax)) else null

    var group =  Group(children = triangles)

    if(bound && split) {
        // fudge to stop artifacts when edge between faces is perpendicular to axis - TODO - find a better fix
        group = group.split(5).withTransform(rotationX(EPSILON) * rotationY(EPSILON) * rotationZ(EPSILON))
    }

    return group
}
