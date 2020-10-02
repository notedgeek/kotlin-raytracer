package com.notedgeek.rtace

import com.notedgeek.rtace.objects.SceneObject

class World(val light: PointLight, val objects: List<SceneObject>) {

    fun intersections(ray: Ray): List<Intersection> {
        var result = emptyList<Intersection>()
        objects.forEach { result = intersectObjects(it, ray, result) }
        return result
    }

    fun shadeHit(comps: Comps) = lighting(comps.obj.material, light, comps.point, comps.eyeV, comps.normal)

    fun colourAt(ray: Ray): Colour {
        val intersections = intersections(ray)
        val hit = hit(intersections)
        return if (hit == null) {
            BLACK
        } else {
            val comps = comps(hit, ray)
            shadeHit(comps)
        }
    }

    private fun intersectObjects(obj: SceneObject, ray: Ray, currentList: List<Intersection>): List<Intersection> {
        val list = obj.intersects(ray)
        val size = currentList.size + list.size
        var currentListIndex = 0
        var listIndex = 0
        val result = ArrayList<Intersection>(size)
        for(i in 0 until size) {
            if (currentList.isEmpty() || currentListIndex == currentList.size) {
                result.add(list[listIndex])
                listIndex++
            } else if (list.isEmpty() || listIndex == list.size) {
                result.add(currentList[currentListIndex])
                currentListIndex++
            } else if (currentList[currentListIndex].t < list[listIndex].t) {
                result.add(currentList[currentListIndex])
                currentListIndex++
            } else {
                result.add(list[listIndex])
                listIndex++
            }
        }
        return result
    }
}

fun world(light: PointLight, objects: List<SceneObject>) = World(light, objects)
