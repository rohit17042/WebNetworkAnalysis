# WebNetworkAnalysis
The Project identifies the communities within a web network using link of a webpage by another webpage. Identifying of "communities" within networks means identifying subsets or clusters containing the nodes with unusually strong or numerous connections.

## What is a Community?
Let's first define the word "Community".
>A community, with respect to graphs, can be defined as a subset of nodes that are densely connected to each other and loosely connected to the nodes in the other communities in the same graph.
>
Let's break that down using an example. Think about social media platforms such as Facebook, Instagram, or Twitter, where we try to connect with other people. Eventually, after a while, we end up being connected with people belonging to different social circles. These social circles can be a group of relatives, school mates, colleagues, etc.

These social circles are nothing but communities!

## What is Community Detection?
The concept of community detection has emerged in network science as a method for finding groups within complex systems through represented on a graph.

The ability to find communities within large networks in some automated fashion could be of considerable use. In a large scale network, such as an online social network, we could have millions of nodes and edges. Detecting communities in such network requires tremendous effort.

Therefore, we need community detection algorithms that can partition the network into multiple communities.

![Image of Community](https://github.com/rohit17042/WebNetworkAnalysis/blob/master/data/community.jpg?raw=true)

By visualising above picture, it can be clearly said that the small network can be partitioned into 3 communities.

## Data
I have used data that was released in 2002 by Google as a part of Google Programming Contest. You can get the data from [here!](https://snap.stanford.edu/data/web-Google.html)
* Nodes represent web pages and directed edges represent hyperlinks between them.
* Number of Nodes: 875713
* Number of Edges: 5105039
## Code Overview
Below is the overview of every java files that I have implemented.
### Graph
It's an interface that declares few methods that every network must contain. Ex: addEdge(), addNode(), removeEdge(), etc.
### WebGraph
The class implements "Graph" interface and contains information of data in the form of "graph" data structure. 
### WebGraphTest
It's a JUnit class which ensures that "WebGraph" class is working as expected.
### GraphLoader
It loads graph with data from a file. The file should consist of lines with 2 integers each, corresponding to a "from" vertex and a "to" vertex.
### SubGraphCreator
It creates a subgraph containing a list of given nodes from a source "WebGraph". It doesn't override original data of given graph, rather it will create a new instance of "WebGraph".
### Node
It resembles node of a graph having unique id.
### Edge
It resembles edge of a graph having sourceNode and destinationNode as instance variables.
### Suggester
It suggests desired number of similar nodes of a given id of a node.
### CommunityDetector
The class has one public method detectCommunity() which takes graph and targetNumOfCommunities as parameters and returns list of graphs. Every graph of the returned list is a unique community. 

The class detects communities using Girvan-Newman Algorithm. It is one of the most widely applied algorithms for social network graph clustering, based on detection of edges that are least likely to fall within the same cluster.
### CommunityDetectorTest
It's a JUnit class which ensures that "CommunityDetector" class is working as expected.

## Correctness
I had created several test cases for verifying the implementations. I had Created test classes by using JUnit5. Expected and actual answers were same for undirected graph. For directed graph, community detector is not answering as expected, so my next step would be to optimize the community detector algorithm for directed graph as well.
