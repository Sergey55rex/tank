package ru.netology.tank.utils

import android.view.View
import ru.netology.tank.HORISONTAL_MAX_SIZE
import ru.netology.tank.VERTICAL_MAX_SIZE
import ru.netology.tank.models.Coordinate
import ru.netology.tank.models.Element


fun View.checkViewCanMoveThrougBorder(coordinate: Coordinate): Boolean{
    if (coordinate.top >= 0
            && coordinate.top + this.height < HORISONTAL_MAX_SIZE
            && coordinate.left >= 0
            && coordinate.left + this.width < VERTICAL_MAX_SIZE
    ){
        return true
    }
    return false
}

fun getElementByCoordinates(coordinate: Coordinate, elementOnContainer:List<Element>) =
        elementOnContainer.firstOrNull { it.coordinate == coordinate }
