package de.jlab.scales.theory;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.KShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.EppsteinKShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class SoloScaleSuggestor implements Iterable<List<ScaleInfo>> {

  @lombok.RequiredArgsConstructor
  @lombok.Getter
  static class Vertex {
    private final Scale chord;
    private final ScaleInfo info;
  }

  static class ScaleNotFoundException extends IllegalArgumentException { }
  
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
      int numberOfDifferentNotes = numberOfDifferentNotes(source.getInfo().getScale(), target.getInfo().getScale());
      return numberOfDifferentNotes + 1000 * (scaleDifficulty(source) + scaleDifficulty(target));
    }
    
    private double scaleDifficulty(Vertex vertex) {
      switch ((BuiltinScaleType)vertex.info.getScaleType()) {
      case Major: return 1;
      case MelodicMinor: return 2;
      default: return 3;
      }
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
          .map(info -> new Vertex(chord, info))
          .collect(Collectors.toList());
    }
    
  }
  
  private final SimpleDirectedWeightedGraph<Vertex, DefaultWeightedEdge> graph;
  private final Vertex source = new Vertex(null, null);
  private final Vertex target = new Vertex(null, null);
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

  Stream<List<Vertex>> stream() {
    KShortestPathAlgorithm<Vertex, DefaultWeightedEdge> algorithm = new EppsteinKShortestPath<>(graph);
    List<GraphPath<Vertex, DefaultWeightedEdge>> paths = algorithm.getPaths(source,  target, numberOfPaths);
    return paths.stream().map(
      gp -> gp.getVertexList().stream().filter(v -> v.info != null).collect(Collectors.toList())
    );
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
