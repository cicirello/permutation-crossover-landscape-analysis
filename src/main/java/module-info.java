/*
 * Experiments for: A Survey and Analysis of Evolutionary Operators for Permutations.
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

/**
 * Code to reproduce the experiments from the following article:
 *
 * <p>Vincent A. Cicirello. 2023. <a
 * href="https://www.cicirello.org/publications/cicirello2023ecta.html">A Survey and Analysis of
 * Evolutionary Operators for Permutations</a>. In <i>Proceedings of the 15th International Joint
 * Conference on Computational Intelligence</i>, pages 288-299. doi:<a
 * href="https://doi.org/10.5220/0012204900003595">10.5220/0012204900003595</a>.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
module org.cicirello.permutation_crossover_landscape_analysis {
  exports org.cicirello.experiments.permutationxoverlandscapes;

  requires org.cicirello.chips_n_salsa;
  requires org.cicirello.jpt;
}
