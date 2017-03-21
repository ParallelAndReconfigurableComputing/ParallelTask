

package fieldFutureGroupWithReduction;


public class MapReduction implements pu.RedLib.Reduction<java.util.Map<java.lang.Integer, java.util.List<java.lang.String>>> {
    pu.RedLib.Reduction<java.util.Map<java.lang.Integer, java.util.List<java.lang.String>>> reducer = null;

    public MapReduction(pu.RedLib.Reduction<java.util.List<java.lang.String>> innerRed) {
        reducer = new pu.RedLib.MapUnion<>(innerRed);
    }

    @java.lang.Override
    public java.util.Map<java.lang.Integer, java.util.List<java.lang.String>> reduce(java.util.Map<java.lang.Integer, java.util.List<java.lang.String>> first, java.util.Map<java.lang.Integer, java.util.List<java.lang.String>> second) {
        return reducer.reduce(first, second);
    }
}

