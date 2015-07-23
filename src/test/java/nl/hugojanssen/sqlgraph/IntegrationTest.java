package nl.hugojanssen.sqlgraph;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import nl.hugojanssen.sqlgraph.io.SQLGraphExporter;
import nl.hugojanssen.sqlgraph.io.SQLGraphImporter;
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

	private File inFile, outFile;

	@BeforeTest
	public void setup()
	{
		this.workflow = new SQLWorkflow();
		this.inFile = new File( this.getClass().getResource( "/scripts/valid/" ).getFile() );
		this.outFile = new File( this.getClass().getResource( "/out/test.gexf" ).getFile() );
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

	@Test( groups = "read-graph", dependsOnGroups = "write-graph", description = "Test valid SQL scripts" )
	public void testReadGraph() throws IllegalArgumentException, IOException
	{
		// make sure the graph is empty before processing
		SQLGraphModel.getInstance().clear();
		assertThat( SQLGraphModel.getInstance().getNodeCount() ).isEqualTo( 0 );
		assertThat( SQLGraphModel.getInstance().getEdgeCount() ).isEqualTo( 0 );

		// read the graph
		SQLGraphImporter.getInstance().importGraph( this.outFile );
		assertThat( SQLGraphModel.getInstance().getNodeCount() ).isEqualTo( 7 );
		assertThat( SQLGraphModel.getInstance().getEdgeCount() ).isEqualTo( 4 );
	}

	@Test( groups = "write-graph", description = "Test valid SQL scripts" )
	public void testWriteGraph() throws IllegalArgumentException, IOException
	{
		// make sure the graph is empty before processing
		assertThat( SQLGraphModel.getInstance().getNodeCount() ).isEqualTo( 0 );
		assertThat( SQLGraphModel.getInstance().getEdgeCount() ).isEqualTo( 0 );

		this.workflow.addWorkflowFile( this.inFile );
		assertThat( this.workflow.getWorkflowFiles().size() ).isEqualTo( 1 );

		// parse
		this.workflow.parse();

		assertThat( SQLGraphModel.getInstance().getNodeCount() ).isEqualTo( 7 );
		assertThat( SQLGraphModel.getInstance().getEdgeCount() ).isEqualTo( 4 );

		// write
		SQLGraphExporter.getInstance().toGEXF( this.outFile );
	}
}
