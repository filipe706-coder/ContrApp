package com.filipe.nifcaixa

/**
 * Validação do NIF português.
 *
 * Regras:
 *  1. Tem de ter exatamente 9 dígitos.
 *  2. O primeiro dígito (ou os dois primeiros) tem de ser um prefixo que existe.
 *  3. O 9.º dígito é um "dígito de controlo" calculado a partir dos outros 8.
 */
object Nif {

    /** Devolve null se o NIF for válido, ou uma mensagem de erro em português. */
    fun validate(nif: String): String? {
        if (!Regex("^\\d{9}$").matches(nif)) {
            return "O NIF tem de ter 9 dígitos."
        }

        // Prefixos possíveis: 1, 2, 3 (pessoas), 5, 6, 8, 9 (empresas/entidades), 45 e 7x (casos especiais)
        val prefixoOk = nif[0] in "1235689" || nif.startsWith("45") || nif[0] == '7'
        if (!prefixoOk) {
            return "Este NIF começa por um número que não existe."
        }

        // Dígito de controlo: multiplica os 8 primeiros dígitos por 9, 8, 7, ... 2 e soma tudo.
        var soma = 0
        for (i in 0 until 8) {
            soma += nif[i].digitToInt() * (9 - i)
        }
        val resto = soma % 11
        val controlo = if (resto < 2) 0 else 11 - resto

        if (controlo != nif[8].digitToInt()) {
            return "O último dígito não bate certo. Confirma o número."
        }
        return null
    }

    /** "123456789" -> "123 456 789", mais fácil de ler para o operador. */
    fun formatar(nif: String): String = nif.chunked(3).joinToString(" ")
}
