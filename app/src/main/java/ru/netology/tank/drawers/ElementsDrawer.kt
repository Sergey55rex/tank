package ru.netology.tank.drawers

import android.os.Build
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

import ru.netology.tank.CELL_SIZE
import ru.netology.tank.HORISONTAL_MAX_SIZE
import ru.netology.tank.R
import ru.netology.tank.VERTICAL_MAX_SIZE
import ru.netology.tank.enums.Direction
import ru.netology.tank.enums.Material
import ru.netology.tank.models.Coordinate
import ru.netology.tank.models.Element
import ru.netology.tank.utils.getElementByCoordinates

class ElementsDrawer(val container: FrameLayout) {

    var currentMaterial = Material.EMPTY
     val elementOnContainer = mutableListOf<Element>()


    fun onTouchContainer(x: Float, y: Float){
        val topMargin = y.toInt() - (y.toInt() % CELL_SIZE)
        val leftMargin = x.toInt() - (x.toInt() % CELL_SIZE)
        val coordinate = Coordinate(topMargin, leftMargin)
        if (currentMaterial==Material.EMPTY) {
            eraseView(coordinate)
        }else{
            drawOrReplaceView(coordinate)
        }
    }

    private fun drawOrReplaceView(coordinate: Coordinate){
        val viewOnCoordinate = getElementByCoordinates(coordinate, elementOnContainer)
        if (viewOnCoordinate==null){
            drawView(coordinate)
            return
        }
        if (viewOnCoordinate.material != currentMaterial){
            replaceView(coordinate)
        }

    }

    private fun replaceView(coordinate: Coordinate){
        eraseView(coordinate)
        drawView(coordinate)
    }

//   private fun getElementByCoordinates(coordinate: Coordinate) =
//            elementOnContainer.firstOrNull { it.coordinate == coordinate }

    private fun eraseView(coordinate: Coordinate){
        val elementOnCoordinate = getElementByCoordinates(coordinate, elementOnContainer)
        if (elementOnCoordinate != null){
            val erasingView = container.findViewById<View>(elementOnCoordinate.viewId)
            container.removeView(erasingView)
            elementOnContainer.remove(elementOnCoordinate)
        }

    }


    internal fun drawView(coordinate: Coordinate){
        val view = ImageView(container.context)
        val layoutParams = FrameLayout.LayoutParams(CELL_SIZE, CELL_SIZE)
        when(currentMaterial){
            Material.EMPTY -> {

            }
            Material.BRICK -> view.setImageResource(R.drawable.brick)
            Material.CONCRETE -> view.setImageResource(R.drawable.concrete)
            Material.GRASS -> view.setImageResource(R.drawable.grass)
        }
        layoutParams.topMargin = coordinate.top
        layoutParams.leftMargin = coordinate.left
//        val viewId = View.generateViewId()

        val viewId = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            View.generateViewId()
        } else {
            TODO("VERSION.SDK_INT < JELLY_BEAN_MR1")
        }

        view.id = viewId
        view.layoutParams = layoutParams
        container.addView(view)
        elementOnContainer.add(Element(viewId,currentMaterial, coordinate))
    }



}