/*
 *  Copyright (C) 2010 Nasser Giacaman, Oliver Sinnen
 *
 *  This file is part of Parallel Task.
 *
 *  Parallel Task is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or (at 
 *  your option) any later version.
 *
 *  Parallel Task is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General 
 *  Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along 
 *  with Parallel Task. If not, see <http://www.gnu.org/licenses/>.
 */

package pt.runtime;

import java.util.ArrayList;

/**
 * 
 * Define a reduction of two items into one. This definition may then be used to reduce a collection 
 * of items into one item. This is primarily intended for reducing the results from a multi-task 
 * (see {@link TaskIDGroup#reduce(Reduction)}).
 * <br><br>
 * This approach allows the programmer to easily define complex reductions that could even involve 
 * entire data structures, for example concatenating lists or maps. A reduction must be: 
 * <ul>
 * <li> <i>associative</i> (the order of performing the reduction makes no difference), and
 * <li> <i>commutative</i> (the order of the items to be reduced makes no difference) since 
 * the interface does not specify order.
 * </ul> 
 * For convenience, some pre-defined reductions are included to be used for common types.
 * 
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 *
 * @param <E>	The type of elements to be reduced
 */
public interface Reduction<E> {

	/**
	 * Specifies a reduction as defined by 2 elements into 1. The reduction must obey the following constraints:
	 * <ul>
	 * <li> <i>associative:</i> the order of evaluating the reduction makes no difference, and
	 * <li> <i>commutative</i> the order of the values (i.e. <code>a</code> and <code>b</code>) makes no difference. 
	 * 
	 * @param a	 The first element in the reduction
	 * @param b The second element in the reduction
	 * @return The result of reducing <code>a</code> with <code>b</code>.
	 */
	public E combine(E a, E b);
	
	/*
	 *	Integer reductions 
	 */
	/**
	 * Perform a minimum reduction for <code>Integer</code> values.
	 */
	public static Reduction<Integer> IntegerMIN = new Reduction<Integer>() {
		public Integer combine(Integer a, Integer b) {
			return Math.min(a, b);
		}
	};
	/**
	 * Perform a maximum reduction for <code>Integer</code> values.
	 */
	public static Reduction<Integer> IntegerMAX = new Reduction<Integer>() {
		public Integer combine(Integer a, Integer b) {
			return Math.max(a, b);
		}
	};
	/**
	 * Perform a sum reduction for <code>Integer</code> values.
	 */
	public static Reduction<Integer> IntegerSUM = new Reduction<Integer>() {
		public Integer combine(Integer a, Integer b) {
			return a + b;
		}
	};
	
	/*
	 * 	Double reductions
	 */
	/**
	 * Perform a minimum reduction for <code>Double</code> values.
	 */
	public static Reduction<Double> DoubleMIN = new Reduction<Double>() {
		public Double combine(Double a, Double b) {
			return Math.min(a, b);
		}
	};
	/**
	 * Perform a maximum reduction for <code>Double</code> values.
	 */
	public static Reduction<Double> DoubleMAX = new Reduction<Double>() {
		public Double combine(Double a, Double b) {
			return Math.max(a, b);
		}
	};
	/**
	 * Perform a sum reduction for <code>Double</code> values.
	 */
	public static Reduction<Double> DoubleSUM = new Reduction<Double>() {
		public Double combine(Double a, Double b) {
			return a + b;
		}
	};
	
	/**
	 * Combines the elements from the arrays into a new array. 
	 */
	public static Reduction<Object[]> ArrayCOMBINE = new Reduction<Object[]>() {
		public Object[] combine(Object[] a, Object[] b) {
			ArrayList<Object> list = new ArrayList<Object>();
			for (int i = 0; i < a.length; i++)
				list.add(a[i]);
			for (int i = 0; i < b.length; i++)
				list.add(b[i]);
			return list.toArray();
		}
	};
	
	/**
	 * Performs a Boolean OR
	 */
	public static Reduction<Boolean> BooleanOR = new Reduction<Boolean>() {
		@Override
		public Boolean combine(Boolean a, Boolean b) {
			return a || b;
		}
	};
	

	/**
	 * Performs a Boolean AND
	 */
	public static Reduction<Boolean> BooleanAND = new Reduction<Boolean>() {
		@Override
		public Boolean combine(Boolean a, Boolean b) {
			return a && b;
		}
	};

	/*
	 * TODO implement more built-in reductions
	 */
}
