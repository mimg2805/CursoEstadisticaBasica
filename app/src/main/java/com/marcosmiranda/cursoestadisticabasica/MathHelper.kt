package com.marcosmiranda.cursoestadisticabasica

import android.util.Log
import ch.obermuhlner.math.big.BigDecimalMath.*
import org.mariuszgromada.math.mxparser.mathcollection.MathFunctions.*
import java.math.*

class MathHelper {
    companion object {

        //private val mc0 = MathContext(0, RoundingMode.HALF_EVEN)
        //private val mc1 = MathContext(1, RoundingMode.HALF_EVEN)
        //private val mc2 = MathContext(2, RoundingMode.HALF_EVEN)
        private val mc3 = MathContext(3, RoundingMode.HALF_EVEN)
        private val mc4 = MathContext(4, RoundingMode.HALF_EVEN)
        //private val mc5 = MathContext(5, RoundingMode.HALF_EVEN)

        fun strToBigInteger(str: String): BigInteger {
            var num = BigInteger.ZERO
            if (str.isNotEmpty() && str.isNotBlank()) num = BigInteger(str)
            return num
        }

        fun strToBigDecimal(str: String): BigDecimal {
            var num = BigDecimal.ZERO
            try {
                if (str.isBlank() || str.isEmpty() || str == ".")
                    num = BigDecimal.ZERO
                else if (str.isNotBlank() || str.isNotEmpty() || str != ".")
                    num = BigDecimal.ZERO + BigDecimal(str)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return num
        }

        fun distNormalEstandar(x: BigDecimal, mi: BigDecimal, sigma: BigDecimal, n: BigDecimal): BigDecimal {
            val top = x - mi
            val bottom = sigma.divide(sqrt(n, mc4), mc4)
            return top.divide(bottom, mc4).round(mc4)
        }

        fun desvEst(numsList: List<BigDecimal>, media: BigDecimal): BigDecimal {
            var subSum = BigDecimal.ZERO
            val n = numsList.size.toBigDecimal()
            var dest = BigDecimal.ONE

            if (n == BigDecimal.ONE)
                return dest
            else {
                for (i in numsList) {
                    subSum += pow((i - media), 2, mc4)
                }
                dest = subSum.divide(n - BigDecimal.ONE, mc4)
            }

            return sqrt(dest, mc4)
        }

        fun mediana(numsList: List<BigDecimal>): BigDecimal {
            val orderedNumsList = numsList.sorted()
            val n = numsList.size.toBigDecimal()
            //Log.e("ordered nums list", orderedNumsList.toString())
            val pos = (n + BigDecimal.ONE).divide(BigDecimal("2"), mc3)
            //Log.e("pos", pos.toPlainString())

            var mediana = BigDecimal.ZERO
            if (orderedNumsList.isNotEmpty()) {
                if (pos > BigDecimal.ONE) {
                    if (pos.toString().contains(".")) {
                        val posUp = ceil((pos - BigDecimal.ONE).toDouble())
                        val posDown = floor((pos - BigDecimal.ONE).toDouble())
                        //Log.e("posUp", posUp.toString())
                        //Log.e("posDown", posUp.toString())
                        //Log.e("num[posUp]", orderedNumsList[posUp.toInt()].toPlainString())
                        //Log.e("num[posDown]", orderedNumsList[posDown.toInt()].toPlainString())
                        mediana = (orderedNumsList[posUp.toInt()] + orderedNumsList[posDown.toInt()]).divide(
                            BigDecimal("2"),
                            mc3
                        )
                    } else {
                        mediana = orderedNumsList[pos.toInt() - 1]
                    }
                } else {
                    mediana = orderedNumsList.first()
                }
            }

            //Log.e("mediana", mediana.toPlainString())
            return mediana
        }

        fun cuartil(numsList: List<BigDecimal>, cuartil: Int): BigDecimal {
            val size = numsList.size
            lateinit var numsSubList: List<BigDecimal>
            var crt = BigDecimal.ZERO

            if (size > 2) {
                val fin = round(size / 2.0, 2).toInt()
                //Log.e("fin", fin.toString())

                if (cuartil == 1) {
                    numsSubList = numsList.sorted().subList(0, fin)
                } else if (cuartil == 3) {
                    numsSubList = numsList.sorted().subList(fin, size)
                }
                Log.e("size", size.toString())
                Log.e("numsSubList", numsSubList.toString())
                crt = mediana(numsSubList)
            }

            return crt
        }
    }
}