package nl.hugojanssen.sqlgraph.model.sql;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.hugojanssen.sqlgraph.SQLFileFilter;
import nl.hugojanssen.sqlgraph.SQLParserUtil;
import nl.hugojanssen.sqlgraph.visitors.SQLVisitor;

import org.apache.log4j.Logger;

public class SQLWorkflow
{
	/** The logger */
	private final static Logger LOG = Logger.getLogger( SQLWorkflow.class );

	private final String name;

	private List<File> workflowFiles = new ArrayList<File>();

	private List<SQLVisitor> visitors = new ArrayList<SQLVisitor>();

	public SQLWorkflow()
	{
		this( "dummy" );
	}

	public SQLWorkflow( String aName )
	{
		this.name = aName;
	}

	public void addWorkflowFile( File aFile ) throws IOException
	{
		LOG.debug( "addWorkflowFile " + aFile );

		// Validate file first
		SQLParserUtil.validateFileOrDirectory( aFile );

		if ( !this.workflowFiles.contains( aFile ) )
		{
			this.workflowFiles.add( aFile );
		}
	}

	public String getName()
	{
		return this.name;
	}

	public List<File> getWorkflowFiles()
	{
		return this.workflowFiles;
	}

	public int parse() throws IOException
	{
		int result = 0;
		for ( File file : this.workflowFiles )
		{
			result += this.parse( file );
		}
		return result;
	}

	private int parse( final File aFile ) throws IOException
	{
		LOG.debug( "Parse " + aFile );
		int result = 0;

		if ( aFile.isDirectory() )
		{
			this.parseDirectory( aFile );
		}
		else if ( SQLParserUtil.isSQLFile( aFile ) )
		{
			SQLScript script = new SQLScript( aFile, this.visitors );
			result += script.parse();
		}
		else
		{
			LOG.debug( "Skipped file " + aFile.getName() + ": Not an SQL file" );
		}
		// signal completion
		//this.parent.setComplete();

		return result;
	}

	private void parseDirectory( final File aDirectory )
	{
		LOG.debug( "parseDirectory " + aDirectory );
		for ( File file : aDirectory.listFiles( new SQLFileFilter() ) )
		{
			try
			{
				this.parse( file );
			}
			catch ( IOException e )
			{
				LOG.warn( "Skipped file " + e.getMessage() );
			}
		}
	}

	public void addVisitor( SQLVisitor aVisitor )
	{
		if ( aVisitor != null )
		{
			this.visitors.add( aVisitor );
		}
	}

	public List<SQLVisitor> getVisitors()
	{
		return this.visitors;
	}

	public void removeAllVisitors()
	{
		this.visitors.clear();
	}
}