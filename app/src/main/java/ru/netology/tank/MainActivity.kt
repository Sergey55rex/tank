package ru.netology.tank
import android.os.Bundle

import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.netology.tank.drawers.BulletDrawer
import ru.netology.tank.drawers.ElementsDrawer
import ru.netology.tank.drawers.GridDrawer
import ru.netology.tank.drawers.TankDrawer
import ru.netology.tank.enums.Direction

import ru.netology.tank.enums.Direction.*
import ru.netology.tank.enums.Material
import ru.netology.tank.models.Coordinate

const val CELL_SIZE = 37
const val VERTICAL_CELL_AMOUNT = 20
const val HORISONTAL_CELL_AMOUNT = 26
const val VERTICAL_MAX_SIZE = CELL_SIZE * VERTICAL_CELL_AMOUNT
const val HORISONTAL_MAX_SIZE = CELL_SIZE * HORISONTAL_CELL_AMOUNT

class MainActivity : AppCompatActivity() {

    private var editMode = false

    private val gridDrawer by lazy {
        GridDrawer(container)
    }

    private val elementsDrawer by lazy {
        ElementsDrawer(container)
    }

    private val tankDrawer by lazy {
        TankDrawer(container)
    }

    private val bulletDrawer by lazy {
        BulletDrawer(container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        container.layoutParams = FrameLayout.LayoutParams(VERTICAL_MAX_SIZE, HORISONTAL_MAX_SIZE)
        editor_clear.setOnClickListener { elementsDrawer.currentMaterial = Material.EMPTY}
        editor_brick.setOnClickListener { elementsDrawer.currentMaterial = Material.BRICK}
        editor_concrete.setOnClickListener { elementsDrawer.currentMaterial = Material.CONCRETE}
        editor_grass.setOnClickListener { elementsDrawer.currentMaterial = Material.GRASS}
        container.setOnTouchListener {_, event ->
            elementsDrawer.onTouchContainer(event.x, event.y)
            return@setOnTouchListener true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                switchEditMode()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun switchEditMode(){
        if (editMode){
            gridDrawer.remoteGird()
            materials_container.visibility = GONE
        }else{
            gridDrawer.drawGrid()
            materials_container.visibility = VISIBLE
        }
        editMode = !editMode
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KEYCODE_DPAD_UP -> tankDrawer.move(myTank, UP, elementsDrawer.elementOnContainer)
            KEYCODE_DPAD_LEFT -> tankDrawer.move(myTank, LEFT, elementsDrawer.elementOnContainer)
            KEYCODE_DPAD_DOWN -> tankDrawer.move(myTank, DOWN, elementsDrawer.elementOnContainer)
            KEYCODE_DPAD_RIGHT -> tankDrawer.move(myTank, RIGHT, elementsDrawer.elementOnContainer)
            KEYCODE_SPACE -> bulletDrawer.makeBulletMove(myTank, tankDrawer.currentDirection)
        }
        return super.onKeyDown(keyCode, event)
    }


}