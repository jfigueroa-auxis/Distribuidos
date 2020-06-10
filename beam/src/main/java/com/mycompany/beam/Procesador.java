/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.beam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.extensions.jackson.ParseJsons;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.Distinct;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
//import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.joda.time.Duration;

/**
 * An template that copies messages from one Pubsub subscription to another
 * Pubsub topic.
 */
public class Procesador {

  /**
   * Main entry point for executing the pipeline.
   *
   * @param args The command-line arguments to the pipeline.
   */
  public static void main(String[] args) {

    // Parse the user options passed from the command-line
    Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);

    options.setStreaming(true);

    run(options);
  }

  /**
   * Runs the pipeline with the supplied options.
   *
   * @param options The execution parameters to the pipeline.
   * @return The result of the pipeline execution.
   */
  public static PipelineResult run(Options options) {
    // Create the pipeline
    Pipeline pipeline = Pipeline.create(options);

    /**
     * Steps: 1) Read PubSubMessage with attributes from input PubSub subscription.
     * 2) Apply any filters if an attribute=value pair is provided. 3) Write each
     * PubSubMessage to output PubSub topic.
     */
    pipeline.apply("Lee del PubSub", PubsubIO.readStrings().fromSubscription(options.getInputSubscription()))
        .apply("ventana de recorte", Window.<String>into(FixedWindows.of(Duration.millis(500))))
        .apply("convertir al objeto", ParseJsons.of(Coords.class))
        .apply("eliminar duplicados", Distinct.withRepresentativeValueFn(Coords::getNombre))
        .apply("map", ParDo.of(new DoFn<Coords,CoordsMapa>() {
          @ProcessElement
          public void processElement(@Element Coords element, OutputReceiver<CoordsMapa> out){
            out.output(new CoordsMapa(element));
          }
        }))
        .apply("reduce", Combine.globally(new ReduceFn()))
        .apply("genera el objeto de coordenadas general", ParDo.of(new DoFn<CoordsMapa[], String>() {
          @ProcessElement
          public void processElement(@Element CoordsMapa[] input, OutputReceiver<String> out) throws JsonProcessingException {
              Mapa m = new Mapa(input);
              ObjectMapper mapper = new JsonMapper();
              String resultado = mapper.writeValueAsString(m);
              out.output(resultado);
          }
        })).apply("Escribe en PubSub", PubsubIO.writeStrings().to(options.getOutputTopic()));

    // Execute the pipeline and return the result.
    return pipeline.run();
  }

}