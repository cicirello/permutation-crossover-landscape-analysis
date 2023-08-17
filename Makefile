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

# Runs all experiments

.PHONY: experiments
experiments: HaystackEM

.PHONY: HaystackEM
HaystackEM:
	java -cp ${JARFILE} org.cicirello.experiments.permutationxoverlandscapes.HaystackEMExperiments > ${pathToDataFiles}/haystack.xover.em.txt
