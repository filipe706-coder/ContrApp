package com.filipe.nifcaixa

/**
 * Gerador de código de barras Code 128 (conjunto B), o formato que quase todos
 * os leitores de supermercado conseguem ler.
 *
 * Como funciona um Code 128:
 *  - Cada símbolo é desenhado com 3 barras pretas e 3 espaços brancos, alternados.
 *  - Cada barra/espaço tem largura de 1 a 4 "módulos" (o módulo é a barra mais fina).
 *  - O código é: [START B] + [um símbolo por carácter] + [checksum] + [STOP].
 *  - O checksum permite ao leitor detetar se leu mal algum símbolo.
 */
object Code128 {

    // Tabela oficial: para cada valor 0..106, as larguras "barra espaço barra espaço barra espaço".
    // O 106 (STOP) tem uma barra extra no fim, por isso tem 7 números.
    private val PATTERNS = arrayOf(
        "212222", "222122", "222221", "121223", "121322", "131222", "122213", "122312", "132212", "221213",
        "221312", "231212", "112232", "122132", "122231", "113222", "123122", "123221", "223211", "221132",
        "221231", "213212", "223112", "312131", "311222", "321122", "321221", "312212", "322112", "322211",
        "212123", "212321", "232121", "111323", "131123", "131321", "112313", "132113", "132311", "211313",
        "231113", "231311", "112133", "112331", "132131", "113123", "113321", "133121", "313121", "211331",
        "231131", "213113", "213311", "213131", "311123", "311321", "331121", "312113", "312311", "332111",
        "314111", "221411", "431111", "111224", "111422", "121124", "121421", "141122", "141221", "112214",
        "112412", "122114", "122411", "142112", "142211", "241211", "221114", "413111", "241112", "134111",
        "111242", "121142", "121241", "114212", "124112", "124211", "411212", "421112", "421211", "212141",
        "214121", "412121", "111143", "111341", "131141", "114113", "114311", "411113", "411311", "113141",
        "114131", "311141", "411131", "211412", "211214", "211232", "2331112"
    )

    private const val START_B = 104
    private const val STOP = 106

    /**
     * Converte o texto numa lista de larguras (em módulos).
     * A posição 0, 2, 4... é barra preta; 1, 3, 5... é espaço branco.
     */
    fun encode(text: String): List<Int> {
        // No conjunto B, o valor de cada carácter é o código ASCII menos 32 ('0' = 16, '9' = 25).
        val valores = text.map { it.code - 32 }

        // Checksum: START + soma de (posição × valor), tudo módulo 103.
        var soma = START_B
        valores.forEachIndexed { i, v -> soma += (i + 1) * v }
        val checksum = soma % 103

        val simbolos = listOf(START_B) + valores + listOf(checksum, STOP)
        return simbolos.flatMap { s -> PATTERNS[s].map { it.digitToInt() } }
    }
}
