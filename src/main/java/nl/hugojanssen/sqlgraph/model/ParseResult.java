package nl.hugojanssen.sqlgraph.model;

import java.util.EventObject;

import nl.hugojanssen.sqlgraph.model.sql.EClauseType;
import nl.hugojanssen.sqlgraph.shared.SQLConstants;
import nl.hugojanssen.sqlgraph.shared.SQLProperties;

/**
 * The ParseResult object holds details about the table that was found in a SQL statement by the parser.
 * 
 * @author hjanssen
 */
@SuppressWarnings( "serial" )
public class ParseResult extends EventObject
{
	private String schema;

	private final String name;

	private final EClauseType role;

	private final long lineNo;

	private final long colNo;

	/**
	 * Creates a new ParseResult object.
	 * 
	 * @param schema the name of the database schema
	 * @param name the name of the table
	 * @param role the role of the table
	 * @param lineNo the line number of the table in the SQL script
	 * @param colNo the column number of the table in the SQL script
	 */
	public ParseResult( Object source, String schema, String name, EClauseType role, long lineNo, long colNo )
	{
		super( source );
		this.schema = schema;
		this.name = name;
		this.role = role;
		this.lineNo = lineNo;
		this.colNo = colNo;

		this.validateState();
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj )
		{
			return true;
		}
		if ( obj == null )
		{
			return false;
		}
		if ( this.getClass() != obj.getClass() )
		{
			return false;
		}
		ParseResult other = (ParseResult) obj;
		if ( this.name == null )
		{
			if ( other.name != null )
			{
				return false;
			}
		}
		else if ( !this.name.equals( other.name ) )
		{
			return false;
		}
		if ( this.schema == null )
		{
			if ( other.schema != null )
			{
				return false;
			}
		}
		else if ( !this.schema.equals( other.schema ) )
		{
			return false;
		}
		return true;
	}

	/**
	 * Returns the column number of the table in the SQL script.
	 * 
	 * @return the column number
	 */
	public long getColNo()
	{
		return this.colNo;
	}

	/**
	 * @return
	 */
	public String getId()
	{
		return this.getSchema() + "." + this.getName();
	}

	/**
	 * Returns the line number of the table in the SQL script.
	 * 
	 * @return the line number
	 */
	public long getLineNo()
	{
		return this.lineNo;
	}

	/**
	 * Returns the name of the table.
	 * 
	 * @return the table name
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Returns the role of the table. A role is either SOURCE or TARGET.
	 * 
	 * @return the table role
	 */
	public EClauseType getRole()
	{
		return this.role;
	}

	/**
	 * Returns the name of the schema in which the table resides in the database.
	 * 
	 * @return the schema name, or an empty string if no name is provided in the SQL statement.
	 */
	public String getSchema()
	{
		return this.schema;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( this.name == null ) ? 0 : this.name.hashCode() );
		result = prime * result + ( ( this.schema == null ) ? 0 : this.schema.hashCode() );
		return result;
	}

	@Override
	public String toString()
	{
		return "ParseResult [schema=" + this.schema + ", name=" + this.name + ", role=" + this.role + ", lineNo=" + this.lineNo
			+ ", colNo=" + this.colNo + "]";
	}

	/**
	 * 
	 */
	private void validateState() throws IllegalArgumentException
	{
		if ( this.name == null || this.name.isEmpty() )
		{
			throw new IllegalArgumentException( "Name must be non-null and non-empty." );
		}
		if ( ( this.schema == null || this.schema.isEmpty() ) )
		{
			if ( this.role != null && this.role.equals( EClauseType.TEMP_TARGET ) )
			{
				this.schema = SQLProperties.getInstance().getProperty( SQLConstants.KEY_DB_TEMP_SCHEMA );
			}
			else
			{
				this.schema = SQLProperties.getInstance().getProperty( SQLConstants.KEY_DB_DEFAULT_SCHEMA );
			}
		}
		if ( this.role == null || !this.role.equals( EClauseType.SOURCE ) || !this.role.equals( EClauseType.TARGET )
			|| !this.role.equals( EClauseType.TEMP_TARGET ) )
		{
			//	throw new IllegalArgumentException( "Role must be SOURCE or TARGET for " + this.name );
		}
	}
}