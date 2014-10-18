package aufgabe1

import java.nio.charset.Charset
/*For Parsing .gka-Files, to get Vertices and Edges.
 *Written by Sebastian Diedrich
 *Date: 2014-10-10
 *
 *Two different Types of Edges are possible, "edge name" and "edgeweight" are optional:
 *	 gerichtet (directed)
 *	 <name node1>[ -> <name node2>][(edge name)][: <edgeweight>];
 *
 *	 ungerichtet (undirected)
 *	 <name node1>[ -- <name node2>][(edge name)][: <edgeweight>];
 */

class GroovyParser {
	//Variables
	String path = "";
	int count = 0;
	ArrayList noMatchFound = new ArrayList();
	
	//Method for setting the path of the file
	public void setPath(String pathOfFile){
		path = pathOfFile
	}
	
	//Method for resetting noMatchFound
	public void resetNoMatchFound(){
		noMatchFound = new ArrayList();
	}
	
	//Method for parsing each line of the gka-file
	def parse(){
		
			//define Pattern (java.util.regex.Pattern)
			//each "(...)" contains later a part of the match; $=end of Pattern
			//word=w=[A-Za-z0-9_] ; digits=d=[0-9] ; whitespaceCharacter=s=[ \t\r\n\v\f]
			//CAVE: *-blanks are possible between the match-parts!!!
			def graphPart = ~/ *([A-Za-z0-9_äüö]+) *((--|->) *([A-Za-z0-9_äüö]+) *([A-Za-z0-9_äüö]+)* *:* *(\d+)*)? *;$/

			//parsing File with InputStreamReader to be able to use a different charset in order to parse umlauts.
            List<GraphPart> parts = new ArrayList<>();
            InputStreamReader reader = new InputStreamReader(new FileInputStream(path), Charset.forName("ISO-8859-1"));
			reader.eachWithIndex(){line, index ->
                if(line.length() == 0) { // Simply ignore lines which are empty.
                    return;
                }
				//define matcher-Object ; if line=graphPart then a matcher-Object is created
				def matcher = line =~ graphPart
					//four match-parts found
					if(matcher.size() == 1){
						//combine variables with the match
						def (ganzeZeile, vertexStart, edgeDef, edgeType, vertexEnd, edgeName, edgeWeihgt) = matcher[0]
							 println "Das ist der Startknoten: ${vertexStart}"
							 println "Das ist der Kantentyp: ${edgeType}"
							 println "Das ist der Endknoten: ${vertexEnd}"
							 println "Das ist das Kantenname: ${edgeName}"
							 println "Das ist das Kantengewicht: ${edgeWeihgt}"

                             WeightedNamedEdge edge = null;
                             if(vertexEnd != null) {
                                 edge = new WeightedNamedEdge(vertexStart, vertexEnd, edgeType.equals("->"))
                                 if (edgeWeihgt != null) {
                                     edge.setWeigth(Integer.parseInt(edgeWeihgt))
                                 }

                                 if (edgeName != null) {
                                     edge.setName(edgeName)
                                 }
                             }
                             parts.add(new GraphPart(vertexStart, edge));
							 count++
							 println ""
					}
					else{
						int realIndex = index+1
						int indexForArray = noMatchFound.size()
						noMatchFound.add(indexForArray, realIndex)
					}
		}
		println "Eingelesene Zeilen: ${count}"
        println "Graph: ${graph}"
		println "Keine Übereinstimmung in ${noMatchFound}"
	}
	
}
