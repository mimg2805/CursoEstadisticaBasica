package com.marcosmiranda.cursoestadisticabasica

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MasApps : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mas_apps)

        val seniasnicasLayout: ConstraintLayout = findViewById(R.id.seniasnicasLayout)
        seniasnicasLayout.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.marcosmiranda.seniasnicas")))
        }

        val nicaroadrageLayout: ConstraintLayout = findViewById(R.id.nicaroadrageLayout)
        nicaroadrageLayout.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.marcosmiranda.nicaroadrage")))
        }

        val purisimaLayout: ConstraintLayout = findViewById(R.id.purisimaLayout)
        purisimaLayout.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.marcosmiranda.purisima")))
        }
    }

}