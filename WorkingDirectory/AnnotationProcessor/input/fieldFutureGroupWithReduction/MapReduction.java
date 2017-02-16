package fieldFutureGroupWithReduction;

import java.util.List;
import java.util.Map;

import pu.RedLib.MapUnion;
import pu.RedLib.Reduction;

public class MapReduction implements Reduction<Map<Integer, List<String>>> {
	Reduction<Map<Integer, List<String>>> reducer = null;
	
	public MapReduction(Reduction<List<String>> innerRed) {
		reducer = new MapUnion<>(innerRed);
	}

	@Override
	public Map<Integer, List<String>> reduce(Map<Integer, List<String>> first, Map<Integer, List<String>> second) {
		return reducer.reduce(first, second);
	}

}
