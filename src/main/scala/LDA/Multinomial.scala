package LDA

object Multinomial {
  private val generator = new scala.util.Random(0)
}

class Multinomial(probabilities: Array[Double]) {
  private val distribution = {
    val total = probabilities.sum
    probabilities.scanLeft(0.0)(_ + _ / total).tail.init :+ 1.0
  }

  def sample = {
    val uniform = Multinomial.generator.nextDouble
    this.distribution.indexWhere(_ > uniform)
  }
}

