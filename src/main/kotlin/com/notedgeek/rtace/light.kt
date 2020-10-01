package com.notedgeek.rtace

class PointLight(var position: Point, var intensity: Colour)

fun pointLight(position: Point, intensity: Colour) = PointLight(position, intensity)