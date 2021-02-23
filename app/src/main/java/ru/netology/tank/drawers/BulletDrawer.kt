package ru.netology.tank.drawers

import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import ru.netology.tank.CELL_SIZE
import ru.netology.tank.R
import ru.netology.tank.enums.Direction
import ru.netology.tank.models.Coordinate
import ru.netology.tank.utils.checkViewCanMoveThrougBorder

private const val BULLET_WIDTH = 15
private const val BULLETE_HEIGHT = 25

class BulletDrawer(val container: FrameLayout) {

    fun makeBulletMove(myTank: View, currentDirection: Direction){
        Thread(Runnable {
            val bullet = createBullet(myTank, currentDirection)
            while (bullet.checkViewCanMoveThrougBorder(Coordinate(bullet.top, bullet.left))){
                when(currentDirection){
                    Direction.UP -> (bullet.layoutParams as FrameLayout.LayoutParams).topMargin -= BULLETE_HEIGHT
                    Direction.DOWN -> (bullet.layoutParams as FrameLayout.LayoutParams).topMargin += BULLETE_HEIGHT
                    Direction.LEFT -> (bullet.layoutParams as FrameLayout.LayoutParams).leftMargin -= BULLETE_HEIGHT
                    Direction.RIGHT -> (bullet.layoutParams as FrameLayout.LayoutParams).leftMargin += BULLETE_HEIGHT
                }
                Thread.sleep(30)
                (container.context as Activity).runOnUiThread {
                    container.removeView(bullet)
                    container.addView(bullet)
                }
            }
            (container.context as Activity).runOnUiThread {
                container.removeView(bullet)
        }
        }).start()
    }

    private fun createBullet(myTank: View, currentDirection: Direction): ImageView{
//      val bullet = ImageView(container.context)
        return ImageView(container.context)
              .apply {
                  this.setImageResource(R.drawable.bullet)
                  this.layoutParams = FrameLayout.LayoutParams(BULLET_WIDTH, BULLETE_HEIGHT)
                  val bulletCoordinate = getBulletCoordinates(this,myTank, currentDirection)
                  (this.layoutParams as FrameLayout.LayoutParams).topMargin = bulletCoordinate.top
                  (this.layoutParams as FrameLayout.LayoutParams).leftMargin = bulletCoordinate.left
                  this.rotation = currentDirection.rotation
//                  container.addView(this)
              }

    }

    private fun getBulletCoordinates(
            bullet: ImageView,
            myTank: View,
            currentDirection: Direction
    ): Coordinate{
        val tankLeftTopCoordinate = Coordinate(myTank.top, myTank.left)
        return when(currentDirection){
            Direction.UP ->
                Coordinate(
                        top = tankLeftTopCoordinate.top - bullet.layoutParams.height,
                        left = getDistanceToMiddleOfTank(tankLeftTopCoordinate.left, bullet.layoutParams.width))
            Direction.DOWN ->
                Coordinate(
                        top = tankLeftTopCoordinate.top + myTank.layoutParams.height,
                        left = getDistanceToMiddleOfTank(tankLeftTopCoordinate.left, bullet.layoutParams.width))
            Direction.LEFT ->
                Coordinate(
                        top = getDistanceToMiddleOfTank(tankLeftTopCoordinate.top,bullet.layoutParams.height),
                        left =  tankLeftTopCoordinate.left - bullet.layoutParams.width)

            Direction.RIGHT ->
                 Coordinate(
                        top = getDistanceToMiddleOfTank(tankLeftTopCoordinate.top,bullet.layoutParams.height),
                        left =  tankLeftTopCoordinate.left + myTank.layoutParams.width)
        }
    }

    private fun getDistanceToMiddleOfTank(startCoordinate: Int, bulletSize: Int): Int{
        return startCoordinate + (CELL_SIZE - bulletSize/2)
    }
}