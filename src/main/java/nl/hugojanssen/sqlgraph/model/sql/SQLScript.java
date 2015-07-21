package nl.hugojanssen.sqlgraph.model.sql;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;

import java.io.File;
import java.io.IOException;
import java.util.List;

import nl.hugojanssen.sqlgraph.shared.SQLConstants;
import nl.hugojanssen.sqlgraph.shared.SQLParserUtil;
import nl.hugojanssen.sqlgraph.shared.SQLProperties;
import nl.hugojanssen.sqlgraph.visitors.SQLVisitor;

import org.apache.log4j.Logger;

/**
 * The <code>SQLScript</code> object encapsulates a <code>File</code> object so that the file can be parsed. The file is
 * validated to ensure that it exists in the file system and has the correct file extension (.sql). This object relies
 * on the General SQL Parser (GSP) to parse the statements in the SQL script. Parse results are communicated to the
 * registered <code>SQLVisitor</code> objects for further processing. <br/>
 * <br/>
 * The GSP supports multiple database dialects. The properties.xml file defines which vendor dialect to use.<br/>
 * <br/>
 * Note that the trial version of the GSP is limited to file sizes of 10kB and expires 90 days after first use. The
 * limitation on the file size is not enforced by this class.
 * 
 * @author hjanssen
 * @see http://www.sqlparser.com/
 */
public class SQLScript extends File
{
	/** Default id */
	private static final long serialVersionUID = 1L;

	/** The logger */
	private final static Logger LOG = Logger.getLogger( SQLScript.class );

	/** The parser instance */
	private static TGSqlParser PARSER;

	/** The SQL dialect for this script */
	private EDbVendor dbVendor;

	/** The list of visitors to notify */
	private final List<SQLVisitor> visitors;

	/**
	 * Construct a new <code>SQLScript</code> object from the specified file and with the specified visitors.
	 * 
	 * @param aFile the SQL file
	 * @param visitors the list of visitors
	 * @throws IllegalArgumentException when the file is not specified or not an SQL file
	 * @throws IOException when the file does not exist or is not readable
	 */
	public SQLScript( File aFile, List<SQLVisitor> visitors ) throws IllegalArgumentException, IOException
	{
		super( aFile.getAbsolutePath() );
		this.visitors = visitors;
		this.setUp();

		this.validateState();
	}

	/**
	 * Parse the SQL script.
	 * 
	 * @return the number of errors, or 0 if no errors occurred
	 * @see gudusoft.gsqlparser.TGSqlParser.parse()
	 */
	public int parse()
	{
		LOG.info( "Parsing file: " + this );

		// Parse the file
		PARSER.setSqlfilename( super.getAbsolutePath() );
		int result = PARSER.parse();
		if ( result == 0 )
		{
			this.handleParserResult();
		}
		else
		{
			this.handleParserError();
		}
		return result;
	}

	private void handleParserError()
	{
		LOG.warn( PARSER.getErrorCount() + " Errors encountered while parsing '" + super.getName() + "': " + PARSER.getErrormessage() );
	}

	private void handleParserResult()
	{
		TStatementList list = PARSER.sqlstatements;
		for ( int i = 0; i < list.size(); i++ )
		{
			LOG.debug( "Parsing statement " + ( i + 1 ) + " of " + list.size() );
			SQLStatement statement = new SQLStatement( this, list.get( i ) );
			statement.setVisitors( this.visitors );
			statement.visit();
		}
	}

	private void setDBVendor()
	{
		String vendor = SQLProperties.getInstance().getProperty( SQLConstants.KEY_DB_VENDOR );
		try
		{
			this.dbVendor = EDbVendor.valueOf( vendor );
		}
		catch ( IllegalArgumentException e )
		{
			LOG.error( "Invalid db.vendor in properties.xml: " + vendor );
		}
	}

	private void setUp()
	{
		this.setDBVendor();
		PARSER = new TGSqlParser( this.dbVendor );
	}

	private void validateState() throws IllegalArgumentException, IOException
	{
		SQLParserUtil.validateSQLFile( super.getAbsoluteFile() );

		// validate parser
		if ( this.dbVendor == null )
		{
			throw new IllegalArgumentException( "Could not read db.vendor from properties.xml" );
		}
		if ( PARSER == null )
		{
			throw new IllegalArgumentException( "Could not instantiate GSP for vendor " + this.dbVendor );
		}
	}
}