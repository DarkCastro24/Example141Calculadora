package com.evarela.example141calculadora2

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private lateinit var operationTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.resultTextView)
        operationTextView = findViewById(R.id.operationTextView)

        // Agregamos todos los botones dentro de un arreglo
        val buttons = arrayOf(
            R.id.btn_seven, R.id.btn_eight, R.id.nine,
            R.id.btn_del, R.id.btn_four, R.id.btn_five, R.id.btn_six,
            R.id.btn_one, R.id.btn_two, R.id.btn_three,
            R.id.btn_zero, R.id.btn_dot, R.id.btn_result,
            R.id.btn_addition, R.id.btn_substraction, R.id.btn_multiplication, R.id.btn_division
        )

        // Asignamos la funcion de concatenar al dar click en cada boton utilizando el arreglo buttons
        for (id in buttons) {
            // Detectamos el evento de click con el listener
            findViewById<Button>(id).setOnClickListener {
                val buttonText = (it as Button).text
                appendText(buttonText)
            }
        }

        // Asignamos la función para borrar el último carácter del textView al dar click en el botón Del
        findViewById<Button>(R.id.btn_del).setOnClickListener {
            deleteChar()
        }

        // Ejecutamos la funcion de calcular resultado al dar click en el boton =
        findViewById<Button>(R.id.btn_result).setOnClickListener {
            calculateResult()
        }
    }

    // Función para borrar el último carácter ingresado
    private fun deleteChar() {
        val text = resultTextView.text.toString()
        if (text.isNotEmpty()) {
            // Eliminamos el último carácter usando substring
            val newText = text.substring(0, text.length - 1)
            // Actualizamos el TextView
            resultTextView.text = newText
        }
    }

    // Funcion para concatenar el boton pulsado en el TextView
    @SuppressLint("SetTextI18n")
    private fun appendText(character: CharSequence) {
        // Verificamos si el textView no esta vacio
        if (operationTextView.text.isNotEmpty()) {
            // Si no esta realizamos una operacion previamente
            cleanTextViews()
        }
        val text = resultTextView.text.toString()
        resultTextView.text = text + character
    }

    // Funcion para limpiar el texto de los TextView
    private fun cleanTextViews() {
        resultTextView.text = ""
        operationTextView.text = ""
    }

    // Funcion para calcular el resultado al presionar =
    @SuppressLint("SetTextI18n")
    private fun calculateResult() {
        // Obtenemos la operacion ingresada en el textView
        val operation = resultTextView.text.toString()
        // Colocamos un try catch por si ocurren errores
        try {
            // Obtenemos el resultado de la operacion mediante la funcion calculate
            val result = calculate(operation)
            // Mostramos la operacion realizada en el textView de abajo
            operationTextView.text = resultTextView.text
            // Mostramos el resultado en el textView principal (convirtiendolo a String)
            resultTextView.text = result.toString()
        } catch (e: Exception) {
            // Mostramos error en el TextView
            resultTextView.text = "Syntax Error"
        }
    }

    // Funcion para realizar el calculo en base a la operacion ingresada
    private fun calculate(operation: String): Double {
        // Inicializa la variable para almacenar el resultado en 0.0
        var result = 0.0
        // Creamos una lista para almacenar los números de la operación
        val numbers = mutableListOf<Double>()
        // Creamos una lista para almacenar los operadores
        val operators = mutableListOf<Char>()
        // Creamos un StringBuilder para construir los números a medida que se leen los caracteres
        val currentChar = StringBuilder()

        // Recorremos caracter por caracter la cadena operation
        for (char in operation) {
            // Verificamos si el caracter es un dígito o un punto
            if (char.isDigit() || char == '.') {
                // Añadimos el caracter al número actual
                currentChar.append(char)
            } else {
                // Verificamos si el número actual no está vacío
                if (currentChar.isNotEmpty()) {
                    // Convertimos el número actual a Double y lo agregamos a la lista
                    numbers.add(currentChar.toString().toDouble())
                    // Limpiamos el número actual
                    currentChar.clear()
                }
                // Verificamos si el caracter no es un espacio
                if (char != ' ') {
                    operators.add(char)
                }
            }
        }

        // Verificamos si después de iterar sobre todos los caracteres el número actual no está vacío
        if (currentChar.isNotEmpty()) {
            // Convertimos el número actual a Double y lo agregamos a la lista
            numbers.add(currentChar.toString().toDouble())
        }

        // Recorremos las posiciones de la lista de operadores
        for (i in operators.indices) {
            // Switch para identificar que operación se esta realizando en la posicion actual
            when (operators[i]) {
                '+' -> {
                    // Se agrega el número actual al resultado
                    result += numbers[i]
                    // Verificamos si es el último operador, si es asi se agrega el siguiente número al resultado
                    if (i == operators.size - 1) result += numbers[i + 1]
                }
                '-' -> {
                    // Se resta el número actual del resultado
                    result += numbers[i]
                    // Verificamos si es el último operador, si es asi se resta el siguiente número del resultado
                    if (i == operators.size - 1) result -= numbers[i + 1]
                }
                '*' -> {
                    // Se multiplica el número actual por el resultado
                    result += numbers[i]
                    // Verificamos si es el último operador, si es asi se multiplica el resultado por el siguiente número
                    if (i == operators.size - 1) result *= numbers[i + 1]
                }
                '/' -> {
                    // Se divide el número actual entre el resultado
                    result += numbers[i]
                    // Verificamos si es el último operador, si es asi divide el resultado entre el siguiente número
                    if (i == operators.size - 1) result /= numbers[i + 1]
                }
            }

        }
        return result
    }
}

