package LDA

object BasicLDA extends App {
  val dir = System.getProperty("user.dir") + java.io.File.separator

  println(java.lang.Runtime.getRuntime.maxMemory)
  print("Source file for corpus (in LDA dir, omit .txt extension): ")
  val keyboard = new java.util.Scanner(System.in)
  var userInput = keyboard.nextLine()
  val inPath = dir + userInput + ".txt";
  val outPath = dir + userInput + "Model.txt";

  print("Number of topics? ")
  val z = keyboard.nextLine().toInt

  print("Number of iterations? ");
  val iterations = keyboard.nextLine().toInt

  val corpus = new SparseTable(inPath)
  corpus.exportWords(dir + "Words.txt")
  corpus.exportDocs(dir + "DocIDs.txt")
  corpus.shuffle()

  val Array(d, v, n, minTime, maxTime) = corpus.constants
  val parameters = Array(d, v, n, z, minTime, maxTime)
  println("D %d V %d N %d Z %d min %d max %d".format(parameters: _*))

  // Use a seed for repeatability during development.
  val randomize = new scala.util.Random(1)
  val randomTopics = Array.fill(n)(randomize.nextInt(z))

  val sampler = new Gibbs(corpus.wordArray, corpus.docArray, randomTopics, corpus.timeline, parameters)
  sampler.exportVinT(dir + "Vint.txt")

	println("Begin iterations.")
  (0 until iterations).foreach { i =>
    sampler.cycle()
    println("Iter %d: %f".format(i, sampler.perplex))
    // String cyclePath = "/Users/tunderwood/LDA/iter" + i + ".txt";
    // sampler.exportTimeline(cyclePath)
    if (i % 20 == 1) {
      corpus.exportTopics(outPath, sampler.topics(100))
      sampler.exportKL(dir + "KLdivergence.txt")
      sampler.exportTinD(dir + "ThetaDistrib.txt")
    }
  }

  corpus.exportTopics(outPath, sampler.topics(100))
  sampler.exportKL(dir + "KLdivergence.txt")
  sampler.exportTinD(dir + "ThetaDistrib.txt")
}

