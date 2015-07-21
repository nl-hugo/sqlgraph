package nl.hugojanssen.sqlgraph.model.sql;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import nl.hugojanssen.sqlgraph.visitors.HelloWorldVisitor;
import nl.hugojanssen.sqlgraph.visitors.TableVisitor;
import nl.hugojanssen.sqlgraph.visitors.TableVisitorListener;

import org.fest.assertions.Fail;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SQLWorkflowTest
{
	private SQLWorkflow workflow;

	private File valid, invalid, validdir, nosql;

	@BeforeTest
	public void setup()
	{
		this.workflow = new SQLWorkflow();

		// load test files
		this.valid = new File( this.getClass().getResource( "/scripts/valid/tables/JoinTests.sql" ).getFile() );
		this.invalid = new File( this.getClass().getResource( "/scripts/invalid/InvalidSQLFile.sql" ).getFile() );
		this.nosql = new File( this.getClass().getResource( "/scripts/invalid/NotAnSQLFile.txt" ).getFile() );
		this.validdir = new File( this.getClass().getResource( "/scripts/valid/" ).getFile() );
	}

	@Test( groups = "visitors", dependsOnGroups = "addfiles", description = "Test addVisitor" )
	public void testAddVisitor()
	{
		this.workflow.addVisitor( null );
		assertThat( this.workflow.getVisitors().size() ).isEqualTo( 0 );

		TableVisitorListener listener = new TableVisitorListener();
		TableVisitor visitor = new TableVisitor( listener );
		this.workflow.addVisitor( visitor );

		assertThat( this.workflow.getVisitors().size() ).isEqualTo( 1 );

		this.workflow.addVisitor( new HelloWorldVisitor( listener ) );
		assertThat( this.workflow.getVisitors().size() ).isEqualTo( 2 );
	}

	@Test( groups = "addfiles", dependsOnGroups = "construct-workflow", description = "Tests addWorkflowFile" )
	public void testAddWorkflowFile()
	{
		try
		{
			this.workflow.addWorkflowFile( this.valid );
			this.workflow.addWorkflowFile( this.invalid );
			this.workflow.addWorkflowFile( this.validdir );
			this.workflow.addWorkflowFile( this.nosql );
			assertThat( this.workflow.getWorkflowFiles().size() ).isEqualTo( 4 );

			// try to add again
			this.workflow.addWorkflowFile( this.valid );
			assertThat( this.workflow.getWorkflowFiles().size() ).isEqualTo( 4 );
		}
		catch ( IOException e )
		{
			Fail.fail();
		}
	}

	@Test( groups = "addfiles", dependsOnGroups = "construct-workflow", description = "Tests addWorkflowFile null", expectedExceptions = java.lang.IllegalArgumentException.class )
	public void testAddWorkflowFileNull() throws IOException
	{
		this.workflow.addWorkflowFile( null );
	}

	@Test( groups = "construct-workflow", description = "Tests constructor" )
	public void testConstructor() throws IOException
	{
		assertThat( this.workflow ).isNotNull();
		assertThat( this.workflow.getName() ).isEqualTo( "dummy" );
		assertThat( this.workflow.getWorkflowFiles().size() ).isEqualTo( 0 );
		assertThat( this.workflow.getVisitors().size() ).isEqualTo( 0 );

		// parser should work on empty file list too.
		assertThat( this.workflow.parse() ).isEqualTo( 0 );
	}

	@Test( dependsOnGroups = "visitors", description = "Test parse" )
	public void testParse()
	{
		try
		{
			// the 'invalid' file will return an error code
			assertThat( this.workflow.parse() ).isGreaterThan( 0 );
		}
		catch ( IOException e )
		{
			Fail.fail();
		}
	}

	@AfterTest
	public void testRemoveAllVisitor()
	{
		assertThat( this.workflow.getVisitors().size() ).isNotEqualTo( 0 );
		this.workflow.removeAllVisitors();
		assertThat( this.workflow.getVisitors().size() ).isEqualTo( 0 );
	}

	@AfterTest
	public void testRemoveAllWorkflowFiles()
	{
		assertThat( this.workflow.getWorkflowFiles().size() ).isNotEqualTo( 0 );
		this.workflow.removeAllWorkflowFiles();
		assertThat( this.workflow.getWorkflowFiles().size() ).isEqualTo( 0 );
	}
}
