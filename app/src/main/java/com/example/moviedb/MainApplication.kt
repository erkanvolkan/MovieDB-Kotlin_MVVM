package com.example.moviedb

import android.app.Application
import android.content.Intent
import android.os.Looper
import android.widget.Toast
import androidx.multidex.MultiDex
import com.example.moviedb.di.appModules
import com.example.moviedb.ui.screen.MainActivity
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import kotlin.system.exitProcess

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MultiDex.install(this)
        /**
         *
        A pragmatic lightweight dependency injection framework for Kotlin developers.

        Written in pure Kotlin, using functional resolution only: no proxy, no code generation, no reflection.

        Koin is a DSL, a light container and a pragmatic API
         */
        startKoin {
            androidContext(this@MainApplication)
            modules(appModules)
        }
        /**
         * Timber comes with half a dozen lint checks that help you spot incorrect usage of the log calls.
         */
        if (BuildConfig.DEBUG) {
            // init timber
            Timber.plant(Timber.DebugTree())

            // init stetho
            Stetho.initializeWithDefaults(this)
        }

        handleUncaughtException()
    }

    /**
     * prevent uncaught exception to crash app
     * restart app for better UX
     */
    fun handleUncaughtException() {
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            object : Thread() {
                override fun run() {
                    Looper.prepare()
                    Toast.makeText(applicationContext, R.string.unknown_error, Toast.LENGTH_SHORT)
                        .show()
                    Looper.loop()
                }
            }.start()

            Thread.sleep(2000)

            val intent = Intent(this, MainActivity::class.java)
            // to custom behaviour, add extra params for intent
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        or Intent.FLAG_ACTIVITY_NEW_TASK
            )
            startActivity(intent)
            try {
                exitProcess(2);
            } catch (e: Exception) {
                startActivity(intent)
            }
        }
    }
}