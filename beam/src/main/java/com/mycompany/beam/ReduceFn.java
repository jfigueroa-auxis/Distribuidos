package com.mycompany.beam;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.beam.sdk.transforms.Combine.CombineFn;

public class ReduceFn extends CombineFn<CoordsMapa, ReduceFn.Accum, CoordsMapa[]>{
    
    /**
     *
     */
    private static final long serialVersionUID = 7259993573225972659L;

    public static class Accum implements Serializable {
        /**
         *
         */
        private static final long serialVersionUID = 7846961552889293641L;
        ArrayList<CoordsMapa> datos = new ArrayList<>();
    }

    @Override
    public Accum createAccumulator() {
        return new Accum();
    }

    @Override
    public Accum addInput(final Accum mutableAccumulator, final CoordsMapa input) {
        mutableAccumulator.datos.add(input);
        return mutableAccumulator;
    }

    @Override
    public Accum mergeAccumulators(final Iterable<Accum> accumulators) {
        final Accum ac = createAccumulator();
        for (final Accum a : accumulators) {
            ac.datos.addAll(a.datos);
        }
        return ac;
    }

    @Override
    public CoordsMapa[] extractOutput(final Accum accumulator) {
        return accumulator.datos.toArray(new CoordsMapa[accumulator.datos.size()]);
    }
}