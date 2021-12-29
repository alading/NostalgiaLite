package nostalgia.appnes

import nostalgia.framework.base.EmulatorActivity
import android.os.Bundle
import nostalgia.framework.Emulator
import nostalgia.appnes.NesEmulator
import nostalgia.framework.ui.preferences.PreferenceUtil
import android.content.Intent
import nostalgia.appnes.NesGalleryActivity
import android.app.ActivityManager
import android.app.ActivityManager.RunningTaskInfo
import android.util.Log
import android.view.KeyEvent
import com.appblend.handfree.yaw.YawMovement
import com.tcl.tv.ideo.yaw.YawUDPClient
import kotlinx.coroutines.*

class NesEmulatorActivity : EmulatorActivity() {
    private var isLastOfStack = false
    private var coroutineJob: Job? = null
    val coroutineExceptionHandler = CoroutineExceptionHandler{ _, t ->
        run {
            t.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLastOfStack = checkLastStack()
    }

    var shader1 = ("precision mediump float;"
            + "varying vec2 v_texCoord;"
            + "uniform sampler2D s_texture;"
            + "uniform sampler2D s_palette; "
            + "void main()"
            + "{           "
            + "		 float a = texture2D(s_texture, v_texCoord).a;"
            + "	     float c = floor((a * 256.0) / 127.5);"
            + "      float x = a - c * 0.001953;"
            + "      vec2 curPt = vec2(x, 0);"
            + "      gl_FragColor.rgb = texture2D(s_palette, curPt).rgb;"
            + "}")
    var shader2 = ("precision mediump float;"
            + "varying vec2 v_texCoord;"
            + "uniform sampler2D s_texture;"
            + "uniform sampler2D s_palette; "
            + "void main()"
            + "{"
            + "		 float a = texture2D(s_texture, v_texCoord).a;"
            + "		 float x = a;	"
            + "		 vec2 curPt = vec2(x, 0);"
            + "      gl_FragColor.rgb = texture2D(s_palette, curPt).rgb;"
            + "}")

    override fun getEmulatorInstance(): Emulator {
        return NesEmulator.getInstance()
    }

    override fun getFragmentShader(): String {
        val shaderIdx = PreferenceUtil.getFragmentShader(this)
        return if (shaderIdx == 1) {
            shader2
        } else shader1
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isLastOfStack) {
            val intent = Intent(this, NesGalleryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkLastStack(): Boolean {
        val mngr = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val taskList = mngr.getRunningTasks(10)
        return taskList[0].numActivities == 1 && taskList[0].topActivity!!.className == this.javaClass.name
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                coroutineJob = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {

                    YawUDPClient.getInstance()?.sendMessage(YawMovement.ROLLLEFT5.move!!)
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, YawMovement.ROLLLEFT5.name)
                    }

                }
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                coroutineJob = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {

                    YawUDPClient.getInstance()?.sendMessage(YawMovement.ROLLRIGHT5.move!!)
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, YawMovement.ROLLRIGHT5.name)
                    }

                }
            }
            KeyEvent.KEYCODE_BUTTON_A -> {
            }
            KeyEvent.KEYCODE_BUTTON_B -> {
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
//        when (keyCode) {
//            KeyEvent.KEYCODE_DPAD_LEFT -> {
//            }
//            KeyEvent.KEYCODE_DPAD_RIGHT -> {
//            }
//            KeyEvent.KEYCODE_BUTTON_A -> {
//            }
//            KeyEvent.KEYCODE_BUTTON_B -> {
//            }
//        }
        coroutineJob = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {

            YawUDPClient.getInstance()?.sendMessage(YawMovement.IDLE.move!!)
            withContext(Dispatchers.Main) {
                Log.d(TAG, YawMovement.IDLE.name)
            }

        }
        return super.onKeyUp(keyCode, event)
    }

    companion object {
        public const val TAG = "NesEmuActivity"
    }
}