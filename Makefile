ifeq ($(OS),Windows_NT)
	py = "python"
else
	py = "python3"
endif

JARFILE = "target/permutation-crossover-landscape-analysis-1.0.0-jar-with-dependencies.jar"
pathToDataFiles = "data"

.PHONY: build
build:
	mvn clean package

# Analyzes data assuming experiments already run

.PHONY: analysis
analysis:
	$(py) -m pip install --user pycairo
	$(py) -m pip install --user matplotlib
	$(py) src/main/python/summarize-stats.py ${pathToDataFiles}/haystack.xover.em.txt > ${pathToDataFiles}/summary.haystack.xover.em.txt
	$(py) src/main/python/summarize-stats.py ${pathToDataFiles}/haystack.xover.edge.txt > ${pathToDataFiles}/summary.haystack.xover.edge.txt
	$(py) src/main/python/summarize-stats.py ${pathToDataFiles}/haystack.xover.directedededge.txt > ${pathToDataFiles}/summary.haystack.xover.directedededge.txt
	$(py) src/main/python/summarize-stats.py ${pathToDataFiles}/haystack.xover.tau.txt > ${pathToDataFiles}/summary.haystack.xover.tau.txt
	$(py) src/main/python/summarize-stats.py ${pathToDataFiles}/haystack.xover.lee.txt > ${pathToDataFiles}/summary.haystack.xover.lee.txt

.PHONY: epstopdf
epstopdf:
	epstopdf ${pathToDataFiles}/haystack.xover.em.eps
	epstopdf ${pathToDataFiles}/haystack.xover.edge.eps
	epstopdf ${pathToDataFiles}/haystack.xover.directedededge.eps
	epstopdf ${pathToDataFiles}/haystack.xover.tau.eps
	epstopdf ${pathToDataFiles}/haystack.xover.lee.eps

# Runs all experiments

.PHONY: experiments
experiments: HaystackEM HaystackEdge HaystackDirectedEdge HaystackTau HaystackLee

.PHONY: HaystackEM
HaystackEM:
	java -cp ${JARFILE} org.cicirello.experiments.permutationxoverlandscapes.HaystackEMExperiments > ${pathToDataFiles}/haystack.xover.em.txt

.PHONY: HaystackEdge
HaystackEdge:
	java -cp ${JARFILE} org.cicirello.experiments.permutationxoverlandscapes.HaystackEdgeExperiments > ${pathToDataFiles}/haystack.xover.edge.txt

.PHONY: HaystackDirectedEdge
HaystackDirectedEdge:
	java -cp ${JARFILE} org.cicirello.experiments.permutationxoverlandscapes.HaystackDirectedEdgeExperiments > ${pathToDataFiles}/haystack.xover.directedededge.txt

.PHONY: HaystackTau
HaystackTau:
	java -cp ${JARFILE} org.cicirello.experiments.permutationxoverlandscapes.HaystackTauExperiments > ${pathToDataFiles}/haystack.xover.tau.txt

.PHONY: HaystackLee
HaystackLee:
	java -cp ${JARFILE} org.cicirello.experiments.permutationxoverlandscapes.HaystackLeeExperiments > ${pathToDataFiles}/haystack.xover.lee.txt
