package com.example.dochyve

import android.content.Intent
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.dochyve.databinding.ActivitySplashBinding
import com.example.dochyve.ui.signin.SignInActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.appcompat.app.AppCompatDelegate

class SplashActivity : AppCompatActivity() {


    companion object {
        private const val SPLASH_DELAY = 5000L
        private const val ANIMATION_DURATION = 1850L
        private const val ANIMATION_START_DELAY = 100L
    }

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // light theme only, ignores system dark mode setting
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        startSplashFlow()
    }

    private fun startSplashFlow() {

        binding.ivSplash.post {
            animateIconFromBottom()
        }

        lifecycleScope.launch {
            delay(SPLASH_DELAY)
            navigateToMain()
        }
    }

    private fun animateIconFromBottom() {

        val moveDistance = binding.root.height * 0.04f  // slightly below center

        binding.ivSplash.apply {
            translationY = moveDistance

            animate()
                .translationY(0f)
                .setDuration(ANIMATION_DURATION) // slower
                .setInterpolator(DecelerateInterpolator()) // very smooth: starting fast, ending smooth
                .setStartDelay(ANIMATION_START_DELAY)
                .start()
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
}