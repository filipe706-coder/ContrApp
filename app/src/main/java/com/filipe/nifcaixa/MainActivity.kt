package com.filipe.nifcaixa

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

/**
 * O único ecrã da app. Tem dois modos:
 *  - MOSTRAR: código de barras + números grandes (é o que mostras na caixa)
 *  - EDITAR: caixas de texto para pôr ou mudar o NIF
 */
class MainActivity : Activity() {

    // SharedPreferences = pequena "gaveta" de dados guardada só neste telemóvel.
    private val prefs by lazy { getSharedPreferences("nif_caixa", Context.MODE_PRIVATE) }

    private lateinit var modoMostrar: View
    private lateinit var modoEditar: View
    private lateinit var barcode: BarcodeView
    private lateinit var digitos: TextView
    private lateinit var nomeMostrado: TextView
    private lateinit var campoNif: EditText
    private lateinit var campoNome: EditText
    private lateinit var erro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Liga as variáveis aos elementos do layout (activity_main.xml)
        modoMostrar = findViewById(R.id.modoMostrar)
        modoEditar = findViewById(R.id.modoEditar)
        barcode = findViewById(R.id.barcode)
        digitos = findViewById(R.id.digitos)
        nomeMostrado = findViewById(R.id.nomeMostrado)
        campoNif = findViewById(R.id.campoNif)
        campoNome = findViewById(R.id.campoNome)
        erro = findViewById(R.id.erro)

        findViewById<Button>(R.id.botaoGuardar).setOnClickListener { guardar() }
        findViewById<Button>(R.id.botaoAlterar).setOnClickListener { abrirEditar() }

        // Ecrã nunca se apaga enquanto a app está aberta
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val nifGuardado = prefs.getString("nif", null)
        if (nifGuardado == null) abrirEditar() else mostrar(nifGuardado, prefs.getString("nome", "") ?: "")
    }

    private fun guardar() {
        val nif = campoNif.text.toString().filter { it.isDigit() }
        val nome = campoNome.text.toString().trim()

        val mensagemErro = Nif.validate(nif)
        if (mensagemErro != null) {
            erro.text = mensagemErro
            erro.visibility = View.VISIBLE
            return
        }

        prefs.edit().putString("nif", nif).putString("nome", nome).apply()
        esconderTeclado()
        mostrar(nif, nome)
    }

    private fun mostrar(nif: String, nome: String) {
        barcode.setText(nif)
        digitos.text = Nif.formatar(nif)
        nomeMostrado.text = nome
        nomeMostrado.visibility = if (nome.isEmpty()) View.GONE else View.VISIBLE

        modoEditar.visibility = View.GONE
        modoMostrar.visibility = View.VISIBLE
        brilho(maximo = true) // brilho no máximo para o leitor ler melhor
    }

    private fun abrirEditar() {
        campoNif.setText(prefs.getString("nif", ""))
        campoNome.setText(prefs.getString("nome", ""))
        erro.visibility = View.GONE

        modoMostrar.visibility = View.GONE
        modoEditar.visibility = View.VISIBLE
        brilho(maximo = false)
        campoNif.requestFocus()
    }

    /** Muda o brilho só desta janela; quando sais da app, volta ao normal sozinho. */
    private fun brilho(maximo: Boolean) {
        val params = window.attributes
        params.screenBrightness =
            if (maximo) WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
            else WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        window.attributes = params
    }

    private fun esconderTeclado() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(campoNif.windowToken, 0)
    }
}
