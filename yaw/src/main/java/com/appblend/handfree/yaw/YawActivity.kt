package com.appblend.handfree.yaw

import android.Manifest
import android.app.Activity
import android.app.Fragment
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.tcl.tv.ideo.player.ui.yawvr.YAWConnectFragment
import java.util.*


/**
 * Activity showcasing the use of [androidx.leanback.widget.PageRow] and
 * [androidx.leanback.widget.ListRow].
 */
class YawActivity : Activity() {

    private var mImageViewQrCode: ImageView? = null

    private lateinit var layerDrawable: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yaw)
        val avatarFragment = YAWConnectFragment()
        addFragment(avatarFragment)

        val metrics: DisplayMetrics = resources.displayMetrics
        Log.e(TAG, "width = " + metrics.widthPixels+", height = "+ metrics.heightPixels)
        Log.e(TAG, "density = " + metrics.density)
        Log.e(TAG, "densityDPI = " + metrics.densityDpi)


    }

    override fun onResume() {
        super.onResume()
        window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)



    }

    override fun onPause() {
        super.onPause()
        window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        // Clear the systemUiVisibility flag
        window?.decorView?.systemUiVisibility = 0

    }



    fun addFragment(fragment: Fragment) {
        val fragmentManager: android.app.FragmentManager? = fragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.add(R.id.fragment_container, fragment, "tag")
        fragmentTransaction?.commit()
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager: android.app.FragmentManager? = fragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragment_container, fragment, "tag")
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }



    companion object {
        val TAG = "YawActivity"

    }

}