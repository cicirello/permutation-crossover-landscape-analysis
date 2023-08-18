/*
 * Fitness landscape analysis of crossover operators for permutations.
 * Copyright (C) 2023 Vincent A. Cicirello
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.cicirello.experiments.permutationxoverlandscapes;

import java.util.ArrayList;
import java.util.SplittableRandom;
import org.cicirello.permutations.Permutation;
import org.cicirello.permutations.distance.LeeDistance;
import org.cicirello.search.Configurator;
import org.cicirello.search.ReoptimizableMetaheuristic;
import org.cicirello.search.evo.AdaptiveEvolutionaryAlgorithm;
import org.cicirello.search.evo.AdaptiveMutationOnlyEvolutionaryAlgorithm;
import org.cicirello.search.evo.NegativeIntegerCostFitnessFunction;
import org.cicirello.search.evo.TournamentSelection;
import org.cicirello.search.operators.CrossoverOperator;
import org.cicirello.search.operators.MutationOperator;
import org.cicirello.search.operators.permutations.CycleCrossover;
import org.cicirello.search.operators.permutations.EdgeRecombination;
import org.cicirello.search.operators.permutations.EnhancedEdgeRecombination;
import org.cicirello.search.operators.permutations.NonWrappingOrderCrossover;
import org.cicirello.search.operators.permutations.OrderCrossover;
import org.cicirello.search.operators.permutations.OrderCrossoverTwo;
import org.cicirello.search.operators.permutations.PartiallyMatchedCrossover;
import org.cicirello.search.operators.permutations.PermutationInitializer;
import org.cicirello.search.operators.permutations.PositionBasedCrossover;
import org.cicirello.search.operators.permutations.PrecedencePreservativeCrossover;
import org.cicirello.search.operators.permutations.SwapMutation;
import org.cicirello.search.operators.permutations.UniformOrderBasedCrossover;
import org.cicirello.search.operators.permutations.UniformPartiallyMatchedCrossover;
import org.cicirello.search.operators.permutations.UniformPrecedencePreservativeCrossover;
import org.cicirello.search.problems.PermutationInAHaystack;

/**
 * Experiments with the Permutation in a Haystack problem using Lee Distance.
 *
 * <p>Code to reproduce the experiments from the following article:
 *
 * <p>INSERT DETAILS HERE ONCE PUBLISHED
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class HaystackLeeExperiments {

  /**
   * Runs the experiments.
   *
   * @param args Ignored as there are no command line arguments.
   */
  public static void main(String[] args) {
    // seed chips-n-salsa's random number generator for reproducibility
    Configurator.configureRandomGenerator(42L);

    final int PERM_LENGTH = 100;
    final int NUM_INSTANCES = 100;
    final int POPULATION_SIZE = 100;
    final int MAX_GENERATIONS = 10000;

    ArrayList<CrossoverOperator<Permutation>> crossoverOps =
        new ArrayList<CrossoverOperator<Permutation>>();
    ArrayList<String> columnLabels = new ArrayList<String>();
    columnLabels.add("Swap");

    crossoverOps.add(new CycleCrossover());
    columnLabels.add("CX");
    crossoverOps.add(new EdgeRecombination());
    columnLabels.add("ER");
    crossoverOps.add(new EnhancedEdgeRecombination());
    columnLabels.add("EER");
    crossoverOps.add(new OrderCrossover());
    columnLabels.add("OX");
    crossoverOps.add(new NonWrappingOrderCrossover());
    columnLabels.add("NWOX");
    crossoverOps.add(new UniformOrderBasedCrossover());
    columnLabels.add("UOBX");
    crossoverOps.add(new OrderCrossoverTwo());
    columnLabels.add("OX2");
    crossoverOps.add(new PrecedencePreservativeCrossover());
    columnLabels.add("PPX");
    crossoverOps.add(new UniformPrecedencePreservativeCrossover());
    columnLabels.add("UPPX");
    crossoverOps.add(new PartiallyMatchedCrossover());
    columnLabels.add("PMX");
    crossoverOps.add(new UniformPartiallyMatchedCrossover());
    columnLabels.add("UPMX");
    crossoverOps.add(new PositionBasedCrossover());
    columnLabels.add("PBX");

    MutationOperator<Permutation> mutationOp = new SwapMutation();

    System.out.print("Instance\tGenerations");
    for (String label : columnLabels) {
      System.out.print("\t" + label);
    }
    System.out.println();
    for (int seed = 1; seed <= NUM_INSTANCES; seed++) {
      PermutationInAHaystack problem =
          new PermutationInAHaystack(
              new LeeDistance(), new Permutation(PERM_LENGTH, new SplittableRandom(seed)));

      ArrayList<ReoptimizableMetaheuristic<Permutation>> evos =
          new ArrayList<ReoptimizableMetaheuristic<Permutation>>();

      // Swap mutation only as benchmark
      evos.add(
          new AdaptiveMutationOnlyEvolutionaryAlgorithm<Permutation>(
              POPULATION_SIZE,
              mutationOp.split(),
              new PermutationInitializer(PERM_LENGTH),
              new NegativeIntegerCostFitnessFunction<Permutation>(problem),
              new TournamentSelection(),
              1));

      // Adaptive EA for each crossover op using Swap as mutation
      for (CrossoverOperator<Permutation> crossover : crossoverOps) {
        evos.add(
            new AdaptiveEvolutionaryAlgorithm<Permutation>(
                POPULATION_SIZE,
                mutationOp.split(),
                crossover.split(),
                new PermutationInitializer(PERM_LENGTH),
                new NegativeIntegerCostFitnessFunction<Permutation>(problem),
                new TournamentSelection(),
                1));
      }

      int totalGenerations = 1;
      System.out.print(seed + "\t" + totalGenerations);
      for (ReoptimizableMetaheuristic<Permutation> ea : evos) {
        ea.optimize(totalGenerations);
        System.out.print("\t" + ea.getProgressTracker().getCost());
      }
      System.out.println();
      int prevTotalGens = totalGenerations;
      for (totalGenerations *= 10; totalGenerations <= MAX_GENERATIONS; totalGenerations *= 10) {
        System.out.print(seed + "\t" + totalGenerations);
        for (ReoptimizableMetaheuristic<Permutation> ea : evos) {
          ea.reoptimize(totalGenerations - prevTotalGens);
          System.out.print("\t" + ea.getProgressTracker().getCost());
        }
        prevTotalGens = totalGenerations;
        System.out.println();
        System.out.flush();
      }
    }
  }
}
