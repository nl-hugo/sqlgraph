package nl.hugojanssen.sqlgraph.model.sql;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.hugojanssen.sqlgraph.shared.SQLFileFilter;
import nl.hugojanssen.sqlgraph.shared.SQLParserUtil;
import nl.hugojanssen.sqlgraph.visitors.SQLVisitor;

import org.apache.log4j.Logger;

/**
 * A <code>SQLWorkflow</code> object contains the directories and files that are to be parsed. All files are accepted,
 * but only SQL files will be parsed. The parse results are communicated through the <code>SQLVisitor</code> objects
 * that can be registered with this workflow.
 * 
 * @author hjanssen
 */
public class SQLWorkflow
{
	/** The logger */
	private final static Logger LOG = Logger.getLogger( SQLWorkflow.class );

	private final String name;

	private List<File> workflowFiles = new ArrayList<File>();

	private List<SQLVisitor> visitors = new ArrayList<SQLVisitor>();

	/**
	 * Construct a new <code>SQLWorkflow</code> object with a dummy name.
	 */
	public SQLWorkflow()
	{
		this( "dummy" );
	}

	/**
	 * Construct a new <code>SQLWorkflow</code> object with the given name.
	 * 
	 * @param aName the workflow name
	 */
	public SQLWorkflow( String aName )
	{
		this.name = aName;
	}

	/**
	 * Add a visitor to the list of visitors.
	 * 
	 * @param aVisitor the visitor to add
	 */
	public void addVisitor( SQLVisitor aVisitor )
	{
		if ( aVisitor != null )
		{
			this.visitors.add( aVisitor );
		}
	}

	/**
	 * Add a file to parse to the workflow. All files are accepted, but only SQL files are parsed. Other files are
	 * simply ignored.
	 * 
	 * @param aFile a file or directory
	 * @throws IOException when the file does not exist or is not readable.
	 */
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

	/**
	 * Returns the name of the workflow.
	 * 
	 * @return the name of the workflow
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Returns all visitors.
	 * 
	 * @return the list of visitors
	 */
	public List<SQLVisitor> getVisitors()
	{
		return this.visitors;
	}

	/**
	 * Returns the list of files to parse.
	 * 
	 * @return the list of files to parse
	 */
	public List<File> getWorkflowFiles()
	{
		return this.workflowFiles;
	}

	/**
	 * Parses all files in the list of workflow files.
	 * 
	 * @return the number of errors that were encountered during parsing
	 * @throws IOException when a file was not readable
	 */
	public int parse() throws IOException
	{
		int result = 0;
		for ( File file : this.workflowFiles )
		{
			result += this.parse( file );
		}
		return result;
	}

	/**
	 * Removes all visitors.
	 */
	public void removeAllVisitors()
	{
		this.visitors.clear();
	}

	/**
	 * Removes all files.
	 */
	public void removeAllWorkflowFiles()
	{
		this.workflowFiles.clear();
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
		// TODO: signal completion
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
}