package com.notedgeek.rtace

class Scene(val world: World, val camera: Camera) {

    override fun toString(): String {
        return "Scene(world=$world, camera=$camera)"
    }
}