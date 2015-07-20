package nl.hugojanssen.sqlgraph.model.graph;

import static org.fest.assertions.Assertions.assertThat;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import nl.hugojanssen.sqlgraph.model.sql.EClauseType;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Node;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SQLGraphModelTest
{
	private SQLGraphModel model;

	private String script1 = "script1", script2 = "script2";

	private ParseResultNode node1, node2, node3t, node3s, node4t, node4s;

	private List<ParseResultNode> sources = new ArrayList<ParseResultNode>();

	@BeforeTest
	public void setup() throws IllegalArgumentException, FileNotFoundException
	{
		this.model = SQLGraphModel.getInstance();
		this.createNodes();
	}

	private void createNodes()
	{
		this.node1 = new ParseResultNode( this, "a", "table1", EClauseType.SOURCE, 1, 1 );
		this.node2 = new ParseResultNode( this, "a", "table2", EClauseType.SOURCE, 1, 1 );

		this.node3t = new ParseResultNode( this, "a", "table3", EClauseType.TARGET, 1, 1 );
		this.node3s = new ParseResultNode( this, "a", "table3", EClauseType.SOURCE, 1, 1 );

		this.node4t = new ParseResultNode( this, "a", "table4", EClauseType.TARGET, 1, 1 );
		this.node4s = new ParseResultNode( this, "a", "table4", EClauseType.SOURCE, 1, 1 );
	}

	@BeforeMethod
	public void clean()
	{
		this.model.clear();
		this.sources.clear();
	}

	@Test( description = "Test empty and null", expectedExceptions = IllegalArgumentException.class )
	public void testEmptyAndNull()
	{
		this.model.addGraphElements( this.script1, this.node4t, null );
		this.model.addGraphElements( this.script1, null, null );
		this.model.addGraphElements( null, null, null );

		this.sources.add( this.node4s );
		this.model.addGraphElements( this.script1, this.node4t, this.sources );
		this.model.addGraphElements( "", null, null );
	}

	@Test( description = "Test empty and null2" )
	public void testEmptyAndNull2()
	{
		this.sources.add( this.node4s );
		try
		{
			this.model.addGraphElements( "", this.node4t, this.sources );
		}
		catch ( IllegalArgumentException e )
		{
			assertThat( e.getMessage() ).isEqualTo( "Edge name should not be empty" );
		}
	}

	@Test( description = "Test equals" )
	public void testEquals()
	{
		assertThat( this.model.equals( null, null ) ).isTrue();

		this.sources.add( this.node4s );
		this.model.addGraphElements( this.script1, this.node4t, this.sources );
		assertThat( this.model.getNodeCount() ).isEqualTo( 1 );

		Node node = this.model.getNodes()[0];
		assertThat( this.model.equals( node, node ) ).isTrue();
		assertThat( this.model.equals( node, null ) ).isFalse();
		assertThat( this.model.equals( null, node ) ).isFalse();
	}

	@Test( description = "Test self loop" )
	public void testSelfLoop()
	{
		// node 4 -> 4
		this.sources.add( this.node4s );

		this.model.addGraphElements( this.script1, this.node4t, this.sources );
		assertThat( this.model.getNodeCount() ).isEqualTo( 1 );
		assertThat( this.model.getEdgeCount() ).isEqualTo( 1 );
	}

	@Test( description = "Test multiple edges" )
	public void testMultipleEdges()
	{
		// node 1 -> 3
		this.sources.add( this.node1 );
		this.model.addGraphElements( this.script1, this.node3t, this.sources );

		// node 2 -> 3
		this.sources.clear();
		this.sources.add( this.node2 );
		this.model.addGraphElements( this.script1, this.node3t, this.sources );

		// node 3 -> 4
		this.sources.clear();
		this.sources.add( this.node3s );
		this.model.addGraphElements( this.script2, this.node4t, this.sources );

		assertThat( this.model.getNodeCount() ).isEqualTo( 4 );
		assertThat( this.model.getEdgeCount() ).isEqualTo( 3 );
	}

	@Test( description = "Test simple edge" )
	public void testSimpleEdge()
	{
		// node 1 -> 3
		this.sources.add( this.node1 );

		this.model.addGraphElements( this.script1, this.node3t, this.sources );
		assertThat( this.model.getNodeCount() ).isEqualTo( 2 );
		assertThat( this.model.getEdgeCount() ).isEqualTo( 1 );
	}

	@Test( description = "Test parallel edges (not yet supported by Gephi)" )
	public void testParallelEdges()
	{
		// node 1 -> 3
		this.sources.add( this.node1 );

		this.model.addGraphElements( this.script1, this.node3t, this.sources );
		this.model.addGraphElements( this.script2, this.node3t, this.sources );

		assertThat( this.model.getNodeCount() ).isEqualTo( 2 );
		assertThat( this.model.getEdgeCount() ).isEqualTo( 1 );

		Edge[] edges = this.model.getEdges();
		Edge edge = edges[0];
		assertThat( edge.getSource().getNodeData().getId() ).isEqualTo( this.node1.getId() );
		assertThat( edge.getTarget().getNodeData().getId() ).isEqualTo( this.node3t.getId() );

		// TODO: is this the desired behavior for parallel edges?
		assertThat( edge.getEdgeData().getLabel() ).isEqualTo( this.script1 );
	}
}
