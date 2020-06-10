package com.mycompany.beam;

import java.util.ArrayList;

import org.apache.beam.sdk.transforms.Combine.CombineFn;

public class ReduceFn extends CombineFn<CoordsMapa, ReduceFn.Accum, CoordsMapa[]>{
    
    public static class Accum{
        ArrayList<CoordsMapa> datos = new ArrayList<>(); 
    }

    @Override
    public Accum createAccumulator() {
        return new Accum();
    }

    @Override
    public Accum addInput(Accum mutableAccumulator, CoordsMapa input) {
        mutableAccumulator.datos.add(input);
        return mutableAccumulator;
    }

    @Override
    public Accum mergeAccumulators(Iterable<Accum> accumulators) {
        Accum ac = createAccumulator();
        for(Accum a : accumulators){
            ac.datos.addAll(a.datos);
        }
        return ac;
    }

    @Override
    public CoordsMapa[] extractOutput(Accum accumulator) {
        return accumulator.datos.toArray(new CoordsMapa[accumulator.datos.size()]);
    }
}