package com.approval.unitconvertor.operations

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.approval.unitconvertor.R

class Area : AppCompatActivity() {

    lateinit var FromSpinner: AppCompatSpinner
    lateinit var ToSpinner: AppCompatSpinner
    lateinit var InputText: TextView
    lateinit var OutputText: TextView
    lateinit var buttonGrid: GridLayout

    lateinit var button1: Button
    lateinit var button2: Button
    lateinit var button3: Button
    lateinit var button4: Button
    lateinit var button5: Button
    lateinit var button6: Button
    lateinit var button7: Button
    lateinit var button8: Button
    lateinit var button9: Button
    lateinit var button0: Button
    lateinit var buttonEnter: Button
    lateinit var buttonDot: Button
    lateinit var buttonClear: Button
    lateinit var buttonDelete: Button

    lateinit var selectedFrom: String
    lateinit var selectedTo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_length_page)

        FromSpinner = findViewById(R.id.FromSpinner)
        ToSpinner = findViewById(R.id.ToSpinner)
        InputText = findViewById(R.id.InputText)
        OutputText = findViewById(R.id.OutputText)
        buttonGrid = findViewById(R.id.buttonGrid)

        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)
        button5 = findViewById(R.id.button5)
        button6 = findViewById(R.id.button6)
        button7 = findViewById(R.id.button7)
        button8 = findViewById(R.id.button8)
        button9 = findViewById(R.id.button9)
        button0 = findViewById(R.id.button0)
        buttonEnter = findViewById(R.id.buttonEnter)

        buttonDot = findViewById(R.id.dot_text)
        buttonClear = findViewById(R.id.clear_text)
        buttonDelete = findViewById(R.id.delete_text)

        FromSpinner.adapter = ArrayAdapter.createFromResource(this,R.array.Area_items,android.R.layout.simple_spinner_item).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        ToSpinner.adapter = ArrayAdapter.createFromResource(this,R.array.Area_items,android.R.layout.simple_spinner_item).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        FromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                selectedFrom = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        ToSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                selectedTo = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        button1.setOnClickListener {
            InputText.text = InputText.text.toString() + "1"
        }

        button2.setOnClickListener {
            InputText.text = InputText.text.toString() + "2"
        }

        button3.setOnClickListener {
            InputText.text = InputText.text.toString() + "3"
        }

        button4.setOnClickListener {
            InputText.text = InputText.text.toString() + "4"
        }

        button5.setOnClickListener {
            InputText.text = InputText.text.toString() + "5"
        }

        button6.setOnClickListener {
            InputText.text = InputText.text.toString() + "6"
        }

        button7.setOnClickListener {
            InputText.text = InputText.text.toString() + "7"
        }

        button8.setOnClickListener {
            InputText.text = InputText.text.toString() + "8"
        }

        button9.setOnClickListener {
            InputText.text = InputText.text.toString() + "9"

        }

        button0.setOnClickListener {
            InputText.text = InputText.text.toString() + "0"
        }

        buttonDot.setOnClickListener {
            InputText.text = InputText.text.toString() + "."
        }

        buttonClear.setOnClickListener {
            InputText.text = ""
            OutputText.text = ""
        }

        buttonDelete.setOnClickListener {
            if (InputText.text.isNotEmpty()){
                InputText.text = InputText.text.toString().dropLast(1)
            }
        }

        buttonEnter.setOnClickListener {
            if (InputText.text == null || InputText.text == "" || InputText.text == "Your Input"){
                OutputText.text = "Please enter a valid number"
            }else{
            calculate(selectedFrom,selectedTo)
            }
        }

    }

    private fun calculate(selectedFrom: String, selectedTo: String) {
        val input = InputText.text.toString().toDouble()

        val result = when {
            selectedFrom == "Square meters" && selectedTo == "Square kilometers" -> input / 1000000
            selectedFrom == "Square meters" && selectedTo == "Square feet" -> input * 10.7639
            selectedFrom == "Square meters" && selectedTo == "Square miles" -> input / 2589988.11
            selectedFrom == "Square meters" && selectedTo == "Square meters" -> input


            selectedFrom == "Square kilometers" && selectedTo == "Square meters" -> input * 1000000
            selectedFrom == "Square kilometers" && selectedTo == "Square feet" -> input * 10763900
            selectedFrom == "Square kilometers" && selectedTo == "Square miles" -> input / 2.58998811
            selectedFrom == "Square kilometers" && selectedTo == "Square kilometers" -> input

            selectedFrom == "Square miles" && selectedTo == "Square meters" -> input * 2589988.11
            selectedFrom == "Square miles" && selectedTo == "Square kilometers" -> input * 2.58998811
            selectedFrom == "Square miles" && selectedTo == "Square feet" -> input * 43560
            selectedFrom == "Square miles" && selectedTo == "Square miles" -> input

            selectedFrom == "Square feet" && selectedTo == "Square meters" -> input / 10.7639
            selectedFrom == "Square feet" && selectedTo == "Square kilometers" -> input / 10763900
            selectedFrom == "Square feet" && selectedTo == "Square miles" -> input / 43560
            selectedFrom == "Square feet" && selectedTo == "Square feet" -> input

            else -> {
                OutputText.text = "Select a valid unit from the options above."
                return
            }
        }

        OutputText.text = result.toString()
    }
}