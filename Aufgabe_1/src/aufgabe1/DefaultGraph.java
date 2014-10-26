package aufgabe1;

import org.jgrapht.EdgeFactory;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.AbstractBaseGraph;

public class DefaultGraph extends AbstractBaseGraph<String, WeightedNamedEdge> implements UndirectedGraph<String, WeightedNamedEdge>
{
	public DefaultGraph()
	{
		super(new EdgeFactory<String, WeightedNamedEdge>()
		{
			@Override
			public WeightedNamedEdge createEdge(final String s, final String s2)
			{
				return new WeightedNamedEdge(s, s2, false);
			}
		}, true, true);
	}
}
