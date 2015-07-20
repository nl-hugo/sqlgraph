package nl.hugojanssen.sqlgraph.model.sql;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import nl.hugojanssen.sqlgraph.shared.SQLParserUtil;
import nl.hugojanssen.sqlgraph.visitors.SQLVisitor;

import org.apache.log4j.Logger;

/**
 * @author hjanssen
 */
public class SQLScript extends File
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The logger */
	private final static Logger LOG = Logger.getLogger( SQLScript.class );

	private static TGSqlParser PARSER = new TGSqlParser( EDbVendor.dbvpostgresql );

	private final List<SQLVisitor> visitors;

	/**
	 * @param aFile
	 * @param visitors
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 */
	public SQLScript( File aFile, List<SQLVisitor> visitors ) throws IllegalArgumentException, FileNotFoundException
	{
		super( aFile.getAbsolutePath() );
		SQLParserUtil.validateSQLFile( super.getAbsoluteFile() );
		this.visitors = visitors;
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
}