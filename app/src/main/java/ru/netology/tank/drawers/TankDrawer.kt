package ru.netology.tank.drawers

import android.view.View
import android.widget.FrameLayout
import ru.netology.tank.CELL_SIZE
import ru.netology.tank.HORISONTAL_MAX_SIZE
import ru.netology.tank.VERTICAL_MAX_SIZE
import ru.netology.tank.enums.Direction
import ru.netology.tank.models.Coordinate
import ru.netology.tank.models.Element

class TankDrawer(val container: FrameLayout) {

    var currentDirection = Direction.DOWN
    fun move(myTank: View, direction: Direction, elementOnContainer:List<Element>) {
        val layoutParams = myTank.layoutParams as FrameLayout.LayoutParams
        val currentCoordinate = Coordinate(layoutParams.topMargin, layoutParams.leftMargin) //save before change
        currentDirection = direction
        myTank.rotation = direction.rotation

        when (direction) {
            Direction.UP -> {
//                myTank.rotation = 0f
                (myTank.layoutParams as FrameLayout.LayoutParams).topMargin += -CELL_SIZE
            }
            Direction.DOWN -> {
//                myTank.rotation = 180f
                (myTank.layoutParams as FrameLayout.LayoutParams).topMargin += CELL_SIZE
            }
            Direction.RIGHT -> {
//                myTank.rotation = 90f
                (myTank.layoutParams as FrameLayout.LayoutParams).leftMargin += CELL_SIZE
            }
            Direction.LEFT -> {
//                myTank.rotation = 270f
                (myTank.layoutParams as FrameLayout.LayoutParams).leftMargin += -CELL_SIZE
            }
        }
        val nextCoordinate = Coordinate(layoutParams.topMargin, layoutParams.leftMargin)  //save after change
        if (checkTankCanMoveThrougBorder(nextCoordinate, myTank)
                && checkTankCanMoveThrougMaterial(nextCoordinate, elementOnContainer)
        ) {
            container.removeView(myTank)
            container.addView(myTank, 0)
        }else{
            (myTank.layoutParams as FrameLayout.LayoutParams).topMargin = currentCoordinate.top
            (myTank.layoutParams as FrameLayout.LayoutParams).leftMargin = currentCoordinate.left
        }

    }

    private fun checkTankCanMoveThrougMaterial(coordinate: Coordinate, elementOnContainer:List<Element>): Boolean{
        getTankCoordinates(coordinate).forEach {
            val element = getElementByCoordinates(it, elementOnContainer)
            if (element != null && !element.material.tankCanGoThrough){
                return false
            }
        }
        return true
    }

    private fun getElementByCoordinates(coordinate: Coordinate, elementOnContainer:List<Element>) =
            elementOnContainer.firstOrNull { it.coordinate == coordinate }

    private fun checkTankCanMoveThrougBorder(coordinate: Coordinate, myTank: View): Boolean{
        if (coordinate.top >= 0
                && coordinate.top + myTank.height < HORISONTAL_MAX_SIZE
                && coordinate.left >= 0
                && coordinate.left + myTank.width < VERTICAL_MAX_SIZE
        ){
            return true
        }
        return false
    }

    private fun getTankCoordinates(topLeftCoordinate: Coordinate): List<Coordinate>{
        val coordinateList = mutableListOf<Coordinate>()
        coordinateList.add(topLeftCoordinate)
        coordinateList.add(Coordinate(topLeftCoordinate.top + CELL_SIZE, topLeftCoordinate.left )) //Botton left
        coordinateList.add(Coordinate(topLeftCoordinate.top, topLeftCoordinate.left + CELL_SIZE)) //top right
        coordinateList.add(Coordinate(topLeftCoordinate.top + CELL_SIZE, topLeftCoordinate.left + CELL_SIZE))  //botton right
        return coordinateList
    }
}