package com.mcxross.cohesive.cps.utils

import java.util.*


/**
 * See [Wikipedia](https://en.wikipedia.org/wiki/Directed_graph) for more information.
 */
class DirectedGraph<V> {

    /**
     * The implementation here is basically an adjacency list, but instead
     * of an array of lists, a Map is used to map each vertex to its list of
     * adjacent vertices.
     */
    private val neighbors: MutableMap<V, MutableList<V>> = HashMap()

    /**
     * True if graph is a dag (directed acyclic graph).
     */
    val isDag: Boolean
        get() = topologicalSort() != null

    /**
     * Add a vertex to the graph. Nothing happens if vertex is already in graph.
     */
    fun addVertex(vertex: V) {
        if (containsVertex(vertex)) {
            return
        }
        neighbors[vertex] = ArrayList()
    }

    /**
     * True if graph contains vertex.
     */
    fun containsVertex(vertex: V): Boolean {
        return neighbors.containsKey(vertex)
    }

    fun removeVertex(vertex: V) {
        neighbors.remove(vertex)
    }

    /**
     * Add an edge to the graph; if either vertex does not exist, it's added.
     * This implementation allows the creation of multi-edges and self-loops.
     */
    fun addEdge(from: V, to: V) {
        addVertex(from)
        addVertex(to)
        neighbors[from]!!.add(to)
    }

    /**
     * Remove an edge from the graph. Nothing happens if no such edge.
     * @throws [IllegalArgumentException] if either vertex doesn't exist.
     */
    fun removeEdge(from: V, to: V) {
        require(containsVertex(from)) { "Nonexistent vertex $from" }
        require(containsVertex(to)) { "Nonexistent vertex $to" }
        neighbors[from]!!.remove(to)
    }

    fun getNeighbors(vertex: V): List<V> {
        return if (containsVertex(vertex)) neighbors[vertex]!! else ArrayList()
    }

    /**
     * Report (as a Map) the out-degree (the number of tail ends adjacent to a vertex) of each vertex.
     */
    fun outDegree(): Map<V, Int> {
        val result: MutableMap<V, Int> = HashMap()
        for (vertex in neighbors.keys) {
            result[vertex] = neighbors[vertex]!!.size
        }
        return result
    }

    /**
     * Report (as a [Map]) the in-degree (the number of head ends adjacent to a vertex) of each vertex.
     */
    fun inDegree(): MutableMap<V, Int> {
        val result: MutableMap<V, Int> = HashMap()
        for (vertex in neighbors.keys) {
            result[vertex] = 0 // all in-degrees are 0
        }
        for (from in neighbors.keys) {
            for (to in neighbors[from]!!) {
                result[to] = result[to]!! + 1 // increment in-degree
            }
        }
        return result
    }

    /**
     * Report (as a List) the topological sort of the vertices; null for no such sort.
     * See [this](https://en.wikipedia.org/wiki/Topological_sorting) for more information.
     */
    fun topologicalSort(): List<V?>? {
        val degree = inDegree()

        // determine all vertices with zero in-degree
        val zeroVertices = Stack<V>() // stack as good as any here
        for (v in degree.keys) {
            if (degree[v] == 0) {
                zeroVertices.push(v)
            }
        }

        // determine the topological order
        val result: MutableList<V?> = ArrayList()
        while (!zeroVertices.isEmpty()) {
            val vertex = zeroVertices.pop() // choose a vertex with zero in-degree
            result.add(vertex) // vertex 'v' is next in topological order
            // "remove" vertex 'v' by updating its neighbors
            for (neighbor in neighbors[vertex]!!) {
                degree[neighbor] = degree[neighbor]!! - 1
                // remember any vertices that now have zero in-degree
                if (degree[neighbor] == 0) {
                    zeroVertices.push(neighbor)
                }
            }
        }

        // check that we have used the entire graph (if not, there was a cycle)
        return if (result.size != neighbors.size) {
            null
        } else result
    }

    /**
     * Report (as a List) the reverse topological sort of the vertices; null for no such sort.
     */
    fun reverseTopologicalSort(): List<V> {
        val list = topologicalSort() ?: return emptyList()
        list.reversed()
        return emptyList()
    }

    /**
     * String representation of graph.
     */
    override fun toString(): String {
        val sb = StringBuilder()
        for (vertex in neighbors.keys) {
            sb.append("\n   ").append(vertex).append(" -> ").append(neighbors[vertex])
        }
        return sb.toString()
    }
}