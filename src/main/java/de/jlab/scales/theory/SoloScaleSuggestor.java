package de.jlab.scales.theory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.KShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.EppsteinKShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.builder.GraphBuilder;
import org.jgrapht.util.SupplierUtil;

public class SoloScaleSuggestor implements Iterable<List<ScaleInfo>> {

  @lombok.RequiredArgsConstructor
  @lombok.Getter
  static class Vertex {
    private final ScaleInfo info;
  }

  static class ScaleNotFoundException extends IllegalArgumentException {}
  
  interface Strategy {
    double weight(Vertex source, Vertex target);
    List<Vertex> toVertices(Scale chord);
  }
  
  public static class DefaultStrategy implements Strategy {

    private final ScaleUniverse universe;

    public DefaultStrategy(ScaleUniverse universe) {
      this.universe = universe;
    }
    
    @Override
    public double weight(Vertex source, Vertex target) {
      if (source.getInfo() == null || target.getInfo() == null) {
        return 0;
      }
      return numberOfDifferentNotes(source.getInfo().getScale(), target.getInfo().getScale());
    }
    
    int numberOfDifferentNotes(Scale source, Scale target) {
      Set<Note> diff = removeAll(source, target);
      diff.addAll(removeAll(target, source));
      return diff.size();
    }

    Set<Note> removeAll(Scale source, Scale target) {
      Set<Note> set = source.asSet();
      set.removeAll(target.asSet());
      return set;
    }
    
    @Override
    public List<Vertex> toVertices(Scale chord) {
      return universe.findScalesContaining(chord.asSet())
          .stream()
          .map(info -> new Vertex(info))
          .collect(Collectors.toList());
    }
    
  }
  
  private final SimpleDirectedWeightedGraph<Vertex, DefaultWeightedEdge> graph;
  private final Vertex source = new Vertex(null);
  private final Vertex target = new Vertex(null);
  private final int numberOfPaths;
  private Strategy strategy;

  public SoloScaleSuggestor(Strategy strategy, List<Scale> chords, int numberOfPaths) {
    this.strategy = strategy;
    this.numberOfPaths = numberOfPaths;
    this.graph = new SimpleDirectedWeightedGraph<Vertex, DefaultWeightedEdge>(DefaultWeightedEdge.class);

    List<Vertex> prev = List.of(source);
    graph.addVertex(source);
    Iterator<Scale> chordIterator = chords.iterator();
    while (chordIterator.hasNext()) {
      Scale chord = chordIterator.next();
      List<Vertex> next = strategy.toVertices(chord);
      if (next.isEmpty()) {
        throw new ScaleNotFoundException();
      }
      addVertices(prev, next);
      prev = next;
      if (!chordIterator.hasNext()) {
        addVertices(prev, List.of(target));
      }
    }

  }

  private void addVertices(List<Vertex> prev, List<Vertex> next) {
    for (Vertex source : prev) {
      for (Vertex target : next) {
        graph.addVertex(target);
        DefaultWeightedEdge edge = graph.addEdge(source, target);
        graph.setEdgeWeight(edge, strategy.weight(source, target));
      }
    }
  }

  @Override
  public Iterator<List<ScaleInfo>> iterator() {
    KShortestPathAlgorithm<Vertex, DefaultWeightedEdge> algorithm = new EppsteinKShortestPath<>(graph);
    List<GraphPath<Vertex, DefaultWeightedEdge>> paths = algorithm.getPaths(source,  target, numberOfPaths);
    return paths.stream().map(
      gp -> gp.getVertexList().stream().filter(v -> v.info != null).map(v -> v.info).collect(Collectors.toList())
    ).collect(Collectors.toList()).iterator();
  }
}
