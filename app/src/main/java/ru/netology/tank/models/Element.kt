package ru.netology.tank.models

import ru.netology.tank.enums.Material

data class Element(
        val viewId: Int,
        val material: Material,
        val coordinate: Coordinate
) {
}