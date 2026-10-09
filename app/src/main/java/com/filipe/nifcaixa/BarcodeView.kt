package com.filipe.nifcaixa

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Uma View que desenha o código de barras.
 * Recebe o texto, pede as larguras ao Code128 e pinta retângulos pretos sobre branco.
 */
class BarcodeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val preto = Paint().apply { color = Color.BLACK; isAntiAlias = false }
    private var larguras: List<Int> = emptyList()

    fun setText(text: String) {
        larguras = Code128.encode(text)
        invalidate() // pede ao Android para voltar a desenhar
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE) // fundo sempre branco, o leitor precisa de contraste
        if (larguras.isEmpty()) return

        // Zona de silêncio: 10 módulos brancos de cada lado (obrigatório para o leitor encontrar o início).
        val quietZone = 10
        val totalModulos = larguras.sum() + quietZone * 2

        // Cada módulo ocupa um número inteiro de píxeis, para as barras ficarem nítidas.
        val modulo = maxOf(1, width / totalModulos)
        var x = ((width - (larguras.sum() * modulo)) / 2).toFloat()

        larguras.forEachIndexed { i, w ->
            val larguraPx = w * modulo
            if (i % 2 == 0) { // posições pares = barras pretas
                canvas.drawRect(x, 0f, x + larguraPx, height.toFloat(), preto)
            }
            x += larguraPx
        }
    }
}
