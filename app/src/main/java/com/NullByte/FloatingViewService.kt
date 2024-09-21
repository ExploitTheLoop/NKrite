package com.NullByte

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.ipc.RootService

class FloatingViewService : Service(), View.OnClickListener , Handler.Callback {

    private lateinit var mWindowManager: WindowManager
    private lateinit var mFloatingView: View
    private lateinit var espView: View
    private lateinit var logoView: View
    private val serviceMessenger = Messenger(Handler(Looper.getMainLooper(),this))
    private var remoteMessenger: Messenger? = null
    private var serviceTestQueued = false
    private var conn: MSGConnection? = null

    // Declare the intent as a member variable
    private lateinit var rootServiceIntent: Intent

    override fun onBind(intent: Intent?): IBinder? {
        return serviceMessenger.binder
    }

    override fun onCreate() {
        super.onCreate()
        createOver()

        logoView = mFloatingView.findViewById(R.id.relativeLayoutParent)
        espView = mFloatingView.findViewById(R.id.espView)
        val textView4 = mFloatingView.findViewById<TextView>(R.id.textView4)
        textView4.text = "ATHER-X"

       // appendConsoleText("Console initialized", Color.GREEN)

        init()
    }

    private fun createOver() {
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.floatview, null)

        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.RGBA_8888
        )

        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mWindowManager.addView(mFloatingView, params)

        val gestureDetector = GestureDetector(this, SingleTapConfirm())

        val closeBtn = mFloatingView.findViewById<ImageView>(R.id.closeBtn)
        closeBtn.setOnClickListener {
            espView.visibility = View.GONE
            logoView.visibility = View.VISIBLE
        }

        val player = mFloatingView.findViewById<LinearLayout>(R.id.Player)
        val item = mFloatingView.findViewById<LinearLayout>(R.id.Item)
        val vehicle = mFloatingView.findViewById<LinearLayout>(R.id.Vehicle)
        val aimbot = mFloatingView.findViewById<LinearLayout>(R.id.Aimbot)
        val sdk = mFloatingView.findViewById<LinearLayout>(R.id.Sdk)
        val setting = mFloatingView.findViewById<LinearLayout>(R.id.Setting)

        val menu1 = mFloatingView.findViewById<Button>(R.id.menu_1)
        val menu2 = mFloatingView.findViewById<Button>(R.id.menu_2)

        menu1.setOnClickListener {
            menu1.setBackgroundResource(R.drawable.radius6)
            menu2.setBackgroundResource(R.drawable.shoot)

            player.visibility = View.VISIBLE
            item.visibility = View.GONE
            vehicle.visibility = View.GONE
            aimbot.visibility = View.GONE
            sdk.visibility = View.GONE
            setting.visibility = View.GONE
        }

        menu2.setOnClickListener {
            menu1.setBackgroundResource(R.drawable.shoot)
            menu2.setBackgroundResource(R.drawable.radius6)

            player.visibility = View.GONE
            item.visibility = View.VISIBLE
            vehicle.visibility = View.GONE
            aimbot.visibility = View.GONE
            sdk.visibility = View.GONE
            setting.visibility = View.GONE
        }

        mFloatingView.findViewById<RelativeLayout>(R.id.relativeLayoutParent).setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (gestureDetector.onTouchEvent(event)) {
                    espView.visibility = View.VISIBLE
                    logoView.visibility = View.GONE
                    return true
                }
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = (mFloatingView.layoutParams as WindowManager.LayoutParams).x
                        initialY = (mFloatingView.layoutParams as WindowManager.LayoutParams).y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val updatedParams = mFloatingView.layoutParams as WindowManager.LayoutParams
                        updatedParams.x = initialX + (event.rawX - initialTouchX).toInt()
                        updatedParams.y = initialY + (event.rawY - initialTouchY).toInt()
                        mWindowManager.updateViewLayout(mFloatingView, updatedParams)
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun init() {

        val cacheShell = Shell.getCachedShell()
        cacheShell?.let {
            if (it.isRoot) {
                if (remoteMessenger == null) {
                    serviceTestQueued = true
                    // Initialize the intent here
                    rootServiceIntent = Intent(this@FloatingViewService, RootServices::class.java)
                    conn = MSGConnection()
                    RootService.bind(rootServiceIntent, conn!!)
                }
            }
        }

        val wallhack = mFloatingView.findViewById<Switch>(R.id.wallhack)
        wallhack.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                cacheShell?.let {
                    if (it.isRoot) {

                        ChangeMem("libil2cpp.so","0x3D48410","00 00 7a 7a","com.dts.freefireth")
                    } else {
                        appendConsoleText(
                            Tools.setCode(
                                "com.dts.freefireth",
                                "libil2cpp.so",
                                0x3D48410,
                                "00 00 7a 44"
                            ),Color.RED)
                    }
                }

            } else {
                cacheShell?.let {
                    if (it.isRoot) {
                        ChangeMem("libil2cpp.so","0x3D48410","00 00 7a 44","com.dts.freefireth")
                    } else {
                        appendConsoleText(
                            Tools.setCode(
                                "com.dts.freefireth",
                                "libil2cpp.so",
                                0x3D48410,
                                "00 00 7a 44"
                            ),Color.RED)
                    }
                }
            }
        }

        val ResetGuest = mFloatingView.findViewById<Switch>(R.id.GuestReset)
        ResetGuest.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                cacheShell?.let {
                    if (it.isRoot) {
                        ChangeMem("libil2cpp.so","0x15cfb3c","01 00 A0 E3 1E FF 2F E1","com.dts.freefireth")
                    } else {
                        appendConsoleText(
                            Tools.setCode(
                                "com.dts.freefireth",
                                "libil2cpp.so",
                                0x15cfb3c,
                                "01 00 A0 E3 1E FF 2F E1"
                            ),Color.RED)
                    }
                }

            } else {
                cacheShell?.let {
                    if (it.isRoot) {
                        ChangeMem("libil2cpp.so","0x15cfb3c","01 00 A0 E3 1E FF 2F E1","com.dts.freefireth")
                    } else {


                        appendConsoleText(
                            Tools.setCode(
                                "com.dts.freefireth",
                                "libil2cpp.so",
                                0x15cfb3c,  // Directly using hex notation
                                "01 00 A0 E3 1E FF 2F E1"
                            )
                            ,Color.RED)
                    }
                }
            }
        }
    }

    fun appendConsoleText(message: String, color: Int) {
        val consoleTextView = mFloatingView.findViewById<TextView>(R.id.console)
       // consoleList.add(message) // Add message to the list
        consoleTextView.text =  message
        //consoleList.joinToString("\n") // Update the TextView with all messages
        consoleTextView.setTextColor(color)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mFloatingView.isInitialized) {
            mWindowManager.removeView(mFloatingView)
        }
        conn?.let {
            RootService.unbind(it) // Unbind the service using the connection object
        }
    }

    override fun onClick(view: View) {
        // Handle click events if needed
    }

    private inner class SingleTapConfirm : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(event: MotionEvent): Boolean {
            return true
        }
    }

    fun ChangeMem(libname: String, offsets: String, replace: String, pkg: String) {
        val message = Message.obtain(null, RootServices.MSG_GETINFO)
        message.data.putString("pkg", pkg)
        message.data.putString("fileSo", libname)
        message.data.putInt("offset", Integer.decode(offsets))
        message.data.putString("hexNumber", replace)

        message.replyTo = serviceMessenger

        try {
            remoteMessenger?.send(message)
        } catch (e: RemoteException) {
            appendConsoleText("Remote error : ${e.message}", Color.RED)
        }
    }


    private inner class MSGConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            appendConsoleText("service: rootService connected", Color.GREEN)
            remoteMessenger = Messenger(service)
            if (serviceTestQueued) {
                serviceTestQueued = false
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            appendConsoleText("service: rootService disconnected", Color.RED)
            remoteMessenger = null
        }
    }

    override fun handleMessage(msg: Message): Boolean {

        val result = msg.data.getString("result")
        val dump = "Status : $result"

        if (result == "SUCCESS") {
            appendConsoleText(dump, Color.GREEN)
        } else {
            appendConsoleText(dump, Color.RED)
        }


        return false
    }
}
