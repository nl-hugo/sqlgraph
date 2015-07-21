package nl.hugojanssen.sqlgraph;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import nl.hugojanssen.sqlgraph.model.graph.SQLGraphModel;
import nl.hugojanssen.sqlgraph.model.sql.SQLWorkflow;
import nl.hugojanssen.sqlgraph.visitors.TableVisitor;
import nl.hugojanssen.sqlgraph.visitors.TableVisitorListener;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class IntegrationTest
{
	private SQLWorkflow workflow;

	@BeforeTest
	public void setup()
	{
		this.workflow = new SQLWorkflow();
	}

	@BeforeMethod
	public void prepare()
	{
		System.out.println( "********* CLEAN ************" );
		SQLGraphModel.getInstance().clear();

		TableVisitorListener listener = new TableVisitorListener();
		TableVisitor visitor = new TableVisitor( listener );
		this.workflow.addVisitor( visitor );
		this.workflow.removeAllWorkflowFiles();

		assertThat( SQLGraphModel.getInstance().getNodeCount() ).isEqualTo( 0 );
		assertThat( SQLGraphModel.getInstance().getEdgeCount() ).isEqualTo( 0 );
	}

	@Test( description = "Test valid SQL scripts" )
	public void testRealStuff2() throws IllegalArgumentException, IOException
	{
		assertThat( SQLGraphModel.getInstance().getNodeCount() ).isEqualTo( 0 );

		File file = new File( this.getClass().getResource( "/scripts/valid/" ).getFile() );
		this.workflow.addWorkflowFile( file );
		assertThat( this.workflow.getWorkflowFiles().size() ).isEqualTo( 1 );

		this.workflow.parse();

		System.out.println( this.workflow.getWorkflowFiles() );
		System.out.println( SQLGraphModel.getInstance().getNodes() );

		// FIXME: laatste resultaten uit vorige file gaan mee! (TableVisitorListener.java:147)
		assertThat( SQLGraphModel.getInstance().getNodeCount() ).isEqualTo( 7 );
		assertThat( SQLGraphModel.getInstance().getEdgeCount() ).isEqualTo( 4 );

		SQLGraphModel.getInstance().toFile( new File( "examples/valid.gexf" ) );
	}
}
