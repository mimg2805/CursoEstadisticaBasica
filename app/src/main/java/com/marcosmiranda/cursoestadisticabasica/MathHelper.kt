package com.marcosmiranda.cursoestadisticabasica

import android.util.Log
import ch.obermuhlner.math.big.BigDecimalMath.pow
import ch.obermuhlner.math.big.BigDecimalMath.sqrt
import org.mariuszgromada.math.mxparser.mathcollection.MathFunctions.round
import org.mariuszgromada.math.mxparser.mathcollection.NumberTheory
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.ceil
import kotlin.math.floor

class MathHelper {
    companion object {

        // private val mc0 = MathContext(0, RoundingMode.HALF_EVEN)
        // private val mc1 = MathContext(1, RoundingMode.HALF_EVEN)
        // private val mc2 = MathContext(2, RoundingMode.HALF_EVEN)
        private val mc3 = MathContext(3, RoundingMode.HALF_EVEN)
        private val mc4 = MathContext(4, RoundingMode.HALF_EVEN)
        // private val mc5 = MathContext(5, RoundingMode.HALF_EVEN)

        fun strToBigInteger(str: String): BigInteger {
            var num = BigInteger.ZERO
            if (str.isNotEmpty() && str.isNotBlank()) num = BigInteger(str)
            return num
        }

        fun strToBigDecimal(str: String): BigDecimal {
            var num = BigDecimal.ZERO
            try {
                num = if (str.isBlank() || str.isEmpty() || str == ".")
                    BigDecimal.ZERO
                else
                    BigDecimal.ZERO + BigDecimal(str.trim())
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

        fun amplitud(values: List<BigDecimal>): BigDecimal {
            return values.max() - values.min()
        }

        fun desviacionMedia(values: List<BigDecimal>): BigDecimal {
            val n = values.size.toBigDecimal()
            val media = media(values)
            var diffSum = BigDecimal.ZERO

            values.forEach {
                diffSum += (it - media).abs()
            }

            return diffSum.divide(n, mc4)
        }

        fun desviacionEstandar(numsList: List<BigDecimal>): BigDecimal {
            var subSum = BigDecimal.ZERO
            val n = numsList.size.toBigDecimal()
            val media = media(numsList)
            var dest = BigDecimal.ONE

            if (n == BigDecimal.ONE)
                return dest
            else {
                numsList.forEach {
                    subSum += pow((it - media), 2, mc4)
                }
                dest = subSum.divide(n - BigDecimal.ONE, mc4)
            }

            return sqrt(dest, mc4)
        }

        fun media(values: List<BigDecimal>): BigDecimal {
            var sum = BigDecimal.ZERO
            values.forEach {
                sum += it
            }

            return sum.divide(values.size.toBigDecimal(), mc4)
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
                    mediana = if (pos.toString().contains(".")) {
                        val posUp = ceil((pos - BigDecimal.ONE).toDouble())
                        val posDown = floor((pos - BigDecimal.ONE).toDouble())
                        //Log.e("posUp", posUp.toString())
                        //Log.e("posDown", posUp.toString())
                        //Log.e("num[posUp]", orderedNumsList[posUp.toInt()].toPlainString())
                        //Log.e("num[posDown]", orderedNumsList[posDown.toInt()].toPlainString())
                        (orderedNumsList[posUp.toInt()] + orderedNumsList[posDown.toInt()]).divide(
                            BigDecimal("2"),
                            mc3
                        )
                    } else {
                        orderedNumsList[pos.toInt() - 1]
                    }
                } else {
                    mediana = orderedNumsList.first()
                }
            }

            //Log.e("mediana", mediana.toPlainString())
            return mediana
        }

        fun moda(values: List<BigDecimal>): List<BigDecimal> {
            val valuesDoubleArray = DoubleArray(values.size)
            var valuesDoubleArrayIndex = 0
            values.forEach {
                valuesDoubleArray[valuesDoubleArrayIndex] = it.toDouble()
                valuesDoubleArrayIndex++
            }

            val distArr = NumberTheory.getDistValues(valuesDoubleArray, true)
            val modaVals = mutableListOf<BigDecimal>()
            var modaMax = 0
            for (dist in distArr) {
                val value = dist[0].toBigDecimal(mc4)
                val count = dist[1].toInt()

                if (count > modaMax) {
                    modaMax = count
                }

                if (modaMax > 1 && count == modaMax) {
                    modaVals.add(value)
                }
            }

            return modaVals
        }

        fun cuartiles(values: List<BigDecimal>): List<BigDecimal> {
            val valuesSorted = values.sorted()
            val result = mutableListOf<BigDecimal>()

            for (quartileType in 1..<4) {
                val len = values.size + 1
                var quartile: BigDecimal
                val newArraySize = (len * (quartileType * 25 / 100f)) - 1
                val newArraySizeInt = newArraySize.toInt()
                quartile = if (newArraySize % 1 == 0f) {
                    valuesSorted[newArraySizeInt]
                } else {
                    (valuesSorted[newArraySizeInt] + valuesSorted[newArraySizeInt + 1]).divide(BigDecimal.valueOf(2), mc4)
                }

                result += quartile
            }

            return result
        }
    }
}