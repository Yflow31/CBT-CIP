package com.approval.unitconvertor

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.approval.unitconvertor.data.MainPageData
import com.approval.unitconvertor.operations.Area
import com.approval.unitconvertor.operations.LengthPage
import com.approval.unitconvertor.operations.Speed
import com.approval.unitconvertor.operations.TemperaturePage
import com.approval.unitconvertor.operations.Time
import com.approval.unitconvertor.operations.Volume
import com.approval.unitconvertor.operations.Weight

class MainActivity : AppCompatActivity() {

    lateinit var rv_item: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_item = findViewById(R.id.rv_item)

        rv_item.layoutManager = LinearLayoutManager(this)

        val itemList = listOf(
            MainPageData("Length", LengthPage::class.java, R.drawable.length),
            MainPageData("Temperature", TemperaturePage::class.java, R.drawable.temperature),
            MainPageData("Weight", Weight::class.java, R.drawable.weight_scale),
            MainPageData("Volume", Volume::class.java, R.drawable.measure),
            MainPageData("Area", Area::class.java, R.drawable.wide),
            MainPageData("Time", Time::class.java, R.drawable.clock),
            MainPageData("Speed", Speed::class.java, R.drawable.speedometer)
        )

        val adapter = ItemAdapter(this, itemList)
        rv_item.adapter = adapter

    }
}